package unlam.crypto.app;

import unlam.crypto.domain.Block;
import unlam.crypto.domain.BlockChain;
import unlam.crypto.utils.JsonUtils;
import unlam.crypto.validators.BlockChainValidator;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        BlockChain blockChain = BlockChain.INSTANCE;

        Block genesisBlock = Block.makeGenesisBlock("Genesis block");
        blockChain.addBlock(genesisBlock);
        blockChain.mineBlockAt(0);

        Block secondBlock = new Block("This is the second block",genesisBlock.getHash());

        blockChain.addBlock(secondBlock);
        blockChain.mineBlockAt(1);

        Block thirdBlock = new Block("This is the third block",secondBlock.getHash());

        blockChain.addBlock(thirdBlock);
        blockChain.mineBlockAt(2);

        boolean valid = BlockChainValidator.isBlockChainValid(blockChain);

        if (!valid) {
            System.out.println("The blockchain is not valid!");
        }

        String jsonBlockChain = JsonUtils.getJSONString(blockChain.getBlockChain());
        System.out.println(jsonBlockChain);
    }
}
