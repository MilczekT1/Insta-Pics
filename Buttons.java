//Copyright (C) 2016  Konrad Boniecki
//License information in file "Main"
package Sources;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/* TODO:
    let user  create and find files on-the-go
*/
public class Buttons extends JPanel implements ActionListener {

    private JButton loadFromSafariButton;
    private JButton loadFromFileButton;

    private String webPageUrl;
    private static int fileAmount = 0;
    private static boolean ableToExecute;

    public Buttons() {
        loadFromFileButton = new JButton("Run");
        loadFromFileButton.addActionListener(this);
        setLayout(new FlowLayout());
        add(loadFromFileButton);

        if (System.getProperty("os.name").equals("Mac OS X")){
            loadFromSafariButton = new JButton("Load from Safari tabs");
            loadFromSafariButton.addActionListener(this);
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
            //*
            if (System.getProperty("os.name").equals("Mac OS X")){
                try {
                    file = new FileReader("/Users/Konrad/Desktop/insta.txt");
                    reader = new BufferedReader(file);
                }catch (Exception loadingFileError) {
                    System.out.println("File loading error");
                    ableToExecute = false;
                }
            } else {//TODO: different OS
                try {
                    file = new FileReader("path");
                    reader = new BufferedReader(file);
                }catch (Exception loadingFileError) {
                    System.out.println("File loading error");
                    ableToExecute = false;
                }
            }
            //*
            parseAndSaveImagesFrom(reader, ableToExecute);
        }
        else if (source == loadFromSafariButton){
            String script = copyToClipboardURL_AppleScript();
            runAppleScript(script);
            String copied = copyFromClipboard();
            Scanner urls  = new Scanner(copied);

            parseAndSaveImagesFromSafari(urls, ableToExecute);
        }
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
    private void parseAndSaveImagesFromSafari(Scanner localURLS, boolean localAbletoexecute){
        while (ableToExecute) {
            this.webPageUrl = readNextLine(localURLS);
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