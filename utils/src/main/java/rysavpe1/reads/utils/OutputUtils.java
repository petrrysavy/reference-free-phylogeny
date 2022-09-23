package rysavpe1.reads.utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author petr
 */
public class OutputUtils {

    private OutputUtils() {
    }

    public static List<String> printMatrix(double[][] matrix, double[] rowHeaders, double[] colHeaders,
            String midHeader, String doubleFormat) {

        final ArrayList<String> list = new ArrayList<>(matrix.length + 1);
        list.add(midHeader + StringUtils.toStringFormatted(colHeaders, doubleFormat));
        for (int i = 0; i < matrix.length; i++)
            list.add(String.format(doubleFormat, rowHeaders[i]) + StringUtils.toStringFormatted(matrix[i], doubleFormat));
        return list;

    }

    public static List<String> printMatrix(double[][] matrix, String[] rowHeaders, String[] colHeaders,
            String midHeader, String doubleFormat, String stringFormat) {

        final ArrayList<String> list = new ArrayList<>(matrix.length + 1);
        list.add(midHeader + StringUtils.toStringFormatted(colHeaders, stringFormat));
        for (int i = 0; i < matrix.length; i++)
            list.add(String.format(stringFormat, rowHeaders[i]) + StringUtils.toStringFormatted(matrix[i], doubleFormat));
        return list;

    }

    public static List<String> printMatrix(double[][] matrix, int[] rowHeaders, int[] colHeaders,
            String midHeader, String doubleFormat, String intFormat) {

        final ArrayList<String> list = new ArrayList<>(matrix.length + 1);
        list.add(midHeader + StringUtils.toStringFormatted(colHeaders, intFormat));
        for (int i = 0; i < matrix.length; i++)
            list.add(String.format(intFormat, rowHeaders[i]) + StringUtils.toStringFormatted(matrix[i], doubleFormat));
        return list;

    }

    public static List<String> printMatrixPhylipFormat(double[][] matrix, String[] sampleNames) {
        final ArrayList<String> list = new ArrayList<>(matrix.length + 1);
        list.add(Integer.toString(matrix.length));
        for (int i = 0; i < matrix.length; i++)
            list.add(StringUtils.toFixedWidth(sampleNames[i], 10) + StringUtils.toStringFormatted(matrix[i], "%12.10f "));
        return list;
    }

    public static List<String> printDependency(int[] variable, double[] result,
            String header, String doubleFormat, String intFormat) {

        final ArrayList<String> list = new ArrayList<>(variable.length + 1);
        list.add(header);
        for (int i = 0; i < variable.length; i++)
            list.add(String.format(intFormat, variable[i]) + String.format(doubleFormat, result[i]));
        return list;
    }

    public static List<String> print2DDependency(double[][] matrix, int[] rowVariable, int[] colVariable,
            String header, String doubleFormat, String intFormat) {
        final ArrayList<String> list = new ArrayList<>(rowVariable.length * colVariable.length + 1);
        if (header != null)
            list.add(header);
        for (int i = 0; i < rowVariable.length; i++)
            for (int j = 0; j < colVariable.length; j++)
                list.add(String.format(intFormat, rowVariable[i])+String.format(intFormat, colVariable[j])+String.format(doubleFormat, matrix[i][j]));
        return list;
    }

    public static List<String> printSeries(int[] series) {
        final ArrayList<String> list = new ArrayList<>(series.length);
        for (int val : series)
            list.add(Integer.toString(val));
        return list;
    }
}
