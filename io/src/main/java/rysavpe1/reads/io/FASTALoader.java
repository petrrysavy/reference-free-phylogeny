package rysavpe1.reads.io;

import rysavpe1.reads.io.utils.MismatchedFileException;
import rysavpe1.reads.io.utils.FileOpener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import rysavpe1.reads.io.utils.DefaultFileOpener;
import rysavpe1.reads.model.ReadBag;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.settings.Settings;
import rysavpe1.reads.utils.StringUtils;

/**
 * This class provides functionality for loading FASTA files.
 *
 * @author Petr Ryšavý
 */
public final class FASTALoader extends AbstractLoader {

    public FASTALoader(FileOpener opener) {
        super(opener == null ? DefaultFileOpener.INSTANCE : opener);
    }

    @Override
    public Sequence loadSequence(BufferedReader br) throws IOException {
        final String description = br.readLine();
        checkDescription(description);

        ArrayList<String> lines = new ArrayList<>(1024);
        String line = collectSequence(br, lines);
        if (line != null)
            Settings.LOGGER.log(Level.INFO, "File may contain more than one FASTA sequence. Loading only the first one.");

        return new Sequence(checker.checkSequence(StringUtils.toCharArray(lines)), description.trim().substring(1));
    }

    @Override
    public ReadBag loadReadBag(BufferedReader br) throws IOException {
        ReadBag bag = new ReadBag(32, null);

        String line = br.readLine();
        while (line != null) {
            final String description = line;
            checkDescription(description);

            ArrayList<String> lines = new ArrayList<>(1024);
            line = collectSequence(br, lines);

            bag.add(new Sequence(checker.checkSequence(StringUtils.toCharArray(lines)), description.trim().substring(1)));
        }

        return bag;
    }

    @Override
    public void writeSequence(Sequence seq, BufferedWriter bw) throws IOException {
        bw.append('>').append(seq.getDescription() == null ? "sequence" : seq.getDescription()).append('\n');
        bw.write(seq.getSequence());
        bw.append('\n');
    }

    private void checkDescription(final String description) throws MismatchedFileException {
        if (description.isEmpty() || description.charAt(0) != '>')
            throw new MismatchedFileException("File does not match FASTA format, illegal description" + description);
    }

    private String collectSequence(BufferedReader br, ArrayList<String> lines) throws IOException {
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty() && line.charAt(0) != '>')
            lines.add(line);
        return line;
    }
}
