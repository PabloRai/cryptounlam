package unlam.crypto.domain;


import java.util.ArrayList;
import java.util.List;

public enum BlockChain {

    INSTANCE;

    public static final int DIFFICULTY = 2;
    private final List<Block> blockChain = new ArrayList<>();

    public void addBlock(Block block) {
        blockChain.add(block);
    }

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

}
