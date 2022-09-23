package rysavpe1.reads.io.utils;

/**
 *
 * @author petr
 */
public interface SequenceChecker {

    public static final SequenceChecker DUMMY = (seq) -> seq;
    public static final SequenceChecker STOP_ON_ERROR = (seq) -> SequenceCheckers.checkFASTASequence(seq);
    public static final SequenceChecker DERANDOMIZE = (seq) -> SequenceCheckers.derandomizeFASTASequence(seq);

    public char[] checkSequence(char[] seq);
}
