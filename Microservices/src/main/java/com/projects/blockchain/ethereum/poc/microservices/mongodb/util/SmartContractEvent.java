package com.projects.blockchain.ethereum.poc.microservices.mongodb.util;

import java.math.BigInteger;
import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "SmartContractEventsCollection")
public class SmartContractEvent {	
	private final String sourceAccount;
	private final String targetAccount;
	private final int amount;
	private final Date eventDate;
	private final BigInteger sourceAccountBalance;
	private final BigInteger targetAccountBalance;
	private final String smartContractAddress;
	private final EventType eventType;
	
	
	public SmartContractEvent(final String sourceAccount, final String targetAccount, 
			final int amount, final Date eventDate, 
			final BigInteger sourceAccountBalance, final BigInteger targetAccountBalance, final String smartContractAddress, final EventType eventType) {
		this.sourceAccount = sourceAccount;
		this.targetAccount = targetAccount;
		this.amount = amount;
		this.eventDate = eventDate;
		this.sourceAccountBalance = sourceAccountBalance;
		this.targetAccountBalance = targetAccountBalance;
		this.smartContractAddress = smartContractAddress;
		this.eventType = eventType;
	}
	

	public String getSourceAccount() {
		return sourceAccount;
	}
	
	public String getTargetAccount() {
		return targetAccount;
	}
	
	public int getAmount() {
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

	public String getSmartContractAddress() {
		return smartContractAddress;
	}

	public EventType getEventType() {
		return eventType;
	}


	@Override
	public String toString() {
		return String.format(
				"SmartContractEvent [sourceAccount=%s, targetAccount=%s, amount=%s, eventDate=%s, sourceAccountBalance=%s, targetAccountBalance=%s, smartContractAddress=%s, eventType=%s]",
				sourceAccount, targetAccount, amount, eventDate, sourceAccountBalance, targetAccountBalance,
				smartContractAddress, eventType);
	}

}
