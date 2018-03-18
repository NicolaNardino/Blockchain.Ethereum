package com.projects.blockchain.ethereum.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.utils.Convert.Unit;
import org.web3j.utils.Numeric;

public final class Utility {
	
	public static Properties getApplicationProperties(final String propertiesFileName) throws FileNotFoundException, IOException {
		final Properties p = new Properties();
		try(final InputStream inputStream = ClassLoader.getSystemResourceAsStream(propertiesFileName)) {
			p.load(inputStream);
			return p;
		}
	}
	public static void shutdownExecutorService(final ExecutorService es, long timeout, TimeUnit timeUnit) {
		es.shutdown();
		try {
			if (!es.awaitTermination(timeout, timeUnit))
				es.shutdownNow();
		} catch (final InterruptedException e) {
			System.err.println("Unable to awaitTermination while shutting down the executor service.");
		}
		System.out.println("Terminated ExecutorService "+es.toString());
	}
	
	/**
	 * It establishes the connection to an Ethereum node and to a given wallet account.
	 * */
	public static Web3jContainer buildWeb3jContainer(final String nodeURL, final String accountPassword, final String walletFilePath) {
    	final Web3j web3j = Web3j.build(new HttpService(nodeURL));
		try {
			final Credentials credentials = WalletUtils.loadCredentials(accountPassword, walletFilePath);
			return new Web3jContainer(web3j, credentials);	
		}
    	catch (final Exception e) {
    		throw new RuntimeException(e);
    	}
    }
	
	/**
	 * It transfers Ethers from the Wallet identified by {@link Credentials} to a target account. Furthermore, it decides how long to wait
	 * for a transaction receipt and the polling interval.
	 * It uses the <code>Transfer<code>, which wraps all the machinery of signing the transaction and getting the receipt.
	 * */
	public static TransactionReceipt ethTransferImplicitTransaction(final Web3j web3j, final Credentials credentials, final String targetAccount, 
			final BigDecimal amount, final int trReceiptAttempts, final int trReceiptSleepTime, final Unit unit) throws InterruptedException, TransactionException, Exception {
		return new Transfer(web3j, new RawTransactionManager(web3j,credentials, trReceiptAttempts, trReceiptSleepTime))
				.sendFunds(targetAccount, amount, unit).send();
	}
	
	/**
	 * Functionally similar to {@link #ethTransferExplicitTransaction(Web3j, Credentials, String, BigDecimal, int, int)} with the difference that
	 * the transaction is explicitly built.
	 * */
	public static TransactionReceipt ethTransferExplicitTransaction(final Web3j web3j, final Credentials credentials, final String targetAccount, 
			final BigInteger amount, final int trReceiptAttempts, final int trReceiptSleepTime) throws IOException, InterruptedException, ExecutionException, TransactionException {
		final EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
		final EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(targetAccount, DefaultBlockParameterName.PENDING).send();
		final BigInteger nonce = ethGetTransactionCount.getTransactionCount();
		final RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(nonce, ethGasPrice.getGasPrice(), Transfer.GAS_LIMIT, 
				targetAccount, amount);
		final EthSendTransaction tr = web3j.ethSendRawTransaction(Numeric.toHexString(TransactionEncoder.signMessage(rawTransaction, credentials))).send();
		final PollingTransactionReceiptProcessor trp = new PollingTransactionReceiptProcessor(web3j, trReceiptAttempts, trReceiptSleepTime);
		return trp.waitForTransactionReceipt(tr.getTransactionHash());
	}
	
	/**
	 * Calls a servlet and prints its outcome.
	 * */
	public static String servletCall(final String baseURL, final String parameters) {
		HttpURLConnection con = null;
		try {
			final StringBuilder response = new StringBuilder();
			try (final BufferedReader responseStream = new BufferedReader(new InputStreamReader(new URL(baseURL + "?" + parameters).openStream()))) {
				String line;
				while ((line = responseStream.readLine()) != null) 
					response.append(line).append('\r');
			}
			return response.toString();
		} catch (final Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			if (con != null)
				con.disconnect();
		}
	}
}
