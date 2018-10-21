package com.projects.blockchain.ethereum.poc.microservices.client;

import java.math.BigInteger;

import com.projects.blockchain.ethereum.restful.DepositData;

public interface CoinManagerServiceClientInterface {

	/**
	 * Gets Coin Manager owner account.
	 * 
	 * @param url getOwner URL.
	 * 
	 * @return Owner account.
	 * */
	String getOwner(String url);

	/**
	 * Gets the balance of an account within Coin Manager.
	 * 
	 * @param url getAccountBalance URL.
	 * @param account Account within Coin Manager. 
	 * 
	 * @return Account balance.
	 * */
	BigInteger getAccountBalance(String url, String account);

	/**
	 * It can be used for both raising and transferring funds within Coin Manager. 
	 * 
	 * @param url raiseFund or transferFund URL.
	 * 
	 * @return Transaction hash or null in case of errors.
	 * */
	String raiseOrTransferFund(String url, String targetAccount, BigInteger amount);

	/**
	 * Deposits funds into Deposit Manager through Coin Manager.
	 * 
	 * @param url deposit URL.
	 * @param depositData Target account and amount.
	 * 
	 * @return True if the deposit succeeded.
	 * */
	boolean deposit(String url, DepositData depositData);

}