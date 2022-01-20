package com.example.thesis_v1_0_0.classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class FileManager {

    public static byte[] getFileBytes(String filepath, ClassLoader classLoader){
        byte[] byteArray = null;
        try{
            File file = FileManager.getFileFromResource(filepath,classLoader);
            FileInputStream fl = new FileInputStream(file);
            byteArray = new byte[(int)file.length()];
            fl.read(byteArray);
            fl.close();
        }catch (Exception e){
            System.out.println(e);
        }
        return byteArray;
    }

    public static InputStream getFileFromResourceAsStream(String fileName, ClassLoader classLoader) {

        // The class loader that loaded the class
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    private static File getFileFromResource(String fileName, ClassLoader classLoader) throws URISyntaxException {
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return new File(resource.toURI());
        }
    }
}
