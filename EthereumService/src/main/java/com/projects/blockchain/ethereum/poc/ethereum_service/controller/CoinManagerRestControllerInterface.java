package com.projects.blockchain.ethereum.poc.ethereum_service.controller;

import java.math.BigInteger;

import org.springframework.http.ResponseEntity;

import com.projects.blockchain.ethereum.restful.DepositData;

public interface CoinManagerRestControllerInterface extends SmartContractManagerRestControllerInterface {

	ResponseEntity<String> coinManagerRaiseFund(String targetAccount, BigInteger amount) throws Exception;

	ResponseEntity<String> coinManagerTransferFundFromOwnerAccount(String targetAccount, BigInteger amount)
			throws Exception;

	ResponseEntity<String> deposit(DepositData depositData) throws Exception;

}