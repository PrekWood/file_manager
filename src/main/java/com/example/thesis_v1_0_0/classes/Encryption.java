package com.example.thesis_v1_0_0.classes;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;

/**
 * the main idea of this class was from this article
 * https://www.baeldung.com/java-digital-signature
 */
public class Encryption {

    private final char[] password = "3=vyBL!YF9~#".toCharArray();

    public byte[] generateDigitalSignature(byte[] fileBytes) {

        // Hash the files using SHA-256
        byte[] fileHash = this.hash(fileBytes);

        // Encrypt the hash using the Private Key
        return this.encrypt(fileHash);
    }

    public boolean validateDigitalSignature(byte[] fileBytesReceived, byte[] encryptedMessageHash) {

        // Decrypt digital signature using the Public Key
        byte[] decryptedMessageHash = this.decrypt(encryptedMessageHash);

        // Hash once again the file received by the client
        byte[] receivedFileHashed = this.hash(fileBytesReceived);

        // If the hashes are the same that means that the validation is correct
        return Arrays.equals(decryptedMessageHash, receivedFileHashed);
    }

    public byte[] encrypt(byte[] hashedBytes) {
        byte[] digitalSignature = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, this.getPrivateKey());
            digitalSignature = cipher.doFinal(hashedBytes);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return digitalSignature;
    }

    public byte[] decrypt(byte[] encryptedMessageHash) {
        byte[] decryptedMessageHash = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, this.getPublicKey());
            decryptedMessageHash = cipher.doFinal(encryptedMessageHash);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return decryptedMessageHash;
    }

    public PrivateKey getPrivateKey() {
        PrivateKey privateKey = null;
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            InputStream privateKeyFileStream = FileManager.getFileFromResourceAsStream("static/certificates/sender_keystore.p12", getClass());
            keyStore.load(privateKeyFileStream, password);
            privateKey = (PrivateKey) keyStore.getKey("senderKeyPair", password);
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | IOException | CertificateException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public PublicKey getPublicKey() {
        PublicKey publicKey = null;
        try {
            InputStream publicKeyFileStream = FileManager.getFileFromResourceAsStream("static/certificates/sender_certificate.cer", getClass());
            CertificateFactory f = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) f.generateCertificate(publicKeyFileStream);
            publicKey = certificate.getPublicKey();
        } catch (CertificateException e) {
            System.out.println("catch");
            e.printStackTrace();
        }
        return publicKey;
    }

    public byte[] hash(byte[] fileBytes) {
        byte[] fileHash = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            fileHash = messageDigest.digest(fileBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return fileHash;
    }

    public static String byteArrayToHexString(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
