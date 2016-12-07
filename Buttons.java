//Copyright (C) 2016  Konrad Boniecki
//License information in file "Main"
package Sources;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Logger;

public class Buttons extends JPanel implements ActionListener {

    private JButton loadFromSafariButton;
    private JButton loadFromFileButton;

    private static JFileChooser chooser;
    private String webPageUrl;
    private String pathToLocation;
    private static boolean ableToExecute;

    public Buttons() {
        createButtons();
        addButtonsToPanel();
    }

    private void createButtons(){
        Dimension buttonSize = new Dimension(MainWindow.SCREEN_WIDTH/5,MainWindow.SCREEN_HEIGHT/5);

        loadFromFileButton = new JButton("Load from txt");
        loadFromFileButton.setPreferredSize(buttonSize);
        loadFromFileButton.addActionListener(this);

        if (isApplePC()){
            loadFromSafariButton = new JButton("Load from Safari tabs");
            loadFromSafariButton.setPreferredSize(buttonSize);
            loadFromSafariButton.addActionListener(this);
        }
    }

    private void addButtonsToPanel(){
        add(loadFromFileButton);
        if (isApplePC()) {
            add(loadFromSafariButton);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        ableToExecute = true;
        chooser = new JFileChooser();

        if(source == loadFromFileButton) {
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = chooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                BufferedReader reader = getURLSFromSelectedFile();
                pathToLocation = setSaveDirectory();
                parseAndSaveImagesFrom(reader);
            }
        }
        else if (source == loadFromSafariButton){
            Scanner urls  = new Scanner(getURLSFromSafari());
            pathToLocation = setSaveDirectory();
            parseAndSaveImagesFromSafari(urls);
        }
    }

    private boolean isApplePC(){
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

    private String setSaveDirectory(){
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

    private String getURLSFromSafari(){
        String script = AppleScript_copyToClipboardURL();
        runAppleScript(script);
        return copyFromClipboard();
    }
    private BufferedReader getURLSFromSelectedFile(){
        String pathToSelectedFile = chooser.getSelectedFile().getPath();
        try {
            FileReader file = new FileReader(pathToSelectedFile);
            BufferedReader localReader = new BufferedReader(file);
            return localReader;
        } catch (Exception loadingFileError) {
            System.out.println("File loading error");
            ableToExecute = false;
        }
        return null;
    }
    private void runAppleScript(String script) {
        Runtime runtime = Runtime.getRuntime();
        String[] args = {"osascript", "-e", script};
        try {
            Process process = runtime.exec(args);
        } catch (IOException e) {
            e.printStackTrace();
            ableToExecute = false;
        }
    }
    private String AppleScript_copyToClipboardURL(){
            String script = "tell application \"Safari\"\n" +
                    "\tset the_URLs to \"\"\n" +
                    "\trepeat with this_tab in tabs of window 1\n" +
                    "\t\tset the_URLs to the_URLs & URL of this_tab & return\n" +
                    "\tend repeat\n" +
                    "end tell\n" +
                    "set the clipboard to the_URLs\n";
            return script;
        }
    private String copyFromClipboard(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        String result = "NotNull";
        try {
            result = (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return result;
    }

    private void parseAndSaveImagesFrom(BufferedReader localReader){
            while (ableToExecute) {
                this.webPageUrl = readNextLine(localReader);

                String stringHTML = getPage_HTML(webPageUrl);
                String imageURL = extractImageURL_Instagram(stringHTML);
                saveImageFromURL(imageURL);
            }
        }
    private void parseAndSaveImagesFromSafari(Scanner localScanner){
        while (ableToExecute) {
            this.webPageUrl = readNextLine(localScanner);

            String stringHTML = getPage_HTML(webPageUrl);
            String imageURL = extractImageURL_Instagram(stringHTML);
            saveImageFromURL(imageURL);
        }
    }

    private String readNextLine(BufferedReader localReader) {
        try {
            return localReader.readLine();
        }
        catch(IOException nextLineNotRead){
            boolean ableToExecute = false;
            return null;
        }
    }
    private String readNextLine(Scanner localScanner){
        try {
            return localScanner.nextLine();
        }
        catch(Exception nextLineNotRead){
            boolean ableToExecute = false;
            return null;
        }
    }
    private String getPage_HTML(String webPageURL){
        try {
            Document inStringHTML = Jsoup.connect(webPageURL).get();
            String text = inStringHTML.toString();
            return text.trim();
        }
        catch(IllegalArgumentException exception1){
            System.out.println("bad url");
            return null;
        }
        catch(IOException exception2){
            System.out.println("Connection error");
            return null;
        }
    }
    private String extractImageURL_Instagram(String stringHTML){
        try {
            //extracting the link from html  og:image/LINK/og:description
            //<meta property="og:image" content="LINK_TO_IMG"> \n <meta property="og:description"
            //----------------^-------|---------|-----------|-------------------|^

            int indexStart = stringHTML.indexOf("og:image");
            int indexEnd = stringHTML.indexOf("og:description");
            String shortString = stringHTML.substring(indexStart, indexEnd);
            String[] stringArray = shortString.split("\"", 0);

            shortString = stringArray[2];
            return shortString;

            //TODO: if wrong url ex facebook, youtube etc... it may cause exception (shortstring)
        }
        catch (NullPointerException exception){
            return "This url is really bad :(";
        }
    }

    private void saveImageFromURL(String imageURL){

        try (InputStream in = new URL(imageURL).openStream()) {
            waitMs(750);
            Files.copy(in, Paths.get(pathToLocation + "/" + new Date().toString() + ".jpg"));
        } catch (Exception savingImageError) {
            System.out.println("File saving error");
            ableToExecute = false;
        }
    }

    private void waitMs(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}