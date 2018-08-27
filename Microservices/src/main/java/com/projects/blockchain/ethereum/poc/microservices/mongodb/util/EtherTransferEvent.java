package com.projects.blockchain.ethereum.poc.microservices.mongodb.util;

import java.math.BigInteger;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "EtherTransferEventsCollection")
public class EtherTransferEvent {
	private final String sourceAccount;
	private final String targetAccount;
	private final Integer amount;
	private final Date eventDate;
	private final BigInteger sourceAccountBalance;
	private final BigInteger targetAccountBalance;
	private final String txHash;
	private final BigInteger gas;
	private final BigInteger gasPrice;	


	public EtherTransferEvent(final String sourceAccount, final String targetAccount, final Integer amount, final Date eventDate,
			final BigInteger sourceAccountBalance, final BigInteger targetAccountBalance, final String txHash, final BigInteger gas,
			final BigInteger gasPrice) {
		this.sourceAccount = sourceAccount;
		this.targetAccount = targetAccount;
		this.amount = amount;
		this.eventDate = eventDate;
		this.sourceAccountBalance = sourceAccountBalance;
		this.targetAccountBalance = targetAccountBalance;
		this.txHash = txHash;
		this.gas = gas;
		this.gasPrice = gasPrice;
	}

	public String getSourceAccount() {
		return sourceAccount;
	}


	public String getTargetAccount() {
		return targetAccount;
	}


	public Integer getAmount() {
		return amount;
	}


	public Date getEventDate() {
		return eventDate;
	}


	public BigInteger getSourceAccountBalance() {
		return sourceAccountBalance;
	}


	public BigInteger getTargetAccountBalance() {
		return targetAccountBalance;
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


	@Override
	public String toString() {
		return String.format(
				"EtherTransferEvent [sourceAccount=%s, targetAccount=%s, amount=%s, eventDate=%s, sourceAccountBalance=%s, targetAccountBalance=%s, txHash=%s, gas=%s, gasPrice=%s]",
				sourceAccount, targetAccount, amount, eventDate, sourceAccountBalance, targetAccountBalance, txHash,
				gas, gasPrice);
	}	
}
