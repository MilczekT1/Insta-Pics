/*
    "Insta Pics" downloads pictures from www.instagram.com
    Copyright (C) 2016  Konrad Boniecki
    Contact: Konrad_Boniecki@hotmail.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/
package InstaPics;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class Main {

    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainWindow window = new MainWindow();
            }
        });
    }

    public static Dimension getScreenSize(){
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        return screenSize;
    }
    protected static void waitMs(int milliseconds){
        //This method is only used due to the problem with naming 2 files with the same date-and-time in 1 sec
        try {
            Thread.sleep(milliseconds);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    protected static String copyFromClipboard(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        String result = "This String should contain clipboard inside\n";
        try {
            result = (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return result;
    }
}
