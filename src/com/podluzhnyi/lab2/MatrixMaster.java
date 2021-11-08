package com.podluzhnyi.lab1;

public class MatrixMaster
{
    public Point3D multiplyMxBy(Point3D point, double phi)
    {
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ();
        double cosPhi = Math.cos(phi);
        double sinPhi = Math.sin(phi);

        double newX = 1 * x + 0   *    y + 0     *      z;
        double newY = 0 * x + cosPhi * y + ((-sinPhi) * z);
        double newZ = 0 * x + sinPhi * y + cosPhi     * z;

        return new Point3D(newX, newY, newZ);
    }

    public Point3D multiplyMyBy(Point3D point, double phi)
    {
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ();
        double cosPhi = Math.cos(phi);
        double sinPhi = Math.sin(phi);

        double newX = cosPhi *    x + 0    *   y + sinPhi   *   z;
        double newY = 0 *         x + 1    *   y + 0        *   z;
        double newZ = (-sinPhi) * x + 0    *   y + cosPhi   *   z;

        return new Point3D(newX, newY, newZ);
    }

    public Point3D multiplyMzBy(Point3D point, double phi)
    {
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ();
        double cosPhi = Math.cos(phi);
        double sinPhi = Math.sin(phi);

        double newX = cosPhi *    x + (-sinPhi)  *   y + 0  *   z;
        double newY = sinPhi *    x + cosPhi     *   y + 0  *   z;
        double newZ = 0      *    x + 0          *   y + 1  *   z;

        return new Point3D(newX, newY, newZ);
    }

    public Point3D shift(Point3D point, Point3D vector)
    {
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ();

        double newX = x + vector.getX();
        double newY = y + vector.getY();
        double newZ = z + vector.getZ();

        return new Point3D(newX, newY, newZ);
    }
}