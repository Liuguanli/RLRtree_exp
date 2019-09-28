package com.unimelb.cis.rlrtree;


import java.io.FileWriter;
import java.io.IOException;

public class FileRecoder {

    public static boolean isWrite = false;

    public static void write(String fileName, String content) {
        if (isWrite) {
            try (FileWriter writer = new FileWriter(fileName, true)) {
                writer.write(content);
//                writer.write("\r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
