package rysavpe1.reads.io;

import rysavpe1.reads.io.utils.Utils;
import rysavpe1.reads.io.utils.FileOpener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import rysavpe1.reads.io.utils.SequenceChecker;
import rysavpe1.reads.model.ReadBag;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.utils.IOUtils;

/**
 * Abstract implementation of the loader of genomic sequences.
 *
 * @author Petr Ryšavý
 */
public abstract class AbstractLoader implements Loader {

    SequenceChecker checker = SequenceChecker.DUMMY;
    final FileOpener opener;

    public AbstractLoader(FileOpener opener) {
        if(opener == null) throw new NullPointerException();
        this.opener = opener;
    }

    @Override
    public Sequence loadSequence(Path file) throws IOException {
        try (BufferedReader br = opener.openForReading(file)) {
            Sequence s = loadSequence(br);
            s.setFile(file);
            return s;
        }
    }

    @Override
    public Sequence[] loadSequences(Iterable<Path> files) throws IOException {
        List<Sequence> sequnces = new ArrayList<>();
        for (Path p : files) sequnces.add(loadSequence(p));
        Sequence[] arr = new Sequence[sequnces.size()];
        return sequnces.toArray(arr);
    }

    @Override
    public Sequence[] loadSequences(Path directory, Iterable<String> filenames) throws IOException {
        return loadSequences(IOUtils.resolve(directory, filenames));
    }

    @Override
    public ReadBag loadReadBag(BufferedReader br) throws IOException {
        ReadBag bag = new ReadBag(32, null);
        br.mark(1);
        while (br.ready() && br.read() != -1) {
            br.reset();
            bag.add(loadSequence(br));
            br.mark(1);
        }
        return bag;
    }

    @Override
    public ReadBag loadReadBag(Path file) throws IOException {
        try (BufferedReader br = opener.openForReading(file)) {
            ReadBag rb = loadReadBag(br);
            rb.setDescription(Utils.stripKnownExtension(file.getFileName().toString()));
            rb.setFile(file);
            for (Sequence s : rb) s.setFile(file);
            return rb;
        }
    }

    @Override
    public ReadBag[] loadReadBags(Iterable<Path> files) throws IOException {
        List<ReadBag> bags = new ArrayList<>();
        for (Path p : files) bags.add(loadReadBag(p));
        ReadBag[] arr = new ReadBag[bags.size()];
        return bags.toArray(arr);
    }

    @Override
    public ReadBag[] loadReadBags(Path directory, Iterable<String> filenames) throws IOException {
        return loadReadBags(IOUtils.resolve(directory, filenames));
    }

    @Override
    public void writeReadBag(ReadBag bag, BufferedWriter bw) throws IOException {
        for (Sequence s : bag) writeSequence(s, bw);
    }

    @Override
    public void writeSequence(Sequence seq, Path file) throws IOException {
        try (BufferedWriter bw = opener.openForWriting(file)) {
            writeSequence(seq, bw);
        }
    }

    @Override
    public void writeReadBag(ReadBag bag, Path file) throws IOException {
        try (BufferedWriter bw = opener.openForWriting(file)) {
            writeReadBag(bag, bw);
        }
    }

    @Override
    public void setSequenceChecker(SequenceChecker chck) {
        if (chck == null) throw new NullPointerException();
        this.checker = chck;
    }

}
