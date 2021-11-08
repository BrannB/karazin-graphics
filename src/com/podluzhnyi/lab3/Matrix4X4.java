package com.podluzhnyi.lab3;

public class Matrix4X4
{
    private double[][] matrix;

    public Matrix4X4(double[]... rows)
    {
        matrix = new double[4][];
        for (int i = 0; i < 4; i++) {
            matrix[i] = rows[i];
        }
    }

    public Matrix4X4() {
        matrix = new double[4][4];
    }

    public Matrix4X4 multiply(Matrix4X4 right)
    {
        Matrix4X4 resultMatrix = new Matrix4X4();

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
            rotatedPoint[i] = getMultipliedValue(matrix[i], new double[] { point.x, point.y, point.z, point.f });
        }

        return new Point3D(rotatedPoint[0], rotatedPoint[1], rotatedPoint[2]);
    }

    public Point3D multiplyAndNormalize(PointForMatrix point)
    {
        double[] updatedPoint = new double[4];
        double[] matrixColumn = new double[] { 0, 0, 0, 0 };

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < 4; j++) {
                matrixColumn[j] = matrix[j][i];
            }
            updatedPoint[i] = getMultipliedValue(new double[] { point.x, point.y, point.z, point.f}, matrixColumn );
        }

        double w = updatedPoint[3];
        double x = updatedPoint[0] / w;
        double y = updatedPoint[1] / w;
        double z = updatedPoint[2] / w;

        return new Point3D(x, y, z);
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