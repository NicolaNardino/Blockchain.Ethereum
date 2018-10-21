package com.projects.blockchain.ethereum.poc.ethereum_service.controller.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Convert;

import com.projects.blockchain.ethereum.utility.Utility;

@Service
public final class EtherTransferService extends EthereumContextServiceBase {
	
	public TransactionReceipt transfer(final String targetAccount, final BigDecimal transferAmount, final String transferUnit) throws InterruptedException, TransactionException, Exception {
		return Utility.ethTransferImplicitTransaction(web3jContainer.getWeb3j(), web3jContainer.getCredentials(), 
				targetAccount, transferAmount, 10, 150, Convert.Unit.fromString(transferUnit));
	}
}
