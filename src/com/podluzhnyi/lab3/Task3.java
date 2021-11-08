package com.podluzhnyi.lab3;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.LinkedList;

public class Task3 extends JFrame 
{
    public Task3() 
    {
        JPanel panel = new JPanel();
        getContentPane().add(panel);
        setSize(980,560);
    }

    public void paint(Graphics g) 
    {
        super.paint(g);
        
        LinkedList<Point2D> list = new LinkedList<>();
        for (int i = 0; i < 50; i++) {
            list.add(new Point2D.Double(100, 100 + i));
        }

        for (int i = 0; i < list.size(); i++) {
            ((Graphics2D) g).draw((Shape) list.get(i));
        }

    }

    public static void main(String[] args) 
    {
        Task3 s = new Task3();
        s.setVisible(true);
    }