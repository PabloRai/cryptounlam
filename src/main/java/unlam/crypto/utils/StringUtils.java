package unlam.crypto.utils;

import unlam.crypto.domain.Transaction;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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

    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }


    public static String getMerkleRoot(List<Transaction> transactions) {
        int count = transactions.size();
        List<String> transactionIds = new ArrayList<>();
        for(Transaction transaction : transactions) {
            transactionIds.add(transaction.getTransactionId());
        }
        List<String> treeLayer = transactionIds;
        while(count > 1) {
            treeLayer = new ArrayList<>();
            for(int i=1; i < transactionIds.size(); i++) {
                treeLayer.add(applySha256(transactionIds.get(i-1) + transactionIds.get(i)));
            }
            count = treeLayer.size();
            transactionIds = treeLayer;
        }
        return (treeLayer.size() == 1) ? treeLayer.get(0) : "";
    }
}
