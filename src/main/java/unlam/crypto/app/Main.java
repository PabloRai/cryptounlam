package unlam.crypto.app;

import unlam.crypto.domain.Block;
import unlam.crypto.domain.BlockChain;
import unlam.crypto.domain.Transaction;
import unlam.crypto.domain.Wallet;
import unlam.crypto.utils.JsonUtils;
import unlam.crypto.validators.BlockChainValidator;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        BlockChain blockChain = BlockChain.INSTANCE;

        Wallet walletSender = new Wallet();
        Wallet walletReceiver = new Wallet();
        Wallet coinBase = new Wallet();

        Transaction transaction = coinBase.generateGenesisTransaction(walletSender.getPublicKey(), 100f, null);

        BlockChain.addToUTXO(transaction.getOutputs().get(0));

        System.out.println("Creating and Mining Genesis block... ");

        Block genesis = blockChain.createBlock();
        genesis.addTransaction(transaction);
        blockChain.mineBlockAt(0);

        Block block1 = blockChain.createBlock();
        System.out.println("\nWalletSenders's balance is: " + walletSender.getBalance());
        System.out.println("\nWalletSenders is Attempting to send funds (40) to walletReceiver...");
        block1.addTransaction(walletSender.sendFunds(walletReceiver.getPublicKey(), 40f));
        blockChain.mineBlockAt(1);

        System.out.println("\nWalletA's balance is: " + walletSender.getBalance());
        System.out.println("WalletB's balance is: " + walletReceiver.getBalance());

        Block block2 = blockChain.createBlock();
        System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
        block2.addTransaction(walletSender.sendFunds(walletReceiver.getPublicKey(), 1000f));
        blockChain.mineBlockAt(2);
        System.out.println("\nWalletA's balance is: " + walletSender.getBalance());
        System.out.println("WalletB's balance is: " + walletReceiver.getBalance());

        Block block3 = blockChain.createBlock();
        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
        block3.addTransaction(walletReceiver.sendFunds( walletSender.getPublicKey(), 20));
        blockChain.mineBlockAt(3);
        System.out.println("\nWalletA's balance is: " + walletSender.getBalance());
        System.out.println("WalletB's balance is: " + walletReceiver.getBalance());

        boolean valid = BlockChainValidator.isBlockChainValid(blockChain);

        if (!valid) {
            System.out.println("The blockchain is not valid!");
        }


    }
}
