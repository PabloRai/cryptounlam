package unlam.crypto.domain;

import unlam.crypto.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {
    private String hash;
    private String previousHash;
    private String merkleRoot;
    private List<Transaction> transactions;
    private long timestamp;
    private int nonce;
    private int difficulty;


    Block(int difficulty, String previousHash) {
        this.previousHash = previousHash;
        this.timestamp = new Date().getTime();
        this.difficulty = difficulty;
        this.hash = calculateHash();
        this.transactions = new ArrayList<>();
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String calculateHash() {
        String input = previousHash + timestamp + nonce + merkleRoot;

        return StringUtils.applySha256(input);
    }

    void mineBlock() {
        merkleRoot = StringUtils.getMerkleRoot(transactions);
        String target = StringUtils.repeat("0", difficulty);
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
    }

    public void addTransaction(Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if(transaction == null) {
            return;
        }
        if((!previousHash.equals("0"))) {
            if((!transaction.processTransaction())) {
                System.out.println("Transaction failed to process. Discarded.");
                return;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }
}
