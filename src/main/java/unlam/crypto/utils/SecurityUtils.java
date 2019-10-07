package unlam.crypto.utils;

import java.security.*;

public class SecurityUtils {

    public static KeyPair generateKeyPair() {
        try {

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");

            return keyGen.generateKeyPair();


        } catch(Exception e) {
            throw new RuntimeException("Something happen while trying to obtain a KeyPar", e);
        }
    }

    public static byte[] applyEllipticCurveDigitalSignatureAlgorithm(PrivateKey privateKey, String input) {
        try {
            Signature signature = Signature.getInstance("ECDSA", "BC");
            signature.initSign(privateKey);
            byte[] strByte = input.getBytes();
            signature.update(strByte);
            return signature.sign();

        } catch (Exception e) {
            throw new RuntimeException("Something happen applying signature",e);
        }
    }

    public static boolean verifyEllipticCurveDigitalSignature(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
