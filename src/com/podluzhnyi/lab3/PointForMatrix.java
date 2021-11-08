package com.podluzhnyi.lab3;

public class PointForMatrix
{
    public double x, y, z, f;

    public PointForMatrix(Point3D point3D)
    {
        x = point3D.getX();
        y = point3D.getY();
        z = point3D.getZ();
        f = 1.0;
    }

    @Override
    public String toString()
    {
        return "PointForMatrix{" +
            "x=" + x +
            ", y=" + y +
            ", z=" + z +
            ", f=" + f +
            '}';
    }
}