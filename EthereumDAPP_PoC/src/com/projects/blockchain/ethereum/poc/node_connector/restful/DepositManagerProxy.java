package com.projects.blockchain.ethereum.poc.node_connector.restful;

import javax.ws.rs.core.Response;

public interface DepositManagerProxy {

	/**
	 * Deposits some WEIs into DepositManager internal storage, setting as sender account the one who instantiated CoinManager.
	 * 
	 * @param amount WEIs to be deposited.
	 * @return Response.
	 *      
	 * */
	Response deposit(long amount);

	/**
	 * Get the balance of a given account from the DepositManager internal storage.
	 * 
	 * @param account Account.
	 * @return Response.
	 *      
	 * */
	Response getAccountBalance(String account);
}