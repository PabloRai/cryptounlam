package unlam.crypto.domain;

import unlam.crypto.utils.SecurityUtils;
import unlam.crypto.utils.StringUtils;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Transaction {

    private String transactionId;
    private PublicKey senderPublicKey;
    private PublicKey receiverPublicKey;
    private float value;
    private byte[] signature;

    private List<TransactionInput> inputs;
    private List<TransactionOutput> outputs;


    private static int SEQUENCE;

    Transaction(PublicKey senderPublicKey, PublicKey receiverPublicKey, float value, List<TransactionInput> inputs) {

        this.senderPublicKey = senderPublicKey;
        this.receiverPublicKey = receiverPublicKey;
        this.value = value;
        this.inputs = inputs != null ? new ArrayList<>(inputs) : null;
        this.outputs = new ArrayList<>();

    }


    private String generateTransactionId() {

        SEQUENCE++;
        String senderPublicKeyAsString = StringUtils.getStringFromKey(senderPublicKey);
        String receiverPublicKeyAsString = StringUtils.getStringFromKey(receiverPublicKey);

        String input = senderPublicKeyAsString + receiverPublicKeyAsString + value + SEQUENCE;

        return StringUtils.applySha256(input);
    }

    void generateSignature(PrivateKey privateKey) {
        String data = getDataFromTransaction();
        signature = SecurityUtils.applyEllipticCurveDigitalSignatureAlgorithm(privateKey, data);
    }

    public boolean verifySignature() {
        String data = getDataFromTransaction();
        return SecurityUtils.verifyEllipticCurveDigitalSignature(senderPublicKey, data, signature);
    }

    public String getTransactionId() {
        return transactionId;
    }

    private String getDataFromTransaction() {
        return StringUtils.getStringFromKey(senderPublicKey) + StringUtils.getStringFromKey(receiverPublicKey) + value;
    }

    boolean processTransaction() {

        if(!verifySignature()) {
            System.out.println("Transaction is no verified!");
            return false;
        }


        Map<String, TransactionOutput> UTXOs = BlockChain.getUTXOs();
        for (TransactionInput transactionInput : inputs) {
            String transactionOutputId = transactionInput.getTransactionOutputId();
            TransactionOutput UTXO = UTXOs.get(transactionOutputId);
            transactionInput.setUTXO(UTXO);
        }

        float leftOver = getInputsValue() - value;
        transactionId = generateTransactionId();

        TransactionOutput receiverTransactionOutput = new TransactionOutput(receiverPublicKey, value, transactionId);
        TransactionOutput senderTransactionOutput = new TransactionOutput(senderPublicKey, leftOver, transactionId);

        outputs.add(receiverTransactionOutput);
        outputs.add(senderTransactionOutput);

        for(TransactionOutput output : outputs) {
            BlockChain.addToUTXO(output);
        }

        for(TransactionInput transactionInput : inputs) {
            TransactionOutput transactionOutput = transactionInput.getUTXO();
            if(transactionOutput == null) {
                continue;
            }

            String transactionOutputId = transactionOutput.getId();

            BlockChain.removeFromUTXO(transactionOutputId);
        }

        return true;
    }

    public float getInputsValue() {
        float total = 0;
        for(TransactionInput i : inputs) {

            if(i.getUTXO() == null) {
                continue;
            }

            total += i.getTransactionOutputValue();
        }
        return total;
    }

    public float getOutputsValue() {
        float total = 0;
        for(TransactionOutput transactionOutput : outputs) {
            total += transactionOutput.getValue();
        }
        return total;
    }

    void setGenesisTransactionId() {
        this.transactionId = "0";
    }

    void addOutput(TransactionOutput transactionOutput) {
        outputs.add(transactionOutput);
    }

    public List<TransactionOutput> getOutputs() {
        return new ArrayList<>(outputs);
    }

    public List<TransactionInput> getInputs() {
        return new ArrayList<>(inputs);
    }

    public PublicKey getReceiverPublicKey() {
        return receiverPublicKey;
    }

    public PublicKey getSenderPublicKey() {
        return senderPublicKey;
    }
}
