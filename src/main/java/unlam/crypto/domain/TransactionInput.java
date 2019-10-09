package unlam.crypto.domain;

public class TransactionInput {

    private String transactionOutputId;

    // UTXO = Unspent transaction output
    private TransactionOutput UTXO;

    TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    void setUTXO(TransactionOutput UTXO) {
        this.UTXO = UTXO;
    }

    public TransactionOutput getUTXO() {
        return UTXO;
    }

    public String getTransactionOutputId() {
        return transactionOutputId;
    }

    float getTransactionOutputValue() {
        return UTXO.getValue();
    }
}
