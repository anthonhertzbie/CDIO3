package org.Game;
import gui_fields.GUI_Street;

import java.awt.*;

public class Field {


    String field;
    Helper helper = new Helper();

    public String getField(String File, int lineNo) {
        field = helper.lineReader(File, lineNo);
        return field;
    }
}
