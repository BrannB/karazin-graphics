package com.podluzhnyi.lab1;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.LinkedList;

public class UglySquare
{
    private LinkedList<Line2D> lines;

    public UglySquare(Line2D... lines)
    {
        this.lines = new LinkedList<Line2D>();
        this.lines.addAll(Arrays.asList(lines));
    }

    public UglySquare(LinkedList<Line2D> collection)
    {
        lines = collection;
    }

    public void paint(Graphics g)
    {
        for (Line2D line : lines) {
            ((Graphics2D) g).draw(line);
        }
    }

    public UglySquare parallelMove(double dx, double dy)
    {
        LinkedList<Line2D> newLines = new LinkedList<>();

        for (Line2D line : lines) {
            newLines.add(parallelMovement(line, dx, dy));
        }

        return new UglySquare(newLines);
    }

    public UglySquare moveOnAngle(Point2D constraint, double angle)
    {
        LinkedList<Line2D> newLines = new LinkedList<>();

        for (Line2D line : lines) {
            newLines.add(moveOnAngleLine(line, constraint, angle));
        }

        return new UglySquare(newLines);
    }

    private Line2D moveOnAngleLine(Line2D line, Point2D constraint, double angle)
    {
        Point2D point1 = line.getP1();
        Point2D point2 = line.getP2();
        double newX = constraint.getX() + (point1.getX() - constraint.getX()) * Math.cos(angle) - (point1.getY() - constraint.getY()) * Math.sin(angle);
        double newY = constraint.getY() + (point1.getY() - constraint.getY()) * Math.cos(angle) + (point1.getX() - constraint.getX()) * Math.sin(angle);
        double newX2 = constraint.getX() + (point2.getX() - constraint.getX()) * Math.cos(angle) - (point2.getY() - constraint.getY()) * Math.sin(angle);
        double newY2 = constraint.getY() + (point2.getY() - constraint.getY()) * Math.cos(angle) + (point2.getX() - constraint.getX()) * Math.sin(angle);

        return new Line2D.Double(newX, newY, newX2, newY2);
    }

    private Line2D parallelMovement(Line2D line, double dx, double dy)
    {
        Point2D point1 = line.getP1();
        Point2D point2 = line.getP2();
        double newX = point1.getX() + dx;
        double newY = point1.getY() + dy;
        double newX2 = point2.getX() + dx;
        double newY2 = point2.getY() + dy;

        return new Line2D.Double(newX, newY, newX2, newY2);
    }
}