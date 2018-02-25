package com.projects.blockchain.ethereum.utility;

import java.util.Date;

public final class EventDetail {
	private final String smartContractAddress;
	private final String sourceAccount;
	private final String targetAccount;
	private final int amount;
	private final Date eventDate;
	private final EventType eventType;
	
	public EventDetail(final String smartContractAddress, final String sourceAccount, final String targetAccount, final int amount,
			final Date eventDate, final EventType eventType) {
		this.smartContractAddress = smartContractAddress;
		this.sourceAccount = sourceAccount;
		this.targetAccount = targetAccount;
		this.amount = amount;
		this.eventType = eventType;
		this.eventDate = eventDate;
	}
	
	public String getSmartContractAddress() {
		return smartContractAddress;
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

	public EventType getEventType() {
		return eventType;
	}

	public Date getEventDate() {
		return eventDate;
	}

	@Override
	public String toString() {
		return "EventDetail [smartContractAddress=" + smartContractAddress + ", sourceAccount=" + sourceAccount
				+ ", targetAccount=" + targetAccount + ", amount=" + amount + ", eventDate=" + eventDate
				+ ", eventType=" + eventType + "]";
	}

}
