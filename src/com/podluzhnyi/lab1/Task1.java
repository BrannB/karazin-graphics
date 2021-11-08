package com.podluzhnyi.lab1;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

public class Task1 extends JFrame
{
    public Task1()
    {
        JPanel panel = new JPanel();
        getContentPane().add(panel);
        setSize(720,480);
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        LinkedList<Line2D> lines = new LinkedList<Line2D>();

        Point2D constraint = new Point2D.Double(570, 300);

        lines.add(new Line2D.Float(250, 250, 300, 300));
        lines.add(new Line2D.Float(300, 300,300, 350));
        lines.add(new Line2D.Float(300, 350, 200, 350));
        lines.add(new Line2D.Float(200, 250, 200, 350));
        lines.add(new Line2D.Float(200, 250, 250, 250));

        UglySquare square = new UglySquare(lines);
        square.paint(g);

        UglySquare parallelMoved = square.parallelMove(70, -210);
        parallelMoved.paint(g);

        UglySquare movedOnAngle = square.moveOnAngle(constraint, Math.toRadians(21));
        movedOnAngle.paint(g);
    }

    public static void main(String []args)
    {
        Task1 s = new Task1();
        s.setVisible(true);
    }
}