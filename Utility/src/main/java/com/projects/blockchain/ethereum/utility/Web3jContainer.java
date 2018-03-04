package com.projects.blockchain.ethereum.utility;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

public final class Web3jContainer {
	private final Web3j web3j;
	private final Credentials credentials;
	
	public Web3jContainer(final Web3j web3j, final Credentials credentials) {
		this.web3j = web3j;
		this.credentials = credentials;
	}
	
	public Web3j getWeb3j() {
		return web3j;
	}
	public Credentials getCredentials() {
		return credentials;
	}
}