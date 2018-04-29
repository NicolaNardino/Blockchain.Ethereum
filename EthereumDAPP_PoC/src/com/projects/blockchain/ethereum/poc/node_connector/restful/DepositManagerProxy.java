package com.projects.blockchain.ethereum.poc.node_connector.restful;

import javax.ws.rs.core.Response;

import com.projects.blockchain.ethereum.restful.DepositData;

public interface DepositManagerProxy {

	/**
	 * Deposit some WEIs into DepositManager internal storage for a given account.
	 * 
	 * @param depositData targetAccount account the WEIs will be credited to, amount WEIs to be deposited.
	 * 
	 * @return Response.
	 *      
	 * */
	Response deposit(DepositData depositData);

	/**
	 * Get the balance of a given account from the DepositManager internal storage.
	 * 
	 * @param account Account.
	 * @return Response.
	 *      
	 * */
	Response getAccountBalance(String account);

	/**
	 * Get the balance of DepositManager. This it split amongst all its deposit accounts.
	 * 
	 * @return Response.
	 *      
	 * */
	Response getDepositManagerBalance();
}