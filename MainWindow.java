//Copyright (C) 2016  Konrad Boniecki
//License information in file Main
package InstaPics;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import javax.swing.*;

public class MainWindow extends JFrame implements MouseListener {

    private int Mouse_X,Mouse_Y;
    private JPanel buttonPanel;
    private JLabel label;
    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 300;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    static {
        Dimension screenSize = Main.getScreenSize();
        SCREEN_HEIGHT = screenSize.height;
        SCREEN_WIDTH = screenSize.width;
    }

    public MainWindow(){
        setFrameProperties();

        buttonPanel = new Buttons();

        JPanel description = new JPanel();
        description.add(new JLabel("description"));

        this.setLayout(new FlowLayout());
        add(description);
        add(buttonPanel);
        pack();
        setVisible(true);
    }

    private void setFrameProperties() {
        setTitle("Instagram Pics by Konrad Boniecki");
        setSize(SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        setLocation(SCREEN_WIDTH/4, SCREEN_HEIGHT/4);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setMinimalizedIcon("Resources/logo.gif");
    }

    private void setMinimalizedIcon(String path){
        URL url = MainWindow.class.getResource(path);
        Image img = new ImageIcon(url).getImage();
        setIconImage(img);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Mouse_X = e.getX();
        Mouse_Y = e.getY();
    }
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
