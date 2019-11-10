package unlam.crypto.app;

import static org.junit.jupiter.api.Assertions.*;

import java.security.Security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unlam.crypto.domain.Block;
import unlam.crypto.domain.BlockChain;
import unlam.crypto.domain.Transaction;
import unlam.crypto.domain.Wallet;
import unlam.crypto.validators.BlockChainValidator;

class MainTests {

	protected Wallet walletSender;
	protected Wallet walletReceiver;
	protected Wallet coinBase;
	protected BlockChain blockChain;

	@BeforeEach
	void initAll() {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		blockChain = BlockChain.INSTANCE;
		blockChain.clear();

		walletSender = new Wallet();
		walletReceiver = new Wallet();
		coinBase = new Wallet();
		
        Transaction transaction = coinBase.generateGenesisTransaction(walletSender.getPublicKey(), 100f, null);

        BlockChain.addToUTXO(transaction.getOutputs().get(0));

        System.out.println("Creating and Mining Genesis block... ");

        Block genesis = blockChain.createBlock();
        genesis.addTransaction(transaction);
        blockChain.mineBlockAt(0);       
  
	}

	@AfterEach
	void afterEach(){
		System.out.println("\nWalletA's balance is: " + walletSender.getBalance());
        System.out.println("WalletB's balance is: " + walletReceiver.getBalance());
	}
	
	@Test
	void testBlockNotMining() {
	    System.out.println("\nTest Block Not Mining");
        Block block1 = blockChain.createBlock();
        System.out.println("\nWalletSenders's balance is: " + walletSender.getBalance());
        System.out.println("\nWalletSenders is Attempting to send funds (40) to walletReceiver...");
        block1.addTransaction(walletSender.sendFunds(walletReceiver.getPublicKey(), 40f));
        //blockChain.mineBlockAt(1);
		assertFalse(BlockChainValidator.isBlockChainValid(blockChain));
	}
	
	@Test
	void testBalance() {
		System.out.println("\nTest Balance");
        Block block1 = blockChain.createBlock();
        System.out.println("\nWalletSenders's balance is: " + walletSender.getBalance());
        System.out.println("\nWalletSenders is Attempting to send funds (40) to walletReceiver...");
        block1.addTransaction(walletSender.sendFunds(walletReceiver.getPublicKey(), 40f));
        blockChain.mineBlockAt(1);
		assertEquals(60f ,walletSender.getBalance());
		assertEquals(40f ,walletReceiver.getBalance());
	}

	@Test
	void testBalanceAfterInvalidTx() {
		System.out.println("\nTest Balance After Invalid Tx");
        Block block1 = blockChain.createBlock();
        System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
        block1.addTransaction(walletSender.sendFunds(walletReceiver.getPublicKey(), 1000f));
        blockChain.mineBlockAt(1);
    	assertEquals(100f ,walletSender.getBalance());
		assertEquals(0f ,walletReceiver.getBalance());
	}
	

}
