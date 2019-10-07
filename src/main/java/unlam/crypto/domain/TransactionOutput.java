package unlam.crypto.domain;

import unlam.crypto.utils.StringUtils;

import java.security.PublicKey;

public class TransactionOutput {


    private String id;
    private PublicKey receiverPublicKey;
    private float value;
    private String parentTransactionId;

    public TransactionOutput(PublicKey receiverPublicKey, float value, String parentTransactionId) {

        this.receiverPublicKey = receiverPublicKey;
        this.value = value;
        this.parentTransactionId = parentTransactionId;

        String receiverPublicKeyAsString = StringUtils.getStringFromKey(receiverPublicKey);
        String input = receiverPublicKeyAsString + value + parentTransactionId;
        this.id = StringUtils.applySha256(input);

    }

    public boolean isMine(PublicKey publicKey) {
        return receiverPublicKey == publicKey;
    }

    public float getValue() {
        return value;
    }

    public String getId() {
        return id;
    }
}
