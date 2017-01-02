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

public class Main {

    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainWindow window = new MainWindow();
            }
        });
    }

    protected static Dimension getScreenSize(){
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        return screenSize;
    }
}
