package unlam.crypto.domain;

import unlam.crypto.utils.StringUtils;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Block {
    private String hash;
    private String previousHash;
    private String data;
    private long timeStamp;
    private int nonce;

    private static boolean GENESIS_BLOCK_ALREADY_CREATED = false;


    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String calculateHash() {
        String input = previousHash + timeStamp + nonce + data;

        return StringUtils.applySha256(input);
    }

    public static Block makeGenesisBlock(String data) {
        if (GENESIS_BLOCK_ALREADY_CREATED) {
            throw new RuntimeException("Genesis block was already created");
        }
        GENESIS_BLOCK_ALREADY_CREATED = true;
        return new Block(data, "0");
    }

    void mineBlock(int difficulty) {
        String target = StringUtils.repeat("0", difficulty);
        AtomicInteger attempts = new AtomicInteger();
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce ++;
            attempts.getAndIncrement();
            hash = calculateHash();
        }

        System.out.println("Calculated hash after: " + attempts + " attempts");
    }
}
