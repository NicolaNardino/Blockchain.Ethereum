package com.projects.blockchain.ethereum.poc.ethereum_service.controller.service;

import java.math.BigInteger;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.projects.blockchain.ethereum.smart_contracts.DepositManager;
import com.projects.blockchain.ethereum.utility.SmartContractName;
import com.projects.blockchain.ethereum.utility.SmartContractsUtility;

@Service
public final class DepositManagerService extends EthereumContextServiceBase implements SmartContractServiceBase {

	private DepositManager depositManager;
	private String depositManagerOwner;
	
	/* (non-Javadoc)
	 * @see com.projects.blockchain.ethereum.poc.ethereum_service.controller.service.SmartContractServiceBase#getAccountBalance(java.lang.String)
	 */
	@Override
	public BigInteger getAccountBalance(final String accountInDepositManager) throws Exception {
		return depositManager.weiDeposits(accountInDepositManager).send();
	}
	
	/* (non-Javadoc)
	 * @see com.projects.blockchain.ethereum.poc.ethereum_service.controller.service.SmartContractServiceBase#getBalance()
	 */
	@Override
	public BigInteger getBalance() throws Exception {
		return depositManager.getBalance().send();
	}
	
	/* (non-Javadoc)
	 * @see com.projects.blockchain.ethereum.poc.ethereum_service.controller.service.SmartContractServiceBase#getOwner()
	 */
	@Override
	public String getOwner() {
		return depositManagerOwner;
	}

	@PostConstruct
	public void init() throws Exception {
		super.init();
    	depositManager = (DepositManager)SmartContractsUtility.loadSmartContract(web3jContainer.getWeb3j(), web3jContainer.getCredentials(), SmartContractName.DepositManager);
    	depositManagerOwner = depositManager.owner().send();
	}
}
