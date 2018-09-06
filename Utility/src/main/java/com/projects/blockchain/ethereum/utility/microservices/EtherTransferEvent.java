package com.projects.blockchain.ethereum.utility.microservices;

import java.math.BigInteger;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "EtherTransferEventsCollection")
public class EtherTransferEvent {
	private String sourceAccount;
	private String targetAccount;
	private Integer amount;
	private Date eventDate;
	private BigInteger sourceAccountBalance;
	private BigInteger targetAccountBalance;
	private String txHash;
	private BigInteger gas;
	private BigInteger gasPrice;	

	public EtherTransferEvent() {}
	
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


	/*public void setSourceAccount(String sourceAccount) {
		this.sourceAccount = sourceAccount;
	}

	public void setTargetAccount(String targetAccount) {
		this.targetAccount = targetAccount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public void setSourceAccountBalance(BigInteger sourceAccountBalance) {
		this.sourceAccountBalance = sourceAccountBalance;
	}

	public void setTargetAccountBalance(BigInteger targetAccountBalance) {
		this.targetAccountBalance = targetAccountBalance;
	}

	public void setTxHash(String txHash) {
		this.txHash = txHash;
	}

	public void setGas(BigInteger gas) {
		this.gas = gas;
	}

	public void setGasPrice(BigInteger gasPrice) {
		this.gasPrice = gasPrice;
	}*/

	@Override
	public String toString() {
		return String.format(
				"EtherTransferEvent [sourceAccount=%s, targetAccount=%s, amount=%s, eventDate=%s, sourceAccountBalance=%s, targetAccountBalance=%s, txHash=%s, gas=%s, gasPrice=%s]",
				sourceAccount, targetAccount, amount, eventDate, sourceAccountBalance, targetAccountBalance, txHash,
				gas, gasPrice);
	}	
}
