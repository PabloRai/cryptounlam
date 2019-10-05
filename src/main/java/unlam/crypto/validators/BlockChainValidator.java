package unlam.crypto.validators;

import unlam.crypto.domain.Block;
import unlam.crypto.domain.BlockChain;
import unlam.crypto.utils.StringUtils;


public class BlockChainValidator {

    public static boolean isBlockChainValid(BlockChain blockChain) {

        String hashTarget = StringUtils.repeat("0", BlockChain.DIFFICULTY);

        for (int i = 1; i < blockChain.getBlockChainSize(); i++) {
            Block currentBlock = blockChain.getBlockChainElementAt(i);
            Block previousBlock = blockChain.getBlockChainElementAt(i-1);

            boolean currentBlockHashRemainsEqualsThanBefore = currentBlock.getHash().equals(currentBlock.calculateHash());

            if (!currentBlockHashRemainsEqualsThanBefore) {
                System.out.println("Current Hashes not equal");
                return false;
            }

            boolean previousBlockHashRemainsEqualsThanBefore = previousBlock.getHash().equals(currentBlock.getPreviousHash());

            if (!previousBlockHashRemainsEqualsThanBefore) {
                System.out.println("Previous Hashes not equal");
                return false;
            }

            if(!currentBlock.getHash().substring(0, BlockChain.DIFFICULTY).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }

        return true;

    }
}
