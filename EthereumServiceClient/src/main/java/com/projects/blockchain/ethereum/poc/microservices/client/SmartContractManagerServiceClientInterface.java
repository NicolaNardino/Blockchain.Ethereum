package com.projects.blockchain.ethereum.poc.microservices.client;

import java.math.BigInteger;

/**
 * Interface describing the common features between CoinManager and DepositManager smart contracts. 
 * 
 * */
public interface SmartContractManagerServiceClientInterface {

	/**
	 * Gets smart contract owner account.
	 * 
	 * @param url getOwner URL.
	 * 
	 * @return Owner account.
	 * */
	String getOwner(String url);

	/**
	 * Gets smart contract account balance.
	 * 
	 * @return Account balance.
	 * */
	BigInteger getBalance(String url);
	
	/**
	 * Gets the balance of an account within either CoinManager or DepositManager.
	 * 
	 * @param url getAccountBalance URL.
	 * @param account Account within CoinManager or DepositManager. 
	 * 
	 * @return Account balance.
	 * */
	BigInteger getAccountBalance(String url, String account);
}