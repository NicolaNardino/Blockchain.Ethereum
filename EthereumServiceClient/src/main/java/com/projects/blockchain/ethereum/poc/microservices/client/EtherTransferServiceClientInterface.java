package com.projects.blockchain.ethereum.poc.microservices.client;

import java.math.BigDecimal;

public interface EtherTransferServiceClientInterface {

	/**
	 * Gets info about the Geth client used to connect to Ethererum and the list of available accounts within the wallet.
	 * 
	 * @param url getInfo URL.	 
	 * 
	 * * */
	String getInfo(String url);

	/**
	 * Allows to transfer ETHERs or WEIs from the main wallet account to another.
	 * 
	 * @param url transfer URL.
	 * @param targetAccount Target account.
	 * @param transferAmount Transfer amount.
	 * @param transferUnit Transfer unit type, ETHER or WEI.
	 * 
	 * @return Transaction hash or null in case of errors.
	 * 
	 * */
	String transfer(String url, String targetAccount, BigDecimal transferAmount, String transferUnit);

}