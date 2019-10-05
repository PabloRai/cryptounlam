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

        blockChain.createBlock("Genesis block");
        blockChain.mineBlockAt(0);

        blockChain.createBlock("This is the second block");
        blockChain.mineBlockAt(1);

        blockChain.createBlock("This is the third block");
        blockChain.mineBlockAt(2);

        boolean valid = BlockChainValidator.isBlockChainValid(blockChain);

        if (!valid) {
            System.out.println("The blockchain is not valid!");
        }

        String jsonBlockChain = JsonUtils.getJSONString(blockChain.getBlockChain());
        System.out.println(jsonBlockChain);
    }
}
