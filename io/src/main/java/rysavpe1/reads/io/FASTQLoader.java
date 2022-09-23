package rysavpe1.reads.io;

import rysavpe1.reads.io.utils.FileOpener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import rysavpe1.reads.io.utils.DefaultFileOpener;
import rysavpe1.reads.io.utils.MismatchedFileException;
import rysavpe1.reads.model.Sequence;

/**
 * This class provides functionality for loading FASTQ files.
 *
 * @author petr
 */
public final class FASTQLoader extends AbstractLoader {

    public FASTQLoader(FileOpener opener) {
        super(opener == null ? DefaultFileOpener.INSTANCE : opener);
    }

    @Override
    public Sequence loadSequence(BufferedReader br) throws IOException {
        final String description = br.readLine();
        final String sequence = br.readLine();
        final String optional = br.readLine();
        final String quality = br.readLine();

        if (description.isEmpty() || description.charAt(0) != '@'
                || sequence.isEmpty()
                || optional.isEmpty() || optional.charAt(0) != '+'
                || quality.isEmpty()
                || quality.length() != sequence.length())
            throw new MismatchedFileException("File does not match FASTQ format.");

        return new Sequence(checker.checkSequence(sequence.toCharArray()), description.trim().substring(1));
    }

    @Override
    public void writeSequence(Sequence seq, BufferedWriter bw) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
