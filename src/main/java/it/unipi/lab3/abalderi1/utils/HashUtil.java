package it.unipi.lab3.abalderi1.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe di utilità per l'hashing delle stringhe usando l'algoritmo SHA-256.
 */
public class HashUtil {

    /**
     * Effettua l'hashing di una stringa utilizzando l'algoritmo SHA-256.
     *
     * @param text La stringa da cifrare.
     * @return La stringa cifrata in formato esadecimale.
     */
    public static String hashWithSHA256(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash string", e);
        }
    }

    /**
     * Converte un array di byte nella sua rappresentazione esadecimale.
     *
     * @param bytes L'array di byte da convertire.
     * @return Una rappresentazione stringa dell'array di byte in esadecimale.
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
