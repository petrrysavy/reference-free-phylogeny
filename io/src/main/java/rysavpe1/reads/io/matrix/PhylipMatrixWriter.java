package rysavpe1.reads.io.matrix;

import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class PhylipMatrixWriter extends MatrixWriter {

    @Override
    protected void storeMatrixSafe(double[][] matrix, String[] labels, Writer output) throws IOException {
        output.append(Integer.toString(labels.length)).append('\n');
        for(double[] vec : matrix) {
            for(int i = 0; i < vec.length - 1; i++)
                output.append(Double.toString(vec[i])).append('\t');
            output.append(Double.toString(vec[vec.length - 1])).append('\n');
        }
    }
    
}
