package com.projects.blockchain.ethereum.com.projects.blockchain.ethereum.smart_contracts;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Properties;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import com.projects.blockchain.ethereum.smart_contracts.CoinManager;
import com.projects.blockchain.ethereum.smart_contracts.utility.Utility;

public final class CoinManagerTest {
	private static CoinManager coinManager;
	private static final Random random = new Random();
	private static final String contractAddress = "0xc2016d9b5e071ef4bd1142977bad4c07501acb28";
	
	@BeforeClass
	public static void classSetup() throws Exception {
		final Properties properties = Utility.getApplicationProperties("smartContracts.properties");
		final Web3j web3j = Web3j.build(new HttpService(properties.getProperty("nodeURL")));
		final Credentials credentials = WalletUtils.loadCredentials(properties.getProperty("accountPassword"), properties.getProperty("walletFilePath"));
		coinManager = Utility.loadCoinManager(web3j, credentials, contractAddress);
	}
	
	@Test
	public void testMessage() throws Exception {
		final String testMessage = String.valueOf(random.nextInt(999));
		coinManager.setMessage(testMessage).send();
		assertEquals(testMessage, coinManager.getMessage().send());
	}
	
	@Test
	public void testMint() throws Exception {
		final String testReceiver = String.valueOf(random.nextInt(999));
		final BigInteger amount = BigInteger.valueOf(random.nextInt(999));
		final BigInteger testReceiverBalance = coinManager.balances(testReceiver).send();
		coinManager.mint(testReceiver, amount).send();
		assertEquals(amount.add(testReceiverBalance), coinManager.balances(testReceiver).send());
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
		coinManager.send(receiverAccount, receiverAmount).send();
		assertEquals(senderBalance.subtract(receiverAmount), coinManager.balances(senderAccount).send());
		assertEquals(receiverBalance.add(receiverAmount), coinManager.balances(receiverAccount).send());
	}
}
