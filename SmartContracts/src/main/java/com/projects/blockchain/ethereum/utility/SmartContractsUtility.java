package com.projects.blockchain.ethereum.utility;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;

import com.projects.blockchain.ethereum.smart_contracts.CoinManager;
import com.projects.blockchain.ethereum.smart_contracts.DepositManager;

public final class SmartContractsUtility {

	private enum OpType {
		Load, Deploy
	}
	
	/**
	 * Deploys one of the available smart contracts, CoinManager or DepositManager. 
	 * In order for a smart contract to be deployable from its initial Solidity (.sol) representation, it'must have been:
	 * <ul>
	 * 	<li>compiled by solc, which produced bin and abi files.</li>
	 *  <li>run the above bin and abi files with a web3j executable in order to produce the Java representation, which can be deployed.</li>
	 * </ul>
	 * 
	 *  @param params List of arguments to be passed to the smart contract constructor.
	 *  
	 * */
	public static Contract deploySmartContract(final Web3j web3j, final Credentials credentials, final SmartContractName scm, final String... params) throws Exception {
		final long startTime = System.currentTimeMillis();
		final Contract contract;
		switch(scm) {
		case CoinManager: 
			contract = CoinManager.deploy(web3j, credentials, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT, params[0], params[1]).send();
			break;
		case DepositManager: 
			contract = DepositManager.deploy(web3j, credentials, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT).send();
			break;
		default: throw new IllegalArgumentException("Unknown contract name: "+scm);
		}
		printInfo(OpType.Deploy, scm.toString(), contract.getContractAddress(), contract.isValid(), credentials.getAddress(), startTime);
		return contract;
	}

	/**
	 * Loads one of the available smart contracts, from the Ethereum network, into their Java representation.  
	 * 
	 * */
	public static Contract loadSmartContract(final Web3j web3j, final Credentials credentials, final SmartContractName scm) {
		try {
			final long startTime = System.currentTimeMillis();
			final Contract contract;
			switch(scm) {
			case CoinManager: 
				contract = CoinManager.load(scm.getAddress(), web3j, credentials, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);
				break;
			case DepositManager: 
				contract = DepositManager.load(scm.getAddress(), web3j, credentials, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);
				break;
			default: throw new IllegalArgumentException("Unknown contract name: "+scm);
			}
			printInfo(OpType.Load, scm.toString(), contract.getContractAddress(), contract.isValid(), credentials.getAddress(), startTime);
			return contract;	
		}
		catch(final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Redeploys CoinManager, by first loading and killing it.
	 * */
	public static CoinManager reDeployCoinManager(final Web3j web3j, final Credentials credentials, final String coinName, final String depositManagerAddress) throws Exception {
		((CoinManager)loadSmartContract(web3j, credentials, SmartContractName.CoinManager)).kill();
		return (CoinManager)deploySmartContract(web3j, credentials, SmartContractName.CoinManager, coinName, depositManagerAddress);
	}

	/**
	 * Redeploys DepositManager, by first loading and killing it. Once it's been redeployed, also CoinManager must be.
	 * In fact, the latter holds a reference to the address of DepositManager. 
	 * */
	public static DepositManager reDeployDepositManager(final Web3j web3j, final Credentials credentials, final String coinName) throws Exception {
		((DepositManager)loadSmartContract(web3j, credentials, SmartContractName.DepositManager)).kill();
		final Contract depositManager = deploySmartContract(web3j, credentials, SmartContractName.DepositManager);
		reDeployCoinManager(web3j, credentials, coinName, depositManager.getContractAddress());
		return (DepositManager)depositManager;
	}
	
	private static void printInfo(final OpType opType, final String name, final String address, final boolean isValid, final String owner, final long startTime) {
		final StringBuilder sb = new StringBuilder();
		sb.append("OpType: "+opType+"\n")
		.append("Contract: "+name+", address: "+ address+"\n")
		.append("Owner: "+owner+"\n")
		.append("Is valid: "+isValid+"\n")
		.append("Time taken: "+(System.currentTimeMillis() - startTime)+" ms.\n")
		.append("Details available at: https://rinkeby.etherscan.io/address/"+address+"\n\n");
		System.out.println(sb.toString());
	}
}
