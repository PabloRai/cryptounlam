package unlam.crypto.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class StringUtils {

    //Applies Sha256 to a string and returns the result.
    public static String applySha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException("Something happen applying SHA-256", e);
        }
    }

    public static String repeat(String stringToRepeat, int count) {
        return new String(new char[count]).replace("\0", stringToRepeat);
    }
}
