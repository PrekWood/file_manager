package com.example.thesis_v1_0_0.classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class FileManager {

    public static byte[] getFileBytes(String filepath){
        byte[] byteArray = null;
        try{
            File file = new File(filepath);
            FileInputStream fl = new FileInputStream(file);
            byteArray = new byte[(int)file.length()];
            fl.read(byteArray);
            fl.close();
        }catch (Exception e){
            System.out.println(e);
        }
        return byteArray;
    }

    public static InputStream getFileFromResourceAsStream(String fileName, Class<?> classObject) {

        // The class loader that loaded the class
        ClassLoader classLoader = classObject.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }
}
