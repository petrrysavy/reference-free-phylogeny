package rysavpe1.reads.io;

import rysavpe1.reads.io.utils.SequenceChecker;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import rysavpe1.reads.model.ReadBag;
import rysavpe1.reads.model.Sequence;

/**
 * This interface defines a common interface for loading and storing sequences
 * to files.
 * @author Petr Ryšavý
 */
public interface Loader {

    public Sequence loadSequence(BufferedReader in) throws IOException;
    
    public Sequence loadSequence(Path file) throws IOException;
    
    public Sequence[] loadSequences(Iterable<Path> files) throws IOException;
    
    public Sequence[] loadSequences(Path directory, Iterable<String> filenames) throws IOException;
    
    public void writeSequence(Sequence seq, BufferedWriter bw) throws IOException;
    
    public void writeSequence(Sequence seq, Path file) throws IOException;

    public ReadBag loadReadBag(BufferedReader in) throws IOException;
    
    public ReadBag loadReadBag(Path file) throws IOException;
    
    public ReadBag[] loadReadBags(Iterable<Path> files) throws IOException;
    
    public ReadBag[] loadReadBags(Path directory, Iterable<String> filenames) throws IOException;
    
    public void writeReadBag(ReadBag bag, Path file) throws IOException;
    
    public void writeReadBag(ReadBag bag, BufferedWriter bw) throws IOException;
    
    public void setSequenceChecker(SequenceChecker chck);
}
