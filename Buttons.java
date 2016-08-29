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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Buttons extends JPanel implements ActionListener {

    private JButton loadURLButton;

    private String webPageUrl;
    private static int fileAmount = 0;
    private static boolean ableToExecute;

    public Buttons() {
        loadURLButton = new JButton("Run");
        loadURLButton.addActionListener(this);
        setLayout(new FlowLayout());
        add(loadURLButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if(source == loadURLButton) {
            FileReader file;
            BufferedReader reader = null;
            //*
            try {
                if (System.getProperty("os.name").equals("Mac OS X")){
                    file = new FileReader("/Users/<username>/Desktop/insta.txt");
                    reader = new BufferedReader(file);
                } else {//TODO: different os
                    file = new FileReader("/Users/<username>/Desktop/insta.txt");
                    reader = new BufferedReader(file);
                }
                ableToExecute = true;
            }catch (Exception loadingFileError) {
                System.out.println("File loading error");
                ableToExecute = false;
            }
            //*

            while (ableToExecute) {
                this.webPageUrl = readNextLine(reader);
                this.fileAmount++;

                String stringHTML = getPage_HTML(webPageUrl);
                String imageURL = extractImageURL_Instagram(stringHTML);
                saveImagefromURL(imageURL);
            }
            this.fileAmount = 0;
        }
    }

    private String copyFromClipboard(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        String result = null;
        try {
            result = (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return result;
    }
    private String getPage_HTML(String webPageURL){
        try {
            Document inStringHTML = Jsoup.connect(webPageUrl).get();
            String text = inStringHTML.toString();
            text.trim();
            return text;
        }
        catch(IOException exception){
            System.out.println("Connection error");
            //ableToExecute = false;
            return "badlink";
        }
    }
    private String extractImageURL_Instagram(String stringHTML){
        int indexStart = stringHTML.indexOf("og:image");
        int indexEnd = stringHTML.indexOf("og:description");
        String shortString = stringHTML.substring(indexStart, indexEnd);

        //extracting the link from "dirty" string  og:image/LINK/og:description
        String[] stringArray = shortString.split("\"", 0);
        shortString = stringArray[2];

        return shortString;
    }
    private String readNextLine(BufferedReader localReader) {
        try {
            String url = localReader.readLine();
            return url;
        }
        catch(IOException nextLineNotRead){
            System.out.println("Reading next line error");
            boolean ableToExecute = false;
            return "155034";
        }
    }
    private void saveImagefromURL(String imageURL){
        try (InputStream in = new URL(imageURL).openStream()) {
            Files.copy(in, Paths.get("/Users/Konrad/Desktop/" + fileAmount + ".jpg"));
        } catch (Exception savingImageError) {
            System.out.println("File saving error");
            //this.ableToExecute = false;
        }
    }
}