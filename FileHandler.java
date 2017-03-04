//Copyright (C) 2016  Konrad Boniecki
//License information in file "Main"
package InstaPics;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

public class FileHandler extends SourceHandler {

    @Override
    public void actionPerformed(ActionEvent event){
        ableToExecute = true;
        chooser = new JFileChooser();

        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new FileNameExtensionFilter("*.txt","txt"));
        int result = chooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            BufferedReader reader = getURLSFromSelectedFile();
            pathToLocation = setSaveDirectory(ableToExecute);

            LinkedList<String> links = new LinkedList<>(parseFileLinks(reader));
            String extension;
            for (String link: links) {
                extension = link.endsWith(".mp4") ? ".mp4" : ".jpg";
                saveImageFromURL(link, extension, pathToLocation, ableToExecute);
            }
        }
        chooser.resetChoosableFileFilters();
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
    private Collection<String> parseFileLinks(BufferedReader localReader){
        LinkedList<String> links = new LinkedList<>();
        String stringHTML;
        String fileURL;
        while (ableToExecute) {
            webPageUrl = readNextLine(localReader);
            if (!webPageUrl.equals("null")) {
                stringHTML = getPage_HTML(webPageUrl);
                fileURL = extractFileLink(stringHTML);
                links.add(fileURL);
            }
            else{
                ableToExecute=false;
            }
        }
        return links;
    }
    private String readNextLine(BufferedReader localReader) {
        try {
            return localReader.readLine();
        }
        catch(IOException nextLineNotRead){
            ableToExecute = false;
            return "null";
        }
    }
}
