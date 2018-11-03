package com.projects.blockchain.ethereum.poc.ethereum_service.controller;

import java.math.BigInteger;

import org.springframework.http.ResponseEntity;

public interface SmartContractManagerRestControllerInterface {

	ResponseEntity<BigInteger> getAccountBalance(String account) throws Exception;

	ResponseEntity<BigInteger> getBalance() throws Exception;

	ResponseEntity<String> getOwner();

}