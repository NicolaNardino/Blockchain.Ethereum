package com.projects.blockchain.ethereum.poc.ethereum_service.controller;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.projects.blockchain.ethereum.poc.ethereum_service.controller.service.CoinManagerService;
import com.projects.blockchain.ethereum.restful.DepositData;
import com.projects.blockchain.ethereum.utility.OpType;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Spring Boot RESTful service allowing to interact with the CoinManager and, indirectly, with DepositManager smart contracts.
 * */
@RestController
@RequestMapping("/ethererum/coin_manager")
public final class CoinManagerRestController {
	@Autowired
	private CoinManagerService coinManagerService;
	
	@ApiOperation(value = "Mints a given amount in MyCoins.")
	@RequestMapping(method = RequestMethod.GET, value = "/raiseFund", produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> coinManagerRaiseFund(
			@ApiParam(value = "Target account where the fund will be minted.", required = true) @RequestParam final String targetAccount,
			@ApiParam(value = "MyCoins amount to be minted.", required = true) @RequestParam final BigInteger amount) throws Exception {
		final TransactionReceipt tr = coinManagerService.coinManagerOperation(OpType.RaiseFund, targetAccount, amount);
		return new ResponseEntity<String>(tr.getTransactionHash(), HttpStatus.OK);	
	}
	
	@ApiOperation(value = "Transfers MyCoins from CoinManager owner account to another.")
	@RequestMapping(method = RequestMethod.GET, value = "/transferFund", produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> coinManagerTransferFundFromOwnerAccount(
			@ApiParam(value = "Target account where the smart contract's MyCoins will be transferred.", required = true) @RequestParam final String targetAccount,
			@ApiParam(value = "MyCoins to be transferred.", required = true) @RequestParam final BigInteger amount) throws Exception {
		final TransactionReceipt tr = coinManagerService.coinManagerOperation(OpType.TransferFund, targetAccount, amount);
		return new ResponseEntity<String>(tr.getTransactionHash(), HttpStatus.OK);	
	}
	
	@ApiOperation(value = "Gets the CoinManager owner address.")
	@RequestMapping(method = RequestMethod.GET, value = "/getOwner", produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> getOwner() {
		return new ResponseEntity<String>(coinManagerService.getOwner(), HttpStatus.OK);	
	}
	
	@ApiOperation(value = "Gets the balance of an account within CoinManager.")
	@RequestMapping(method = RequestMethod.GET, value = "/getAccountBalance/{account}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<BigInteger> getAccountBalance(
			@ApiParam(value = "Account address whose balance will be retrieved within CoinManager.", required = true) @PathVariable final String account) throws Exception {
		return new ResponseEntity<BigInteger>(coinManagerService.getAccountBalance(account), HttpStatus.OK);	
	}
	
	@ApiOperation(value = "Deposits some WEIs to an accoun within DepositManager via CoinManager.")
	@RequestMapping(method = RequestMethod.PUT, value = "/deposit")
	public ResponseEntity<String> deposit(
			@ApiParam(value = "Wrapper class for Account address within DepositManager and transfer amount.", required = true) 
			@RequestBody final DepositData depositData) throws Exception {
		coinManagerService.sendToDepositManager(depositData);
		return new ResponseEntity<String>("Deposited "+depositData.getAmount()+" to "+depositData.getTargetAccount(), HttpStatus.OK);	
	}
}