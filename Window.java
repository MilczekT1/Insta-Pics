package Sources;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame implements KeyListener, MouseListener {

    private int Position_X,Position_Y;
    public static String wyborUsera;
    public static int indeks;

    public Window() {
        addMouseListener(this);

        setTitle("Download Instagram Pics");
        setSize(600,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel buttonPanel = new Buttons();
        add(buttonPanel);

        setLayout(new FlowLayout(FlowLayout.LEFT));
        pack();//dopasowuje rozmiar do paneli
        setVisible(true);
        buttonPanel.hasFocus();
        buttonPanel.grabFocus();
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
