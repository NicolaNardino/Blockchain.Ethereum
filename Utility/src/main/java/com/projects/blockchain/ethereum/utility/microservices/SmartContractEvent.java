package com.projects.blockchain.ethereum.utility.microservices;

import java.math.BigInteger;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.projects.blockchain.ethereum.utility.EventType;

@Document(collection = "SmartContractEventsCollection")
public class SmartContractEvent {	
	private String sourceAccount;
	private String targetAccount;
	private Integer amount;
	private Date eventDate;
	private BigInteger sourceAccountBalance;
	private BigInteger targetAccountBalance;
	private String smartContractAddress;
	private EventType eventType;
	
	public SmartContractEvent() {}
	
	public SmartContractEvent(final String sourceAccount, final String targetAccount, 
			final Integer amount, final Date eventDate, 
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

	public String getSmartContractAddress() {
		return smartContractAddress;
	}

	public EventType getEventType() {
		return eventType;
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

	public void setSmartContractAddress(String smartContractAddress) {
		this.smartContractAddress = smartContractAddress;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	@Override
	public String toString() {
		return String.format(
				"SmartContractEvent [sourceAccount=%s, targetAccount=%s, amount=%s, eventDate=%s, sourceAccountBalance=%s, targetAccountBalance=%s, smartContractAddress=%s, eventType=%s]",
				sourceAccount, targetAccount, amount, eventDate, sourceAccountBalance, targetAccountBalance,
				smartContractAddress, eventType);
	}

}
