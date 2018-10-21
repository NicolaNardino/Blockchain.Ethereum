package com.projects.blockchain.ethereum.poc.ethereum_service.controller.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.projects.blockchain.ethereum.utility.Utility;
import com.projects.blockchain.ethereum.utility.Web3jContainer;

@Component
public class EthereumContextServiceBase {

	protected Web3jContainer web3jContainer;
	
	@Autowired
	private Environment env;
	
	public Web3jContainer getWeb3jContainer() {
		return web3jContainer;
	}
	
	@PostConstruct
	public void init() throws Exception {
		web3jContainer = Utility.buildWeb3jContainer(env.getProperty("node.url"), env.getProperty("account.password"), env.getProperty("wallet.file.path"));
	}
}
