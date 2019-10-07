package unlam.crypto.domain;

import unlam.crypto.utils.SecurityUtils;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

public class Wallet {

    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public Wallet() {

        final KeyPair keyPair = SecurityUtils.generateKeyPair();

        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public Transaction generateTransaction(PublicKey receiverPublicKey, float value, List<TransactionInput> inputs) {

        Transaction transaction = new Transaction(this.publicKey, receiverPublicKey, value, inputs);
        transaction.generateSignature(this.privateKey);

        return transaction;
    }
}
