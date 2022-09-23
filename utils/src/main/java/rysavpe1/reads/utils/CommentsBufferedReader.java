package rysavpe1.reads.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * A buffered reader that ignores lines that starts with a special substring.
 *
 * @author Petr Ryšavý
 */
public class CommentsBufferedReader extends BufferedReader {

    /** Comment symbol. */
    private String commentMark;

    /**
     * Buffered reader that skips lines that start with {@code commentMark}.
     * @param commentMark Symbol to quote comment.
     * @param in Decorated reader.
     */
    public CommentsBufferedReader(String commentMark, Reader in) {
        super(in);
        this.commentMark = commentMark;
    }

    /**
     * Gets the symbol used to quote comments.
     * @return The comment symbol.
     */
    public String getCommentMark() {
        return commentMark;
    }

    /**
     * Sets the symbol used to quote comments.
     * @param commentMark New symbol that marks comments.
     */
    public void setCommentMark(String commentMark) {
        this.commentMark = commentMark;
    }

    /**
     * Reads a line and ignores everything that is behind {@code commentMark}.
     * @return Important part of the line.
     * @throws IOException When there is a problem with IO.
     */
    public String readLineNoComment() throws IOException {
        String line = readLine();
        int index = line.indexOf(commentMark);
        return index == -1 ? line : line.substring(0, index);
    }

    /**
     * Reads the first line that is not empty. We ignore leading and trailing
     * whitespaces and comments.
     * @return First line that contain some information.
     * @throws IOException
     */
    public String readNonemptyLine() throws IOException {
        String line;
        do {
            line = readLineNoComment().trim();
        } while (line != null && line.isEmpty());
        return line;
    }
}
