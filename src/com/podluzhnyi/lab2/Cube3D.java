package com.podluzhnyi.lab2;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.LinkedList;

public class Cube3D
{
    private LinkedList<Point3D> points;
    private MatrixMaster master;
    private Matrix rotationMatrix;

    public Cube3D(LinkedList<Point3D> list)
    {
        this.points = list;
        master = new MatrixMaster();
        this.rotationMatrix = new Matrix();
    }

    public Cube3D(Point3D... points)
    {
        this.points = new LinkedList<Point3D>();
        this.points.addAll(Arrays.asList(points));
        master = new MatrixMaster();
    }

    public Cube3D rotate(double angleX, double angleY, double angleZ)
    {
        Cube3D rotatedX = this.rotateX(angleX);
        Cube3D rotatedY = rotatedX.rotateY(angleY);
        return rotatedY.rotateZ(angleZ);
    }

    public Cube3D shift(Point3D vector)
    {
        LinkedList<Point3D> updatedList = new LinkedList<>();

        for (Point3D point : points) {
            updatedList.add(master.shift(point, vector));
        }

        return new Cube3D(updatedList);
    }

    public LinkedList<Point2D.Double> project()
    {
        LinkedList<Point2D.Double> list = new LinkedList<>();

        for (Point3D point3D : this.points) {
            list.add(projectPoint(point3D));
        }

        return  list;
    }

    public Cube3D rotateAboutArbitraryLine(Point3D p, Point3D pDash, double phi)
    {
        rotationMatrix = rotateAboutArbitraryAxis(p, pDash, phi);
        LinkedList<Point3D> updatedList = new LinkedList<>();

        for (Point3D point : points) {
            updatedList.add(rotationMatrix.multiplyByVertex(new PointForMatrix(point)));
        }

        return new Cube3D(updatedList);
    }

    private Matrix rotateAboutArbitraryAxis(Point3D p, Point3D pDash, double phi)
    {
        double x = p.getX();
        double y = p.getY();
        double z = p.getZ();

        double a = pDash.getX();
        double b = pDash.getY();
        double c = pDash.getZ();

        double l = Math.sqrt(a * a + b * b + c * c);
        double v = Math.sqrt(b * b + c * c);

        // 1 step, rotation matrix
        Matrix T = new Matrix(
            new double[] { 1,   0,  0, 0 },
            new double[] { 0,   1,  0, 0 },
            new double[] { 0,   0,  1, 0 },
            new double[] { -x, -y, -z, 1 }
        );

        // 2 step, rotation about x-axis by thetaX
        Matrix Rx = new Matrix(
            new double[] { 1,      0,   0, 0 },
            new double[] { 0,    c/v, b/v, 0 },
            new double[] { 0, -(b/v), c/v, 0 },
            new double[] { 0,      0,   0, 1 }
        );

        // 3 step, rotation about y-axis by thetaY in clock-wise direction
        Matrix Ry = new Matrix(
            new double[] { v/l,  0, -(a/l),  0 },
            new double[] {   0,  1,      0,  0 },
            new double[] { a/l,  0,    v/l,  0 },
            new double[] {   0,  0,      0,  1 }
        );

        // 4 rotation about a given angle (phi)
        Matrix Rz = new Matrix(
            new double[] {  Math.cos(phi),  Math.sin(phi), -(a/l),  0 },
            new double[] { -Math.sin(phi),  Math.cos(phi),      0,  0 },
            new double[] {              0,              0,      1,  0 },
            new double[] {              0,              0,      0,  1 }
        );

        // 5 Ry^(-1)
        Matrix RyMinus = new Matrix(
            new double[] {    v/l,  0,  a/l,  0 },
            new double[] {      0,  1,    0,  0 },
            new double[] { -(a/l),  0,  v/l,  0 },
            new double[] {      0,  0,    0,  1 }
        );

        // 6 Rx^(-1)
        Matrix RxMinus = new Matrix(
            new double[] { 1,    0,       0,  0 },
            new double[] { 0,  c/v,  -(b/v),  0 },
            new double[] { 0,  b/v,     c/v,  0 },
            new double[] { 0,    0,       0,  1 }
        );

        // 7 T^(-1)
        Matrix TMinus = new Matrix(
            new double[] { 1,  0,  0,  0 },
            new double[] { 0,  1,  0,  0 },
            new double[] { 0,  0,  1,  0 },
            new double[] { x,  y,  z,  1 }
        );

        Matrix finalRotationMatrix = T.multiply(Rx);
        finalRotationMatrix = finalRotationMatrix.multiply(Ry);
        finalRotationMatrix = finalRotationMatrix.multiply(Rz);
        finalRotationMatrix = finalRotationMatrix.multiply(RyMinus);
        finalRotationMatrix = finalRotationMatrix.multiply(RxMinus);
        finalRotationMatrix = finalRotationMatrix.multiply(TMinus);

        return finalRotationMatrix;
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();

        for (Point3D point : points) {
            result.append(point).append("\n");
        }

        return new String(result);
    }

    private Point2D.Double projectPoint(Point3D point3D)
    {
        double x = point3D.getX();
        double y = point3D.getY();
        double z = point3D.getZ();

        return new Point2D.Double(x, y);
    }

    private Cube3D rotateX(double phi)
    {
        LinkedList<Point3D> updatedList = new LinkedList<>();

        for (Point3D point : points) {
            updatedList.add(master.multiplyMxBy(point, phi));
        }

        return new Cube3D(updatedList);
    }

    private Cube3D rotateY(double phi)
    {
        LinkedList<Point3D> updatedList = new LinkedList<>();

        for (Point3D point : points) {
            updatedList.add(master.multiplyMyBy(point, phi));
        }

        return new Cube3D(updatedList);
    }

    private Cube3D rotateZ(double phi)
    {
        LinkedList<Point3D> updatedList = new LinkedList<>();

        for (Point3D point : points) {
            updatedList.add(master.multiplyMzBy(point, phi));
        }

        return new Cube3D(updatedList);
    }
}