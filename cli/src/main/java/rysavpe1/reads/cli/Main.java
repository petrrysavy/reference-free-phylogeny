package rysavpe1.reads.cli;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;
import rysavpe1.reads.combined.multiprojected.CombinedQGramDistance;
import rysavpe1.reads.combined.multiprojected.model.CombinedInput;
import rysavpe1.reads.io.FileType;
import rysavpe1.reads.io.utils.MismatchedFileException;
import rysavpe1.reads.model.ReadBag;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.utils.IOUtils;
import rysavpe1.reads.utils.MathUtils;
import rysavpe1.reads.utils.OutputUtils;
import rysavpe1.reads.utils.StringUtils;

/**
 * The main distance file.
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
@Command(name = "distanceMatrix",
        footer = "Reference-Free Phylogeny from Sequencing Data",
        description = "This program allows distance matrix calculation for unassembled reads and/or contigs.")
public class Main implements Runnable {

    @Option(names = {"-t", "--sampleCoverage"}, defaultValue = "2.0",
            description = "Reads bags will be downsampled to the average coverage specified by this parameter. Use this parameter to control runtime vs. precision. Use -n option to disable sampling.",
            descriptionKey = "sampleCoverage")
    private double targetCoverage;

    @Option(names = {"-a", "--no-sampling"}, description = "This option diables sampling. I.e., all reads are used without any filtering.")
    private boolean noSampling;
    @Option(names = {"-n", "--n-cores"}, defaultValue = "-1", description = "This option set the number of cores. Default value -1 is (the number of cores -1).")
    private int nCores;

    @Option(names = {"-d", "--sequencingDepth"}, description = "Coverage of the providede read bags.", defaultValue = "NaN")
    private double coverage;

    @Option(names = {"-l", "--genomeLengths"}, description = "Length of the genomes, if known.", split = ",", required = false, arity = "1..*", descriptionKey = "genomeLength")
    private final List<Integer> genomeLenghtsL = new ArrayList<>();
    private int[] genomeLenghts;

    @Option(names = {"-r", "--reads"}, description = "FASTA/Q files with raw reads", split = ",", required = false, arity = "1..*", descriptionKey = "readsFile")
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final List<Path> readsFilesL = new ArrayList<>();
    private Path[] readsFiles;

    @Option(names = {"-c", "--contigs"}, description = "FASTA files with contigs", split = ",", required = false, arity = "1..*", descriptionKey = "contigFile")
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final List<Path> contigsFilesL = new ArrayList<>();
    private Path[] contigsFiles;

    @Option(names = {"-o", "--output"}, description = "Output file for the distance matrix.", required = true, defaultValue = "distanceMatrix.dat")
    private Path output;

    @Spec
    private CommandSpec spec;

    private final static Logger LOGGER = Logger.getLogger(Main.class.getName());

    /** The read length will be calculated over validation of the read inputs. */
    private int readLength = -1;

    public static void main(String[] args) {
        LOGGER.info("Started the program with the parameters:");
        LOGGER.info(StringUtils.toString(args, " "));
        CommandLine.run(new Main(), args);
    }

    @Override
    public void run() {
        validateInput();

        CombinedInput[] input;
        try {
            LOGGER.info("Going to read the input.");
            input = readInput();
            LOGGER.info("Input successfully loaded.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading input.", e);
            return;
        }

        // estimate the coverage ...
        double estimatedCoverage = noSampling ? coverage : targetCoverage;
        if (Double.isNaN(estimatedCoverage)) {
            int allReadsCount = 0;
            for (CombinedInput bags : input)
                allReadsCount += bags.value1.size();
            estimatedCoverage = ((double) allReadsCount * readLength) / MathUtils.sum(genomeLenghts);
            if (estimatedCoverage == 0.0) estimatedCoverage = 1; // only contigs
            LOGGER.log(Level.FINE, "Estimated coverage as {0}.", estimatedCoverage);
        }

        // calculate the distance matrix and save the result
        CombinedQGramDistance combinedDist = new CombinedQGramDistance(readLength, estimatedCoverage, 20, true, true);
        final double[][] distanceMatrix = new double[input.length][input.length];
        try {
            LOGGER.info("Going to calculate the distance matrix.");
            ExecutorService exec = Executors.newFixedThreadPool(nCores);
            List<Callable<Void>> tasks = new ArrayList<>();
            for (int i = 0; i < input.length; i++)
                for (int j = 0; j < i; j++) {
                    tasks.add(new TaskCallable(i, j, distanceMatrix, combinedDist, input));
                }
            List<Future<Void>> futures = exec.invokeAll(tasks);
            exec.shutdown();
            exec.awaitTermination(365, TimeUnit.DAYS);
            for(Future<Void> future : futures) {
                try {
                    future.get();
                } catch(ExecutionException ex) {
                    LOGGER.log(Level.SEVERE, "Exception during distance calculation.", ex);
                }
            }

            LOGGER.info("Distance matrix calculated");
            Path[] headersFiles = readsFiles.length == 0 ? contigsFiles : readsFiles;
            String[] headers = new String[headersFiles.length];
            for (int i = 0; i < headers.length; i++)
                headers[i] = headersFiles[i].getFileName().toString();
            IOUtils.write(output, OutputUtils.printMatrix(distanceMatrix, headers, headers, StringUtils.kCopies(" ", 22), "%12.10f ", "%22s "));
        } catch (InterruptedException ex) {
            LOGGER.log(Level.SEVERE, "The program was interrupted.", ex);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Cannot write the output file.", ex);
        }
    }

    private void validateInput() {
        genomeLenghts = genomeLenghtsL.stream().mapToInt(Integer::intValue).toArray();
        readsFiles = readsFilesL.toArray(new Path[readsFilesL.size()]);
        contigsFiles = contigsFilesL.toArray(new Path[contigsFilesL.size()]);
        if(readsFiles.length == 0) readLength = 100;

        if (Double.isNaN(coverage) && genomeLenghts.length == 0)
            throw new CommandLine.ParameterException(spec.commandLine(), "Provide either -d or -l option.");
        if (!Double.isNaN(coverage) && genomeLenghts.length != 0)
            throw new CommandLine.ParameterException(spec.commandLine(), "Do not use -d option together with -l option.");
        if (coverage < 1e-6 || coverage > 1e6) // holds for Inf as well
            throw new CommandLine.ParameterException(spec.commandLine(), "Coverage (-d option) can be only between 1e-6 and 1e6.");
        if (targetCoverage < 1e-6 || targetCoverage > 1e6 || Double.isNaN(targetCoverage))
            throw new CommandLine.ParameterException(spec.commandLine(), "Sampling coverage (-t option) can be only between 1e-6 and 1e6.");

        if (readsFiles.length == 0 && contigsFiles.length == 0)
            throw new CommandLine.ParameterException(spec.commandLine(), "Provide either read bags (-r option) and/or contigs (-c option).");
        if (readsFiles.length != 0 && contigsFiles.length != 0 && readsFiles.length != contigsFiles.length)
            throw new CommandLine.ParameterException(spec.commandLine(), "When reads (-r option) and contigs (-c option) are provided, the lists need to have the same length.");

        if (genomeLenghts.length != 0 && genomeLenghts.length != Math.max(readsFiles.length, contigsFiles.length))
            throw new CommandLine.ParameterException(spec.commandLine(), "Genome lengths (-l option) must be provided for all samples, found "+genomeLenghts.length+", epected "+Math.max(readsFiles.length, contigsFiles.length)+".");
        if(readsFiles.length == 0 && genomeLenghts.length == 0)
            throw new CommandLine.ParameterException(spec.commandLine(), "If no reads are provided, then the genomes lengths (-l option) must be specified.");

        if (nCores <= 0 && nCores != -1)
            throw new CommandLine.ParameterException(spec.commandLine(), "Number of cores cannot be negative.");
        if (nCores > 1e4)
            throw new CommandLine.ParameterException(spec.commandLine(), "Number of cores cannot be greater than 10,000.");
        if (nCores == -1)
            nCores = Runtime.getRuntime().availableProcessors() - 1;
        if (nCores > Runtime.getRuntime().availableProcessors())
            LOGGER.warning("Then number of cores is greater than the number of available processors. This might be ineffective.");
    }

    private CombinedInput[] readInput() throws IOException {
        final int N = Math.max(readsFiles.length, contigsFiles.length);

        final ReadBag[] bags = new ReadBag[N];
        final int[] originalReadCount = new int[N];
        // first filter out unusable sequences
        for (int i = 0; i < bags.length; i++) {
            if (readsFiles.length == 0)
                bags[i] = new ReadBag(0, "Empty read bag");
            else {
                ReadBag original = FileType.guessFileType(readsFiles[i]).loader.loadReadBag(readsFiles[i]);
                ReadBag filtered = new ReadBag(original.uniqueSize(), original.getDescription(), original.getFile());
                originalReadCount[i] = original.size();
                if(originalReadCount[i] == 0) throw new MismatchedFileException("File "+original.getFile()+" appears to be empty.");
                sLoop:
                for (Sequence s : original.toSet()) {
                    for (char ch : s.getSequence())
                        if (ch != 'A' && ch != 'C' && ch != 'G' && ch != 'T')
                            continue sLoop;

                    if (readLength == -1) readLength = s.length();
                    if (readLength != s.length())
                        throw new MismatchedFileException("All reads are assumed to have the same length. Read " + s + " does not have length " + readLength + ".");
                    filtered.add(s, original.count(s));
                }
                LOGGER.log(Level.FINE, "Loaded read bag {0} and found {1} usable reads out of {2}.", new Object[]{readsFiles[i], filtered.size(), original.size()});
                bags[i] = filtered;

                // finally, sample to the size
                if (!noSampling) {
                    double samplingRatio = genomeLenghts.length == 0 ? ((double) targetCoverage) / coverage
                            : ((double) targetCoverage * genomeLenghts[i] / readLength) / bags[i].size();
                    bags[i] = bags[i].sample(samplingRatio);
                }
                LOGGER.log(Level.FINE, "Final size of the bag after sampling is {0}", filtered.size());
            }
        }

        // now read the contigs
        final ReadBag[] contigs = new ReadBag[N];
        for (int i = 0; i < contigs.length; i++)
            contigs[i] = contigsFiles.length == 0 ? new ReadBag(0, "Empty contig set")
                    : FileType.guessFileType(contigsFiles[i]).loader.loadReadBag(contigsFiles[i]);

        // and filter redundant reads (that are already in the contigs)
        final FilterRedundantReadsAndEmbedd filterAndEmbed = new FilterRedundantReadsAndEmbedd(readLength);
        CombinedInput[] input = new CombinedInput[N];
        for (int i = 0; i < bags.length; i++)
            input[i] = filterAndEmbed.preprocessData(bags[i], contigs[i], genomeLenghts.length != 0 ? genomeLenghts[i] : (int) (((double) originalReadCount[i] * readLength) / coverage));

        return input;
    }

    /**
     * This class represents a single thread job to calculate a single entry in
     * a distance matrix.
     */
    static class TaskCallable implements Callable<Void> {

        /** The indices in the matrix. */
        final int i, j;
        /** The matrix to fill. */
        final double[][] distanceMatrix;
        /** Distance to use. */
        final CombinedQGramDistance combinedDist;
        /** Input reads and contigs. */
        final CombinedInput[] inputs;

        public TaskCallable(int i, int j, double[][] distanceMatrixMethod, CombinedQGramDistance combinedDist, CombinedInput[] inputs) {
            this.i = i;
            this.j = j;
            this.distanceMatrix = distanceMatrixMethod;
            this.combinedDist = combinedDist;
            this.inputs = inputs;
        }

        @Override
        public Void call() throws Exception {
            LOGGER.log(Level.FINE, "Starting to calculate the field ({0}, {1}) in the distance matrix.", new Object[]{i, j});

            distanceMatrix[i][j] = distanceMatrix[j][i] = combinedDist.getDistance(inputs[i], inputs[j]);
            return null;
        }

    }
}
