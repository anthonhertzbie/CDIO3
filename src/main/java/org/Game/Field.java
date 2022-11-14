package org.Game;
import gui_fields.GUI_Street;

import java.awt.*;

public class Field {


    String[] fieldInformation = new String[6];
    GUI_Street field;
    Helper helper = new Helper();
    Color color;

    int R1, G1, B1;


    public GUI_Street getField(String language, int lineNo) {
        fieldInformation = helper.lineReader(language, lineNo).split("/");
        // Stolen from https://stackoverflow.com/questions/2854043/converting-a-string-to-color-in-java, Erick Robertson && ZZ Coder
        try {
            java.lang.reflect.Field colorReflect = Class.forName("java.awt.Color").getField(fieldInformation[5]);
            color = (Color)colorReflect.get(null);
        } catch (Exception e) {
            color = null; // Not defined
        }
        // Stealing stops
        R1 = Integer.parseInt(fieldInformation[4].split(",")[0]);
        G1 = Integer.parseInt(fieldInformation[4].split(",")[1]);
        B1 = Integer.parseInt(fieldInformation[4].split(",")[2]);
        System.out.println(R1);
        System.out.println(G1);
        System.out.println(B1);


        System.out.println(fieldInformation[5]);
        System.out.println(fieldInformation[0] + " || "+ fieldInformation[1]+ " || "+ fieldInformation[2]+ " || "+ fieldInformation[3]+ " || "+ fieldInformation[4]+ " || "+ fieldInformation[5]);
        this.field = new GUI_Street(fieldInformation[0], fieldInformation[1], fieldInformation[2], fieldInformation[3], new Color(R1,G1,B1), color);
        this.field.setBorder(Color.BLACK, Color.white);
        return field;
    }
}
