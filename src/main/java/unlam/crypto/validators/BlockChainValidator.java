package unlam.crypto.validators;

import unlam.crypto.domain.*;
import unlam.crypto.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;


public class BlockChainValidator {

    public static boolean isBlockChainValid(BlockChain blockChain) {

        String hashTarget = StringUtils.repeat("0", BlockChain.DIFFICULTY);
        Map<String, TransactionOutput> tempUTXOs = new HashMap<>(); //a temporary working list of unspent transactions at a given block state.
        Transaction genesis = blockChain.getBlockChainElementAt(0).getTransactions().get(0);
        tempUTXOs.put(genesis.getOutputs().get(0).getId(), genesis.getOutputs().get(0));

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

            for(Transaction transaction : currentBlock.getTransactions()) {

                if (!transaction.verifySignature()) {
                    System.out.println("Transaction with id: " + transaction.getTransactionId() + " is not verified");
                    return false;
                }

                if (transaction.getInputsValue() != transaction.getOutputsValue()) {
                    System.out.println("Inputs are note equal to outputs on Transaction with id: " + transaction.getTransactionId());
                    return false;
                }

                for(TransactionInput input: transaction.getInputs()) {
                    TransactionOutput tempOutput = tempUTXOs.get(input.getTransactionOutputId());

                    if(tempOutput == null) {
                        System.out.println("Referenced input on Transaction " + transaction.getTransactionId() + " is Missing");
                        return false;
                    }

                    if(input.getUTXO().getValue() != tempOutput.getValue()) {
                        System.out.println("Referenced input Transaction " + transaction.getTransactionId() + " value is Invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.getTransactionOutputId());
                }

                for(TransactionOutput output: transaction.getOutputs()) {
                    tempUTXOs.put(output.getId(), output);
                }

                if( transaction.getOutputs().get(0).getReceiverPublicKey() != transaction.getReceiverPublicKey()) {
                    System.out.println("Transaction " + transaction.getTransactionId() + " output reciepient is not who it should be");
                    return false;
                }

                if( transaction.getOutputs().get(1).getReceiverPublicKey() != transaction.getSenderPublicKey()) {
                    System.out.println("Transaction " + transaction.getTransactionId() + " output 'change' is not sender.");
                    return false;
                }

            }

        }

        return true;

    }
}
