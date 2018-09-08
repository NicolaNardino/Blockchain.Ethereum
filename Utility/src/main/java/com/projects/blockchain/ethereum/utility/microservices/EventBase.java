package com.projects.blockchain.ethereum.utility.microservices;

import java.math.BigInteger;
import java.util.Date;

public class EventBase {
	private String sourceAccount;
	private String targetAccount;
	private Integer amount;
	private Date eventDate;
	private BigInteger sourceAccountBalance;
	private BigInteger targetAccountBalance;	
	
	public EventBase() {}
			
	public EventBase(String sourceAccount,String targetAccount, Integer amount, Date eventDate,
			BigInteger sourceAccountBalance, BigInteger targetAccountBalance) {
		this.sourceAccount = sourceAccount;
		this.targetAccount = targetAccount;
		this.amount = amount;
		this.eventDate = eventDate;
		this.sourceAccountBalance = sourceAccountBalance;
		this.targetAccountBalance = targetAccountBalance;
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

	public void setSourceAccount(String sourceAccount) {
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

	@Override
	public String toString() {
		return String.format(
				"EventBase [sourceAccount=%s, targetAccount=%s, amount=%s, eventDate=%s, sourceAccountBalance=%s, targetAccountBalance=%s]",
				sourceAccount, targetAccount, amount, eventDate, sourceAccountBalance, targetAccountBalance);
	}
	
}
