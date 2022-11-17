package org.Game;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Helper {
    public static String lang = "English";

    // Reads file
    public String lineReader(String file, int lineNo) {
        // The line number
        String line = " ";
        try {
            line = Files.readAllLines(Paths.get(lang + file)).get(lineNo);
        } catch (IOException e) {
            System.out.println(e);
        }
        return line;
    }
}
