//Copyright (C) 2016  Konrad Boniecki
//License information in file "Main"
package InstaPics;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

public class Buttons extends JPanel implements ActionListener {

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

        Action usingTxt = new File_Handler();
        loadFromFileButton = new JButton(usingTxt);
        loadFromFileButton.setIcon(new ImageIcon(getClass().getResource("Resources//Txt_Logo.jpg")));
        loadFromFileButton.setPreferredSize(buttonSize);

        if (isAppleOS()){
            Action usingSafari = new Safari_Handler();
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

    @Override
    public void actionPerformed(ActionEvent event) {
        //Object source = event.getSource();
        ;
    }


    protected static String setSaveDirectory(boolean ableToExecute){
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return chooser.getCurrentDirectory().toString();
        }
        else if (result == JFileChooser.CANCEL_OPTION){
            ableToExecute = false;
        }
        return null;
    }
    protected static String getPage_HTML(String webPageURL){
        try {
            Document inStringHTML = Jsoup.connect(webPageURL).get();
            String text = inStringHTML.toString();
            return text.trim();
        }
        catch(IllegalArgumentException exception1){
            System.out.println("Exception: Bad URL");
            return null;
        }
        catch(IOException connectionException){
            System.out.println("Connection error");
            // TODO: create new file with remained urls
            return null;
        }
    }
    protected static String extractImageURL_Instagram(String stringHTML){
        try {
            int indexStart = stringHTML.indexOf("og:image");
            int indexEnd = stringHTML.indexOf("og:description");
            String shortString = stringHTML.substring(indexStart, indexEnd);
            //extracting the link from html  og:image/LINK/og:description
            //<meta property="og:image" content="LINK_TO_IMG"> \n <meta property="og:description"
            //----------------^-------|---------|-----------|-------------------|^---------------
            String[] stringArray = shortString.split("\"", 0);

            shortString = stringArray[2];
            //TODO: if wrong url ex facebook, youtube etc... it may cause exception (shortstring)
            return shortString;
        }
        catch (NullPointerException exception){
            return "This url is really bad :(";
        }
    }
    protected static void saveImageFromURL(String imageURL, String extension, String pathToLocation, Boolean ableToExecute){
        try (InputStream in = new URL(imageURL).openStream()) {
            Main.waitMs(501);
            Files.copy(in, Paths.get(pathToLocation + "/" + new Date().toString() + "." + extension));
        } catch (Exception savingImageError) {
            System.out.println("File saving error");
            ableToExecute = false;
        }
    }
}