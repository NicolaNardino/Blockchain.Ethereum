package com.projects.blockchain.ethereum.smart_contracts;

import java.util.Properties;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import com.projects.blockchain.ethereum.utility.SmartContractName;
import com.projects.blockchain.ethereum.utility.SmartContractsUtility;
import com.projects.blockchain.ethereum.utility.Utility;

/**
 * Utility class to deploy and kill smart contracts.
 * */
public final class SmartContractsDeployer {

	public static void main(final String[] args) {
		try {
			final Properties properties = Utility.getApplicationProperties("smartContracts.properties");
			final Web3j web3j = Web3j.build(new HttpService(properties.getProperty("nodeURL")));
			final Credentials credentials = WalletUtils.loadCredentials(properties.getProperty("accountPassword"), properties.getProperty("walletFilePath"));		
			SmartContractsUtility.reDeployCoinManager(web3j, credentials, "MyCoin", SmartContractName.DepositManager.getAddress());
			//final DepositManager depositManager = SmartContractsUtility.reDeployDepositManager(web3j, credentials, "MyCoin");
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
