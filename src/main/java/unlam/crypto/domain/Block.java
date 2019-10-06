package unlam.crypto.domain;

import unlam.crypto.utils.StringUtils;

import java.util.Date;

public class Block {
    private String hash;
    private String previousHash;
    private String data;
    private long timestamp;
    private int nonce;
    private int difficulty;


    Block(String data, int difficulty, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timestamp = new Date().getTime();
        this.difficulty = difficulty;
        this.hash = calculateHash();
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String calculateHash() {
        String input = previousHash + timestamp + nonce + data;

        return StringUtils.applySha256(input);
    }

    void mineBlock() {
        String target = StringUtils.repeat("0", difficulty);
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
    }
}
