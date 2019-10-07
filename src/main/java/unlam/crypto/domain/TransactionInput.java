package unlam.crypto.domain;

public class TransactionInput {

    private String transactionOutputId;

    // UTXO = Unspent transaction output
    private TransactionOutput UTXO;

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    public void setUTXO(TransactionOutput UTXO) {
        this.UTXO = UTXO;
    }

    public TransactionOutput getUTXO() {
        return UTXO;
    }

    public String getTransactionOutputId() {
        return transactionOutputId;
    }

    public float getTransactionOutputValue() {
        return UTXO.getValue();
    }
}
