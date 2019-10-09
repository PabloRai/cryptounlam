package unlam.crypto.domain;

import unlam.crypto.utils.SecurityUtils;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wallet {

    private final PublicKey publicKey;
    private final PrivateKey privateKey;
    private final HashMap<String,TransactionOutput> UTXOs;

    public Wallet() {

        final KeyPair keyPair = SecurityUtils.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
        UTXOs = new HashMap<>();

    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public Transaction generateGenesisTransaction(PublicKey receiverPublicKey, float value, List<TransactionInput> inputs) {

        Transaction transaction = new Transaction(this.publicKey, receiverPublicKey, value, inputs);
        transaction.generateSignature(this.privateKey);
        transaction.setGenesisTransactionId();
        transaction.addOutput(new TransactionOutput(receiverPublicKey, value, transaction.getTransactionId())); //manually add the Transactions Output

        return transaction;
    }

    private Transaction generateTransaction(PublicKey receiverPublicKey, float value, List<TransactionInput> inputs) {

        Transaction transaction = new Transaction(this.publicKey, receiverPublicKey, value, inputs);
        transaction.generateSignature(this.privateKey);

        return transaction;
    }

    public float getBalance() {
        Map<String,TransactionOutput> UTXOs = BlockChain.getUTXOs();
        float balance = 0;
        for(Map.Entry<String, TransactionOutput> entry : UTXOs.entrySet()) {
            TransactionOutput transactionOutput = entry.getValue();
            if (transactionOutput.isMine(this.publicKey)) {
                this.UTXOs.put(transactionOutput.getId(), transactionOutput);
                balance += transactionOutput.getValue();
            }
        }
        return balance;
    }


    public Transaction sendFunds(PublicKey receiver, float value) {
        float balance = getBalance();
        if (balance < value) {
            System.out.println("Cannot process transaction with value: " + value);
            return null;
        }

        List<TransactionInput> inputs = new ArrayList<>();
        float totalToSend = 0;

        while(totalToSend < value) {
            for (Map.Entry<String, TransactionOutput> entry: UTXOs.entrySet()){
                TransactionOutput UTXO = entry.getValue();
                totalToSend += UTXO.getValue();
                inputs.add(new TransactionInput(UTXO.getId()));
            }
        }

        Transaction newTransaction = generateTransaction(receiver, value, inputs);

        for(TransactionInput input: inputs){
            UTXOs.remove(input.getTransactionOutputId());
        }

        return newTransaction;
    }

}
