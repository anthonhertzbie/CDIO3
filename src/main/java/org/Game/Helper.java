package org.Game;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Helper {
    public static String lang;

    // Reads file
    public String lineReader(int lineNo) {
        // The line number
        String line = null;
        try {
            line = Files.readAllLines(Paths.get(lang)).get(lineNo);
        } catch (IOException e) {
            System.out.println(e);
        }
        return line;
    }
}
