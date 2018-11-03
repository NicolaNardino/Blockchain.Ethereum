package com.projects.blockchain.ethereum.poc.ethereum_service.controller;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.projects.blockchain.ethereum.poc.ethereum_service.controller.service.DepositManagerService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Spring Boot RESTful service allowing to interact with DepositManager smart contract..
 * */
@RestController
@RequestMapping("/ethererum/deposit_manager")
public final class DepositManagerRestController implements SmartContractManagerRestControllerInterface {
	@Autowired
	private DepositManagerService depositManagerService;
	
	/* (non-Javadoc)
	 * @see com.projects.blockchain.ethereum.poc.ethereum_service.controller.SmartContractManager#getAccountBalance(java.lang.String)
	 */
	@Override
	@ApiOperation(value = "Gets the balance of an account within DepositManager.")
	@RequestMapping(method = RequestMethod.GET, value = "/getAccountBalance/{account}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<BigInteger> getAccountBalance(
			@ApiParam(value = "Account address whose balance will be retrieved within CoinManager.", required = true) @PathVariable final String account) throws Exception {
		return new ResponseEntity<BigInteger>(depositManagerService.getAccountBalance(account), HttpStatus.OK);	
	}
	
	/* (non-Javadoc)
	 * @see com.projects.blockchain.ethereum.poc.ethereum_service.controller.SmartContractManager#getBalance()
	 */
	@Override
	@ApiOperation(value = "Gets CoinManager balance.")
	@RequestMapping(method = RequestMethod.GET, value = "/getBalance", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<BigInteger> getBalance() throws Exception {
		return new ResponseEntity<BigInteger>(depositManagerService.getBalance(), HttpStatus.OK);	
	}
	
	/* (non-Javadoc)
	 * @see com.projects.blockchain.ethereum.poc.ethereum_service.controller.SmartContractManager#getOwner()
	 */
	@Override
	@ApiOperation(value = "Gets CoinManager owner address.")
	@RequestMapping(method = RequestMethod.GET, value = "/getOwner", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> getOwner() {
		return new ResponseEntity<String>(depositManagerService.getOwner(), HttpStatus.OK);	
	}
}