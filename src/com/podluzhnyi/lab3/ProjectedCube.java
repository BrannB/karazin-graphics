package com.podluzhnyi.lab3;

import java.awt.geom.Point2D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class ProjectedCube
{
    private ArrayList<Line2D> lines;

    public ProjectedCube(LinkedList<Point2D.Double> list)
    {
        this.lines = new ArrayList<>(15);

        lines.add(new Line2D.Double(list.get(0), list.get(1)));
        lines.add(new Line2D.Double(list.get(0), list.get(3)));
        lines.add(new Line2D.Double(list.get(0), list.get(5)));
        lines.add(new Line2D.Double(list.get(1), list.get(2)));

        lines.add(new Line2D.Double(list.get(3), list.get(2)));
        lines.add(new Line2D.Double(list.get(3), list.get(9)));
        lines.add(new Line2D.Double(list.get(2), list.get(8)));

        lines.add(new Line2D.Double(list.get(1), list.get(4)));
        lines.add(new Line2D.Double(list.get(5), list.get(9)));
        lines.add(new Line2D.Double(list.get(9), list.get(8)));
        lines.add(new Line2D.Double(list.get(7), list.get(8)));

        lines.add(new Line2D.Double(list.get(6), list.get(4)));
        lines.add(new Line2D.Double(list.get(4), list.get(7)));
        lines.add(new Line2D.Double(list.get(6), list.get(7)));
        lines.add(new Line2D.Double(list.get(5), list.get(6)));
    }

    public void paint(Graphics g)
    {
        for (Line2D line : lines) {
            ((Graphics2D) g).draw(line);
        }
    }
}