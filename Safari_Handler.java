package InstaPics;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Scanner;

public class Safari_Handler extends AbstractAction {

    private boolean ableToExecute;
    private String webPageUrl;
    private String pathToLocation;
    private JFileChooser chooser;

    @Override
    public void actionPerformed(ActionEvent event){
        ableToExecute = true;
        chooser = new JFileChooser();

        Scanner urls = new Scanner(getURLSFromSafari());
        pathToLocation = Buttons.setSaveDirectory(ableToExecute);
        parseAndSaveImagesFromSafari(urls);

    }

    private String getURLSFromSafari(){
        String copyToClipboardFromSafari = AppleScript_copyToClipboardURL();
        runAppleScript(copyToClipboardFromSafari);
        return Main.copyFromClipboard();
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

    private void parseAndSaveImagesFromSafari(Scanner localScanner){
        //TODO: detect extension (mp4)
        while (ableToExecute) {
            webPageUrl = readNextLine(localScanner);
            if (webPageUrl !=null) {
                String stringHTML = Buttons.getPage_HTML(webPageUrl);
                String imageURL = Buttons.extractImageURL_Instagram(stringHTML);
                Buttons.saveImageFromURL(imageURL, "jpg", pathToLocation, ableToExecute);
            }
            else{
                ableToExecute = false;
                //System.out.println("End of saving image from safari");
            }
        }
    }
    private String readNextLine(Scanner localScanner){
        try {
            return localScanner.nextLine();
        }
        catch(Exception nextLineNotRead){
            ableToExecute = false;
            return null;
        }
    }
}
