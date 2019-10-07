package unlam.crypto.domain;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum BlockChain {

    INSTANCE;

    public static final int DIFFICULTY = 2;
    private final List<Block> blockChain = new ArrayList<>();

    public void addBlock(Block block) {
        blockChain.add(block);
    }

    private static Map<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();


    public void createBlock(String data) {
        if (blockChain.isEmpty()) {
            createGenesisBlock(data);
            return;
        }

        int blockChainSize = getBlockChainSize();
        Block lastBlock = blockChain.get(blockChainSize - 1);

        Block block = new Block(data, DIFFICULTY, lastBlock.getHash());
        blockChain.add(block);
    }

    private void createGenesisBlock(String data) {
        Block block = new Block(data, DIFFICULTY, "0");

        blockChain.add(block);
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

    public List<Block> getBlockChain() {
        // Return a copy of the blockchain
        return new ArrayList<>(blockChain);
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
