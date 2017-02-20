package InstaPics;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class File_Handler extends AbstractAction {

    private boolean ableToExecute;
    private String webPageUrl;
    private String pathToLocation;
    private JFileChooser chooser;

    @Override
    public void actionPerformed(ActionEvent event){
        ableToExecute = true;
        chooser = new JFileChooser();

        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new FileNameExtensionFilter("*.txt","txt"));
        int result = chooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            BufferedReader reader = getURLSFromSelectedFile();
            pathToLocation = Buttons.setSaveDirectory(ableToExecute);
            parseAndSaveImagesFrom(reader, pathToLocation);
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
    private void parseAndSaveImagesFrom(BufferedReader localReader, String pathToLocation){
        while (ableToExecute) {
            webPageUrl = readNextLine(localReader);
            if (webPageUrl != null) {
                String stringHTML = Buttons.getPage_HTML(webPageUrl);
                String imageURL = Buttons.extractImageURL_Instagram(stringHTML);
                Buttons.saveImageFromURL(imageURL, "jpg", pathToLocation, ableToExecute);
            }
            else{
                ableToExecute=false;
            }
        }
    }
    private String readNextLine(BufferedReader localReader) {
        try {
            return localReader.readLine();
        }
        catch(IOException nextLineNotRead){
            ableToExecute = false;
            return null;
        }
    }
}
