package com.projects.blockchain.ethereum.poc.ethereum_service.controller.service;

import java.math.BigInteger;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.projects.blockchain.ethereum.restful.DepositData;
import com.projects.blockchain.ethereum.smart_contracts.CoinManager;
import com.projects.blockchain.ethereum.utility.OpType;
import com.projects.blockchain.ethereum.utility.SmartContractName;
import com.projects.blockchain.ethereum.utility.SmartContractsUtility;

@Service
public final class CoinManagerService extends EthereumContextServiceBase implements SmartContractServiceInterface {

	private CoinManager coinManager;
	private String coinManagerOwner;
	
	public TransactionReceipt coinManagerOperation(final OpType opType, final String targetAccount, final BigInteger transferAmount) throws Exception {
		final TransactionReceipt transferReceipt;
		switch(opType) {
			case RaiseFund: transferReceipt = coinManager.mint(targetAccount, transferAmount).send(); break;
			case TransferFund: transferReceipt = coinManager.send(targetAccount, transferAmount).send(); break;
			default: transferReceipt = null;
		}
		return transferReceipt;
	}
	
	/* (non-Javadoc)
	 * @see com.projects.blockchain.ethereum.poc.ethereum_service.controller.service.SmartContractServiceBase#getAccountBalance(java.lang.String)
	 */
	@Override
	public BigInteger getAccountBalance(final String accountInCoinManager) throws Exception {
		return coinManager.balances(accountInCoinManager).send();
	}
	
	/* (non-Javadoc)
	 * @see com.projects.blockchain.ethereum.poc.ethereum_service.controller.service.SmartContractServiceBase#getBalance()
	 */
	@Override
	public BigInteger getBalance() throws Exception {
		return coinManager.getBalance().send();
	}
	
	/* (non-Javadoc)
	 * @see com.projects.blockchain.ethereum.poc.ethereum_service.controller.service.SmartContractServiceBase#getOwner()
	 */
	@Override
	public String getOwner() {
		return coinManagerOwner;
	}
	
	public void sendToDepositManager(final DepositData depositData) throws Exception {
		coinManager.sendToDepositManager(depositData.getTargetAccount(), depositData.getAmount()).send();
	}
		
	@PostConstruct
	public void init() throws Exception {
		super.init();
		coinManager = (CoinManager)SmartContractsUtility.loadSmartContract(web3jContainer.getWeb3j(), web3jContainer.getCredentials(), SmartContractName.CoinManager);
    	coinManagerOwner = coinManager.owner().send();
	}
}