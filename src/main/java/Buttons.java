//Copyright (C) 2016  Konrad Boniecki
//License information in file "Main"

import javax.swing.*;
import java.awt.*;

public final class Buttons extends JPanel {

    private JButton loadFromSafariButton;
    private JButton loadFromFileButton;

    public Buttons() {
        createButtons();
        addButtonsToPanel();
    }

    private void createButtons(){
        Dimension buttonSize = new Dimension(243,243);

        Action usingTxt = new FileHandler();
        loadFromFileButton = new JButton(usingTxt);
        loadFromFileButton.setIcon(new ImageIcon(getClass().getResource("/Txt_Logo.jpg")));
        loadFromFileButton.setPreferredSize(buttonSize);

        if (isAppleOS()){
            Action usingSafari = new SafariHandler();
            loadFromSafariButton = new JButton(usingSafari);
            loadFromSafariButton.setIcon(new ImageIcon(getClass().getResource("/SafariLogo.png")));
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