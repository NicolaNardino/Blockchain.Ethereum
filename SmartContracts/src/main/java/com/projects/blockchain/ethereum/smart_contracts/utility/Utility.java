package com.projects.blockchain.ethereum.smart_contracts.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;

import com.projects.blockchain.ethereum.smart_contracts.CoinManager;

public final class Utility {
	
	public static Properties getApplicationProperties(final String propertiesFileName) throws FileNotFoundException, IOException {
		final Properties p = new Properties();
		try(final InputStream inputStream = ClassLoader.getSystemResourceAsStream(propertiesFileName)) {
			p.load(inputStream);
			return p;
		}
	}
	
	public static CoinManager deployCoinManager(final Web3j web3j, final Credentials credentials) throws Exception {
		final long startTime = System.currentTimeMillis();
		final CoinManager contract = CoinManager.deploy(web3j, credentials, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT).send();
		final StringBuilder sb = new StringBuilder(); 
		sb.append("Deployed contract address: "+ contract.getContractAddress())
		  .append("\nOwner: "+contract.owner().send())
		  .append("\nTime taken: "+(System.currentTimeMillis() - startTime)+" ms.")
		  .append("\nDetails available at: https://rinkeby.etherscan.io/address/"+contract.getContractAddress());
		System.out.println(sb.toString());
		return contract;
	}
	
	public static CoinManager loadCoinManager(final Web3j web3j, final Credentials credentials, final String contractAddress) throws Exception {
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
