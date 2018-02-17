package com.projects.blockchain.ethereum.poc.node_connector.util;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import com.projects.blockchain.ethereum.smart_contracts.CoinManager;

public final class Web3jContainer {
	private final Web3j web3j;
	private final Credentials credentials;
	private final CoinManager coinManager;
	
	public Web3jContainer(final Web3j web3j, final Credentials credentials, final CoinManager coinManager) {
		this.web3j = web3j;
		this.credentials = credentials;
		this.coinManager = coinManager;
	}
	
	public Web3j getWeb3j() {
		return web3j;
	}
	public Credentials getCredentials() {
		return credentials;
	}
	public CoinManager getCoinManager() {
		return coinManager;
	}
	
}