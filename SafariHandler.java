//Copyright (C) 2016  Konrad Boniecki
//License information in file "Main"
package InstaPics;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

public class SafariHandler extends SourceHandler {

    @Override
    public void actionPerformed(ActionEvent event){
        ableToExecute = true;
        chooser = new JFileChooser();
        pathToLocation = setSaveDirectory(ableToExecute);

        Scanner urls = new Scanner(getURLSFromSafari());

        LinkedList<String> links = new LinkedList<>(parseFileLinks(urls));

        String extension;
        for(String link: links){
            extension = link.endsWith(".mp4") ? ".mp4" : ".jpg";
            saveImageFromURL(link, extension, pathToLocation, ableToExecute);
        }
        urls.close();
    }

    private String getURLSFromSafari(){
        String copyToClipboardFromSafari = AppleScript_copyToClipboardURL();
        runAppleScript(copyToClipboardFromSafari);
        // Program execution can be faster than copying links via Applescript
        Main.waitMs(2000);
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

    private Collection<String> parseFileLinks(Scanner localScanner){
        LinkedList<String> fileLinks = new LinkedList<>();
        String stringHTML;
        String fileURL;
        while (ableToExecute) {
            webPageUrl = readNextLine(localScanner);
            if (!webPageUrl.equals("null")) {
                stringHTML = getPage_HTML(webPageUrl);
                fileURL = extractFileLink(stringHTML);
                fileLinks.add(fileURL);
            }
            else{
                ableToExecute = false;
            }
        }
        return fileLinks;
    }
    private String readNextLine(Scanner localScanner){
        try {
            if (localScanner.hasNextLine()) {
                return localScanner.nextLine();
            }
            else{
                return "null";
            }
        }
        catch(Exception nextLineNotRead){
            ableToExecute = false;
            return "null";
        }
    }
}
