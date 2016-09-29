//Copyright (C) 2016  Konrad Boniecki
//License information in file "Main"
package Sources;

import javafx.stage.DirectoryChooser;
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
import java.util.Scanner;

/* TODO:
    let user  create and find and save files on-the-go
*/
public class Buttons extends JPanel implements ActionListener {

    private JButton loadFromSafariButton;
    private JButton loadFromFileButton;

    private static JFileChooser chooser;

    private String webPageUrl;
    private static int fileAmount = 0;
    private static boolean ableToExecute;

    public Buttons() {
        createButtons();
        addButtonsToPanel();
    }

    void createButtons(){
        loadFromFileButton = new JButton("Run");
        loadFromFileButton.addActionListener(this);

        if (System.getProperty("os.name").equals("Mac OS X")){
            loadFromSafariButton = new JButton("Load from Safari tabs");
            loadFromSafariButton.addActionListener(this);
        }
    }

    void addButtonsToPanel(){
        setLayout(new FlowLayout());
        add(loadFromFileButton);
        if (System.getProperty("os.name").equals("Mac OS X")) {
            add(loadFromSafariButton);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        fileAmount = 0;
        ableToExecute = true;

        if(source == loadFromFileButton) {
            FileReader file;
            BufferedReader reader = null;
            chooser = new JFileChooser();
            int result = chooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                String pathToSelectedFile = chooser.getSelectedFile().getPath();
                try {
                    file = new FileReader(pathToSelectedFile);
                    reader = new BufferedReader(file);
                } catch (Exception loadingFileError) {
                    System.out.println("File loading error");
                    ableToExecute = false;
                }
            }
            parseAndSaveImagesFrom(reader, ableToExecute);
        }
        else if (source == loadFromSafariButton){
            Scanner urls  = new Scanner(getURLSFromSafari());
            parseAndSaveImagesFromSafari(urls, ableToExecute);
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

    private String getURLSFromSafari(){
        String script = copyToClipboardURL_AppleScript();
        runAppleScript(script);
        String copied = copyFromClipboard();
        return copied;
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
    private String copyToClipboardURL_AppleScript(){
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
        String result = null;
        try {
            result = (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return result;
    }

    private void parseAndSaveImagesFrom(BufferedReader localReader, boolean localAbletoexecute){
            while (ableToExecute) {
                this.webPageUrl = readNextLine(localReader);
                fileAmount++;

                String stringHTML = getPage_HTML(webPageUrl);
                String imageURL = extractImageURL_Instagram(stringHTML);
                saveImageFromURL(imageURL);
            }
        }
    private void parseAndSaveImagesFromSafari(Scanner localScanner, boolean localAbletoexecute){
        while (ableToExecute) {
            this.webPageUrl = readNextLine(localScanner);
            fileAmount++;

            String stringHTML = getPage_HTML(webPageUrl);
            String imageURL = extractImageURL_Instagram(stringHTML);
            saveImageFromURL(imageURL);
        }
    }

    private String getPage_HTML(String webPageURL){
        try {
            Document inStringHTML = Jsoup.connect(webPageUrl).get();
            String text = inStringHTML.toString();
            text = text.trim();
            return text;
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
            int indexStart = stringHTML.indexOf("og:image");
            int indexEnd = stringHTML.indexOf("og:description");
            String shortString = stringHTML.substring(indexStart, indexEnd);
            //extracting the link from "dirty" string  og:image/LINK/og:description
            String[] stringArray = shortString.split("\"", 0);
            shortString = stringArray[2];
            return shortString;

            //TODO: if wrong url ex facebook, youtube etc... it may cause exception (shortstring)
        }
        catch (NullPointerException exception){
            return null;
        }
    }
    private String readNextLine(BufferedReader localReader) {
        try {
            return localReader.readLine();
        }
        catch(IOException nextLineNotRead){
            System.out.println("Reading next line error");
            boolean ableToExecute = false;
            return null;
        }
    }
    private String readNextLine(Scanner localScanner){
        try {
            return localScanner.nextLine();
        }
        catch(Exception nextLineNotRead){
            System.out.println("Reading next line error");
            boolean ableToExecute = false;
            return null;
        }
    }
    private void saveImageFromURL(String imageURL){
        try (InputStream in = new URL(imageURL).openStream()) {
            Files.copy(in, Paths.get("/Users/Konrad/Desktop/" + fileAmount + ".jpg"));
        } catch (Exception savingImageError) {
            System.out.println("File saving error");
            ableToExecute = false;
        }
    }
}