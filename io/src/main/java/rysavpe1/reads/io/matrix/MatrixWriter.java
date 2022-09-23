package rysavpe1.reads.io.matrix;

import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public abstract class MatrixWriter {
    protected abstract void storeMatrixSafe(double[][] matrix, String[] labels, Writer output) throws IOException;
    
    public void storeMatrix(double[][] matrix, String[] labels, Writer output) throws IOException {
        final int size = labels.length;
        if(matrix.length != size) throw new IllegalArgumentException("Wrong matrix size: "+matrix.length);
        for(double[] vec : matrix) if(vec.length != size) throw new IllegalArgumentException("Wrong matrix size: "+vec.length);
        storeMatrixSafe(matrix, labels, output);
    }
}
