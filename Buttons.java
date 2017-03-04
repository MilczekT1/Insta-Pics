//Copyright (C) 2016  Konrad Boniecki
//License information in file "Main"
package InstaPics;

import javax.swing.*;
import java.awt.*;

public final class Buttons extends JPanel {

    private JButton loadFromSafariButton;
    private JButton loadFromFileButton;

    private JFileChooser chooser;
    private String webPageUrl;
    private String pathToLocation;
    private boolean ableToExecute;

    public Buttons() {
        createButtons();
        addButtonsToPanel();
        this.chooser = new JFileChooser();
    }

    private void createButtons(){
        Dimension buttonSize = new Dimension(243,243);

        Action usingTxt = new FileHandler();
        loadFromFileButton = new JButton(usingTxt);
        loadFromFileButton.setIcon(new ImageIcon(getClass().getResource("Resources//Txt_Logo.jpg")));
        loadFromFileButton.setPreferredSize(buttonSize);

        if (isAppleOS()){
            Action usingSafari = new SafariHandler();
            loadFromSafariButton = new JButton(usingSafari);
            loadFromSafariButton.setIcon(new ImageIcon(getClass().getResource("Resources//SafariLogo.png")));
            loadFromSafariButton.setPreferredSize(buttonSize);
        }
    }

    private void addButtonsToPanel(){
        add(loadFromFileButton);
        if (isAppleOS()) {
            add(loadFromSafariButton);
        }
    }
    private boolean isAppleOS(){
        if (System.getProperty("os.name").equals("Mac OS X")){
            return true;
        }
        else if (System.getProperty("os.name").equals("macOS")){
            return true;
        }
        else {
            return false;
        }
    }
}