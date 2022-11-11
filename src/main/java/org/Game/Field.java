package org.Game;

import gui_fields.GUI_Field;
import gui_fields.GUI_Street;

import java.awt.*;
import java.util.Arrays;

public class Field {


    String[] fieldInformation = new String[6];
    GUI_Street field;
    Helper helper = new Helper();
    Color color;
    Color color2;



    public GUI_Street getField(String language, int lineNo) {
        fieldInformation = helper.lineReader(language, lineNo).split("/");
        // Stolen from https://stackoverflow.com/questions/2854043/converting-a-string-to-color-in-java, Erick Robertson && ZZ Coder
        try {
            java.lang.reflect.Field colorReflect = Class.forName("java.awt.Color").getField(fieldInformation[4]);
            java.lang.reflect.Field colorReflect2 = Class.forName("java.awt.Color").getField(fieldInformation[5]);
            color = (Color)colorReflect.get(null);
            color2 = (Color)colorReflect2.get(null);
        } catch (Exception e) {
            color = null; // Not defined
            color2 = null;
        }
        // Stealing stops

        System.out.println(fieldInformation[5]);
        System.out.println(fieldInformation[0] + " || "+ fieldInformation[1]+ " || "+ fieldInformation[2]+ " || "+ fieldInformation[3]+ " || "+ fieldInformation[4]+ " || "+ fieldInformation[5]);
        this.field = new GUI_Street(fieldInformation[0], fieldInformation[1], fieldInformation[2], fieldInformation[3], color, color2);
        return field;
    }
}
