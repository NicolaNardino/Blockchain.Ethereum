package com.projects.blockchain.ethereum.com.projects.blockchain.ethereum.smart_contracts;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Properties;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import com.projects.blockchain.ethereum.smart_contracts.CoinManager;
import com.projects.blockchain.ethereum.smart_contracts.DepositManager;
import com.projects.blockchain.ethereum.utility.SmartContractName;
import com.projects.blockchain.ethereum.utility.SmartContractsUtility;
import com.projects.blockchain.ethereum.utility.Utility;
import com.projects.blockchain.ethereum.utility.Web3jContainer;

/**
 * Test cases around the functionalities of CoinManager and DepositManager.
 * 
 * */
public final class CoinManagerTest {
	private static Web3j web3j;
	private static Credentials credentials;
	private static CoinManager coinManager;
	private static DepositManager depositManager;
	private static final Random random = new Random();
	
	@BeforeClass
	public static void classSetup() throws Exception {
		final Properties properties = Utility.getApplicationProperties("smartContracts.properties");
		final Web3jContainer web3jContainer = Utility.buildWeb3jContainer(properties.getProperty("nodeURL"), properties.getProperty("accountPassword"), properties.getProperty("walletFilePath"));
		web3j = web3jContainer.getWeb3j();
		credentials = web3jContainer.getCredentials();
		coinManager = (CoinManager)SmartContractsUtility.loadSmartContract(web3j, web3jContainer.getCredentials(), SmartContractName.CoinManager);
		depositManager = (DepositManager)SmartContractsUtility.loadSmartContract(web3j, web3jContainer.getCredentials(), SmartContractName.DepositManager);
	}
	
	/**
	 * Basic test to check CoinManager message store functionality. 
	 * */
	@Test
	@Ignore
	public void testMessage() throws Exception {
		final String testMessage = String.valueOf(random.nextInt(999));
		coinManager.setMessage(testMessage).send();
		assertEquals(testMessage, coinManager.getMessage().send());
	}
	
	/**
	 * Mints some MyCoins and checks they actually show up in the test receiver balance. Mind that, the test receiver must by definition 
	 * be the contract owner.  
	 * */
	@Test
	public void testMint() throws Exception {
		final String testReceiver = "0x99fedc28c33a8d00f7f0602baca0d24c3a17d9f6";
		final BigInteger amount = BigInteger.valueOf(random.nextInt(999));
		final BigInteger testReceiverInitialBalance = coinManager.balances(testReceiver).send();
		coinManager.mint(testReceiver, amount).send();
		final BigInteger testReceiverFinalBalance = coinManager.balances(testReceiver).send();
		System.out.println("TestReceiver: "+testReceiver+", TestReceiverBalance: "+testReceiverFinalBalance+", RandomAmount credited: "+amount);
		assertEquals(amount.add(testReceiverInitialBalance), testReceiverFinalBalance);
		
	}
	
	/**
	 * Sends some MyCoins from a given sender to a receiver account.  
	 * */
	@Test
	public void testSend() throws Exception {
		final String senderAccount = "0x99fedc28c33a8d00f7f0602baca0d24c3a17d9f6";
		final String receiverAccount = "0x9142A699d088be61C993Ace813829D3D25DeAc2d";
		final BigInteger receiverAmount = BigInteger.valueOf(random.nextInt(10) + 1);
		coinManager.mint(senderAccount, BigInteger.valueOf(random.nextInt(10) + 10)).send();		
		final BigInteger senderBalance =  coinManager.balances(senderAccount).send();
		final BigInteger receiverBalance =  coinManager.balances(receiverAccount).send();
		System.out.println("SenderBalance: "+senderBalance+", ReceiverBalance: "+receiverBalance+", TransferAmount: "+receiverAmount);
		try {coinManager.send(receiverAccount, receiverAmount).send();} catch(Exception e) {}
		assertEquals(senderBalance.subtract(receiverAmount), coinManager.balances(senderAccount).send());
		assertEquals(receiverBalance.add(receiverAmount), coinManager.balances(receiverAccount).send());
	}
	
	/**
	 * Sends WEIs to CoinManager by invoking its payable fallback function. 
	 * */
	@Test
	public void testSendWeisToCoinManager() throws Exception {
		final BigInteger initialBalance = coinManager.getBalance().send();
		System.out.println("Initial balance: "+initialBalance+" WEIs.");
		final BigInteger transferAmount = BigInteger.valueOf(random.nextInt(10) + 1);
		Utility.ethTransferExplicitTransaction(web3j, credentials, SmartContractName.CoinManager.getAddress(), transferAmount, 10, 60000);
		final BigInteger finalBalance = coinManager.getBalance().send();
		assertEquals(initialBalance.add(transferAmount), finalBalance);
		System.out.println("Final balance: "+finalBalance+" WEIs.");
	}
	
	/**
	 * Sends WEIs to DepositManager passing by CoinManager. At the end of the transactions, no WEIS must be credited to CoinManager. 
	 * */
	@Test
	public void testSendWeisToDepositManagerByCoinManager() throws Exception {
		final BigInteger depositManagerInitialBalance = depositManager.getBalance().send();
		final BigInteger coinManagerInitialBalance = coinManager.getBalance().send();
		System.out.println("Deposit Manager Initial Balance: "+depositManagerInitialBalance+" WEIs, Coin Manager Initial Balance: "+coinManagerInitialBalance+" WEIs.");
		final BigInteger depositAmount = BigInteger.valueOf(random.nextInt(10) + 1);
		System.out.println("Deposit Amount: "+depositAmount);
		coinManager.sendToDepositManager(depositAmount).send();
		final BigInteger depositManagerFinalBalance = depositManager.getBalance().send();
		final BigInteger coinManagerFinalBalance = coinManager.getBalance().send();
		System.out.println("Deposit Manager Final Balance: "+depositManagerFinalBalance+" WEIs, Coin Manager Final Balance: "+coinManagerFinalBalance+" WEIs.");
		assertEquals(depositManagerInitialBalance.add(depositAmount), depositManagerFinalBalance);
		assertEquals(coinManagerInitialBalance, coinManagerFinalBalance);
	}
}