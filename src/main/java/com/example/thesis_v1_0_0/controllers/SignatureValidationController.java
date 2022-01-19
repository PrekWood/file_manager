package com.example.thesis_v1_0_0.controllers;

import com.example.thesis_v1_0_0.classes.Encryption;
import com.example.thesis_v1_0_0.classes.FileManager;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;

@RestController
public class SignatureValidationController {

    @PostMapping("/validate/{filepath}")
    public HashMap<String, Object> validateDigitalSignature(
            Model model,
            @PathVariable("filepath") String filepath,
            @RequestParam String digitalSignatureHex,
            @RequestParam String fileReceivedHex)
    {
        // Decode hex to bytes
        byte[] digitalSignatureBytes = Encryption.hexStringToByteArray(digitalSignatureHex);
        byte[] fileReceivedBytes = Encryption.hexStringToByteArray(fileReceivedHex);

        // Singature length validation
        if(digitalSignatureBytes.length != 256 ){
            HashMap<String, Object> map = new HashMap<>();
            map.put("success",false);
            return map;
        }

        // Generate digital signature
        boolean validationResults = (new Encryption()).validateDigitalSignature(fileReceivedBytes, digitalSignatureBytes);

        HashMap<String, Object> map = new HashMap<>();
        map.put("success",validationResults);

        return map;
    }

}
