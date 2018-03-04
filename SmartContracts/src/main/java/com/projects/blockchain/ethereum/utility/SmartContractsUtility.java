package com.projects.blockchain.ethereum.utility;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;

import com.projects.blockchain.ethereum.smart_contracts.CoinManager;

public final class SmartContractsUtility {
	
	public static final String CoinManagerAddress = "0xf4729c2807fd0f4431004146ecfc4a47578aaeea";
	
	public static CoinManager deployCoinManager(final Web3j web3j, final Credentials credentials, final String coinName) throws Exception {
		final long startTime = System.currentTimeMillis();
		final CoinManager contract = CoinManager.deploy(web3j, credentials, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT, coinName).send();
		final StringBuilder sb = new StringBuilder(); 
		sb.append("Deployed contract, address: "+ contract.getContractAddress())
		  .append("\nOwner: "+contract.owner().send())
		  .append("\nTime taken: "+(System.currentTimeMillis() - startTime)+" ms.")
		  .append("\nDetails available at: https://rinkeby.etherscan.io/address/"+contract.getContractAddress());
		System.out.println(sb.toString());
		return contract;
	}
	
	public static CoinManager loadCoinManager(final Web3j web3j, final Credentials credentials, final String contractAddress) {
		try {
			final long startTime = System.currentTimeMillis();
			final CoinManager contract = CoinManager.load(contractAddress, web3j, credentials, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);
			final StringBuilder sb = new StringBuilder(); 
			sb.append("\nLoaded contract, address: "+contractAddress)
			  .append("\nOwner: "+contract.owner().send())
			  .append("\nIs valid: "+contract.isValid())
			  .append("\nTime taken: "+(System.currentTimeMillis() - startTime)+" ms.");
			System.out.println(sb.toString());
			return contract;	
		}
		catch(final Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public static CoinManager reDeployCoinManager(final Web3j web3j, final Credentials credentials, final String contractAddress, final String coinName) throws Exception {
		loadCoinManager(web3j, credentials, contractAddress).kill();
		return deployCoinManager(web3j, credentials, coinName);
	}
}
