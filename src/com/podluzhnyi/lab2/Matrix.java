package com.podluzhnyi.lab2;

public class Matrix
{
    private double[][] matrix;

    public Matrix(double[]... rows)
    {
        matrix = new double[4][];
        for (int i = 0; i < 4; i++) {
            matrix[i] = rows[i];
        }
    }

    public Matrix()
    {
        matrix = new double[4][4];
    }

    public Matrix multiply(Matrix right)
    {
        Matrix resultMatrix = new Matrix();

        for (int row = 0; row < resultMatrix.matrix.length; row++) {
            double[] resultRow = new double[4];

            for (int i = 0; i < matrix.length; i++) {
                double[] b = new double[4];

                // forming a right vector for multiplication
                for (int j = 0; j < matrix[i].length; j++) {
                    b[j] = right.matrix[j][i];
                }

                resultRow[i] = getMultipliedValue(this.matrix[row], b);
            }

            resultMatrix.matrix[row] = resultRow;
        }

        return resultMatrix;
    }

    public Point3D multiplyByVertex(PointForMatrix point)
    {
        double[] rotatedPoint = new double[4];

        for (int i = 0; i < matrix.length; i++) {
            rotatedPoint[i] = getMultipliedValue(matrix[i], new double[] {
                point.x, point.y, point.z, point.f
            });
        }

        return new Point3D(rotatedPoint[0], rotatedPoint[1], rotatedPoint[2]);
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                builder.append(matrix[i][j]).append(" | ");
            }
            builder.append("\n");
        }
        return new String(builder);
    }

    private double getMultipliedValue(double[] a, double[] b)
    {
        double result = 0.0;

        for (int i = 0; i < a.length; i++) {
            result += a[i] * b[i];
        }

        return result;
    }
}