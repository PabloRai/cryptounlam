package unlam.crypto.domain;

import unlam.crypto.utils.StringUtils;

import java.security.PublicKey;

public class TransactionOutput {


    private String id;
    private PublicKey receiverPublicKey;
    private float value;

    TransactionOutput(PublicKey receiverPublicKey, float value, String parentTransactionId) {

        this.receiverPublicKey = receiverPublicKey;
        this.value = value;

        String receiverPublicKeyAsString = StringUtils.getStringFromKey(receiverPublicKey);
        String input = receiverPublicKeyAsString + value + parentTransactionId;
        this.id = StringUtils.applySha256(input);

    }

    boolean isMine(PublicKey publicKey) {
        return receiverPublicKey == publicKey;
    }

    public float getValue() {
        return value;
    }

    public String getId() {
        return id;
    }

    public PublicKey getReceiverPublicKey() {
        return receiverPublicKey;
    }
}
