//Copyright (C) 2016  Konrad Boniecki
//License information in file Main
package Sources;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainWindow extends JFrame implements KeyListener, MouseListener {

    private int Position_X,Position_Y;
    private JPanel buttonPanel;
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;

    public MainWindow() {
        setFrameProperties();

        buttonPanel = new Buttons();

        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(buttonPanel);
        pack();
        setVisible(true);
        buttonPanel.grabFocus();
    }

    private void setFrameProperties() {
        setTitle("Instagram Pics");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Position_X = e.getX();
        Position_Y = e.getY();
    }
    @Override
    public void keyPressed(KeyEvent evt) {}
    @Override
    public void keyReleased(KeyEvent evt) {}
    @Override
    public void keyTyped(KeyEvent evt) {}
    @Override
    public void mouseEntered(MouseEvent e) {
        //System.out.println("mouseEntered");
    }
    @Override
    public void mouseExited(MouseEvent e) {
        //System.out.println("mouseExited");
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        //System.out.println("mouseReleased");
    }
    @Override
    public void mousePressed(MouseEvent e) {
        //System.out.println("mousePressed");
    }

}
