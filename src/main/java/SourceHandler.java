//Copyright (C) 2016  Konrad Boniecki
//License information in file "Main"

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedList;

public abstract class SourceHandler extends AbstractAction {

    protected boolean ableToExecute;
    protected String pathToLocation;
    protected String webPageUrl;
    protected JFileChooser chooser;
    protected LinkedList<String> filesNotSaved;

    protected String setSaveDirectory(boolean ableToExecute){
        chooser = new JFileChooser();
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
    protected String getPage_HTML(String webPageURL){

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
    protected String extractFileLink(String stringHTML){
        try {
            String shortString = null;
            int indexStart = 0,indexEnd = 0;

            if (stringHTML.contains("<meta property=\"og:video\"")){
                indexStart = stringHTML.indexOf("og:video");
                indexEnd = stringHTML.indexOf("og:video:type");
            }
            else if (stringHTML.contains("<meta property=\"og:image\"")) {
                indexStart = stringHTML.indexOf("og:image");
                indexEnd = stringHTML.indexOf("og:description");
            }
            else{
                //its not instagram link
                return "null";
            }
            shortString = stringHTML.substring(indexStart, indexEnd);
            //extracting the link from html  og:image/LINK/og:description
            //<meta property="og:image" content="LINK_TO_IMG"> \n <meta property="og:description"
            //----------------^-------|---------|-----------|-------------------|^---------------
            String[] stringArray = shortString.split("\"", 0);
            shortString = stringArray[2];
            return shortString;
        }
        catch (NullPointerException exception){
            return "This url is really bad :(";
        }
    }
    protected void saveImageFromURL(String imageURL, String extension, String pathToLocation, Boolean ableToExecute){
        try (InputStream in = new URL(imageURL).openStream()) {
            Main.waitMs(1001);
            Files.copy(in, Paths.get(pathToLocation + "/" + new Date().toString() + extension));
        } catch (Exception savingImageError) {
            try {
                filesNotSaved.add(imageURL);
            }
            catch(NullPointerException exc){
                System.out.println("nullptr exc after trying to add to error list");
            }
        }
    }
}
