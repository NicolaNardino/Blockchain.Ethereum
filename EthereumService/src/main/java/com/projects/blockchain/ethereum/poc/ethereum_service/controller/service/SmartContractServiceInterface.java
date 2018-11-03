package com.projects.blockchain.ethereum.poc.ethereum_service.controller.service;

import java.math.BigInteger;

public interface SmartContractServiceInterface {

	BigInteger getAccountBalance(String accountInDepositManager) throws Exception;

	BigInteger getBalance() throws Exception;

	String getOwner();
}