package com.projects.blockchain.ethereum.poc.ethereum_service.controller;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.projects.blockchain.ethereum.poc.ethereum_service.controller.service.EtherTransferService;
import com.projects.blockchain.ethereum.utility.microservices.EtherTransferEvent;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Spring Boot RESTful service, which allows to transfer ETHERs/ WEIs between the main wallet account and a target one. 
 * */
@RestController
@RequestMapping("/ethererum")
public final class EthereumRestController {
	@Autowired
	private EtherTransferService etherTransferService;
	
	@ApiOperation(value = "Returns information about the Ethererum client version and the available accounts within the wallet.", response = EtherTransferEvent.class, responseContainer = "List")
	@RequestMapping(method = RequestMethod.GET, value = "/getInfo", produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> getInfo() throws IOException {
		final StringBuilder sb = new StringBuilder();
		final Web3j web3j = etherTransferService.getWeb3jContainer().getWeb3j();
		sb.append("Connected to Ethereum client version: " + web3j.web3ClientVersion().send().getWeb3ClientVersion()+"\n");
		final EthAccounts ethAccounts = web3j.ethAccounts().send();
		sb.append("List of Accounts/ PKs:\n");
		ethAccounts.getAccounts().stream().forEach(account -> sb.append("\t"+account+"\n"));
		return new ResponseEntity<String>(sb.toString(), HttpStatus.OK);	
	}
	
	@ApiOperation(value = "Transfers Ethers between the wallet main account and a target account.")
	@RequestMapping(method = RequestMethod.GET, value = "/transfer", produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> transfer(
			@ApiParam(value = "Target account where to transfer Wei/ Ether.", required = true) @RequestParam final String targetAccount,
			@ApiParam(value = "Transfer amount.", required = true) @RequestParam final BigDecimal transferAmount, 
			@ApiParam(value = "Transfer unit (Wei/ Ether).", required = true) @RequestParam final String transferUnit) throws Exception {
		final TransactionReceipt tr = etherTransferService.transfer(targetAccount, transferAmount, transferUnit);
		return new ResponseEntity<String>(tr.getTransactionHash(), HttpStatus.OK);	
	}
	
	@ApiOperation(value = "Used to check the container health.", response = String.class)
	@RequestMapping(method = RequestMethod.GET, value = "/healthcheck")
	public ResponseEntity<String> healthcheck() {
		return new ResponseEntity<String>("Up", HttpStatus.OK);
	}
}