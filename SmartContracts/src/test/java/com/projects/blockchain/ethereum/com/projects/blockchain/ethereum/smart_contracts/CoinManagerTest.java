package com.projects.blockchain.ethereum.com.projects.blockchain.ethereum.smart_contracts;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Properties;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import com.projects.blockchain.ethereum.smart_contracts.CoinManager;
import com.projects.blockchain.ethereum.smart_contracts.utility.Utility;

public final class CoinManagerTest {
	private static Web3j web3j; 
	private static CoinManager coinManager;
	private static final Random random = new Random();
	
	@BeforeClass
	public static void classSetup() throws Exception {
		final Properties properties = Utility.getApplicationProperties("smartContracts.properties");
		web3j = Web3j.build(new HttpService(properties.getProperty("nodeURL")));
		final Credentials credentials = WalletUtils.loadCredentials(properties.getProperty("accountPassword"), properties.getProperty("walletFilePath"));
		coinManager = Utility.loadCoinManager(web3j, credentials, Utility.CoinManagerAddress);
	}
	
	@Test
	@Ignore
	public void testMessage() throws Exception {
		final String testMessage = String.valueOf(random.nextInt(999));
		coinManager.setMessage(testMessage).send();
		assertEquals(testMessage, coinManager.getMessage().send());
	}
	
	@Test
	@Ignore
	public void testMint() throws Exception {
		final String testReceiver = String.valueOf(random.nextInt(9999));
		final BigInteger amount = BigInteger.valueOf(random.nextInt(999));
		final BigInteger testReceiverInitialBalance = coinManager.balances(testReceiver).send();
		coinManager.mint(testReceiver, amount).send();
		final BigInteger testReceiverFinalBalance = coinManager.balances(testReceiver).send();
		System.out.println("TestReceiver: "+testReceiver+", TestReceiverBalance: "+testReceiverFinalBalance+", RandomAmount credited: "+amount);
		assertEquals(amount.add(testReceiverInitialBalance), testReceiverFinalBalance);
		
	}
	
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
}
