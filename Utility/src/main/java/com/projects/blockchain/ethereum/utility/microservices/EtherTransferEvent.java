package com.projects.blockchain.ethereum.utility.microservices;

import java.math.BigInteger;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "EtherTransferEventsCollection")
public class EtherTransferEvent extends EventBase {
	
	private String txHash;
	private BigInteger gas;
	private BigInteger gasPrice;
	
	public EtherTransferEvent() {}
			
	public EtherTransferEvent(String sourceAccount, String targetAccount, Integer amount, 
			Date eventDate, BigInteger sourceAccountBalance, BigInteger targetAccountBalance, 
			String txHash, BigInteger gas, BigInteger gasPrice) {
		super(sourceAccount, targetAccount, amount, eventDate, sourceAccountBalance, targetAccountBalance);
		this.txHash = txHash;
		this.gas = gas;
		this.gasPrice = gasPrice;
	}

	public String getTxHash() {
		return txHash;
	}


	public BigInteger getGas() {
		return gas;
	}


	public BigInteger getGasPrice() {
		return gasPrice;
	}

	public void setTxHash(String txHash) {
		this.txHash = txHash;
	}

	public void setGas(BigInteger gas) {
		this.gas = gas;
	}

	public void setGasPrice(BigInteger gasPrice) {
		this.gasPrice = gasPrice;
	}

	@Override
	public String toString() {
		return String.format("EtherTransferEvent1 [txHash=%s, gas=%s, gasPrice=%s, toString()=%s]", txHash, gas,
				gasPrice, super.toString());
	}
}
