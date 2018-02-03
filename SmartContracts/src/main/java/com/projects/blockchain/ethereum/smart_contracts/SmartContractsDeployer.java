package com.projects.blockchain.ethereum.smart_contracts;

import java.util.Properties;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;

import com.projects.blockchain.ethereum.smart_contracts.utility.Utility;

/**
 * Utility class to deploy, load, kill and test smart contracts.
 * */
public final class SmartContractsDeployer {

	public static void main(final String[] args) {
		try {
			final Properties properties = Utility.getApplicationProperties("smartContracts.properties");
			final Web3j web3j = Web3j.build(new HttpService(properties.getProperty("nodeURL")));
			final Credentials credentials = WalletUtils.loadCredentials(properties.getProperty("accountPassword"), properties.getProperty("walletFilePath"));
			//deployCoinManager(web3j, credentials);
			final CoinManager coinManager = loadCoinManager(web3j, credentials, "0xd54239c0cf4b4a8f289ddcb1179c97d8d40611c0");
			//coinManager.setMessage("Test Message").send();
			System.out.println("Retrieved message: "+coinManager.message().send());
			//final TransactionReceipt tr = coinManager.kill().send();
			//System.out.println(tr.getStatus()+"/ "+tr.getCumulativeGasUsed());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void deployCoinManager(final Web3j web3j, final Credentials credentials) throws Exception {
		final long startTime = System.currentTimeMillis();
		final CoinManager contract = CoinManager.deploy(web3j, credentials, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT).send();
		final StringBuilder sb = new StringBuilder(); 
		sb.append("Deployed contract address: "+ contract.getContractAddress())
		  .append("\nOwner: "+contract.owner().send())
		  .append("\nTime taken: "+(System.currentTimeMillis() - startTime)+" ms.")
		  .append("\nDetails available at: +https://rinkeby.etherscan.io/address/"+contract.getContractAddress());
		System.out.println(sb.toString());
	}

	private static CoinManager loadCoinManager(final Web3j web3j, final Credentials credentials, final String contractAddress) throws Exception {
		final long startTime = System.currentTimeMillis();
		final CoinManager contract = CoinManager.load(contractAddress, web3j, credentials, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);
		final StringBuilder sb = new StringBuilder(); 
		sb.append("\nOwner: "+contract.owner().send())
		  .append("\nIs valid: "+contract.isValid())
		  .append("\nTime taken: "+(System.currentTimeMillis() - startTime)+" ms.");
		System.out.println(sb.toString());
		return contract;
	}
	
}
