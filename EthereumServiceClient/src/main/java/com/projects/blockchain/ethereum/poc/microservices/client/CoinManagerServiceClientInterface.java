package com.projects.blockchain.ethereum.poc.microservices.client;

import java.math.BigInteger;

import com.projects.blockchain.ethereum.restful.DepositData;

public interface CoinManagerServiceClientInterface extends SmartContractManagerServiceClientInterface {

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