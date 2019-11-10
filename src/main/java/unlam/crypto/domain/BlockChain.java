package unlam.crypto.domain;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum BlockChain {

    INSTANCE;

    public static final int DIFFICULTY = 2;
    private final List<Block> blockChain = new ArrayList<>();



    private static Map<String,TransactionOutput> UTXOs = new HashMap<>();
    
    // just for test purposes
    public void clear() {
    	blockChain.clear();
    	UTXOs.clear();
    }


    public Block createBlock() {
        if (blockChain.isEmpty()) {
            return createGenesisBlock();

        }

        int blockChainSize = getBlockChainSize();
        Block lastBlock = blockChain.get(blockChainSize - 1);

        Block block = new Block(DIFFICULTY, lastBlock.getHash());
        blockChain.add(block);
        return block;
    }

    private Block createGenesisBlock() {
        Block block = new Block(DIFFICULTY, "0");

        blockChain.add(block);
        return block;
    }

    public Block getBlockChainElementAt(int index) {
        if (index < 0 || index >= blockChain.size()) {
            throw new IllegalArgumentException("Index is either a negative number or greater than blockchain size");
        }

        return blockChain.get(index);
    }

    public int getBlockChainSize() {
        return blockChain.size();
    }

    public void mineBlockAt(int index) {
        Block block = getBlockChainElementAt(index);

        block.mineBlock();

        System.out.println("Block mined with hash: " + block.getHash());
    }

    public static Map<String, TransactionOutput> getUTXOs() {
        return new HashMap<>(UTXOs);
    }

    public static void addToUTXO(TransactionOutput transactionToBeAdded) {
        UTXOs.put(transactionToBeAdded.getId(), transactionToBeAdded);
    }

    public static void removeFromUTXO(String id) {
        UTXOs.remove(id);
    }

}
