package com.example.thesis_v1_0_0.controllers;

import com.example.thesis_v1_0_0.classes.Encryption;
import com.example.thesis_v1_0_0.classes.FileManager;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class DownloadController {

    @GetMapping("/download/{filepath}")
    public HashMap<String, Object> downloadFile(Model model, @PathVariable("filepath") String filepath) {

        // Read file bytes
        byte[] fileBytes = FileManager.getFileBytes("E:\\Quack\\ptyxiakh\\project\\src\\main\\resources\\static\\files\\"+filepath);

        // Generate digital signature
        byte[] digitalSignature = (new Encryption()).generateDigitalSignature(fileBytes);

        // Transform bytes to hex
        String digitalSignatureHex = Encryption.byteArrayToHexString(digitalSignature);

        HashMap<String, Object> map = new HashMap<>();
        map.put("file",fileBytes);
        map.put("digitalSignature", digitalSignatureHex);

        return map;
    }

}
