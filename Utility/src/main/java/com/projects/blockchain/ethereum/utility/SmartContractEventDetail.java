package com.projects.blockchain.ethereum.utility;

import java.math.BigInteger;
import java.util.Date;

public final class SmartContractEventDetail extends EventDetail {
	private final String smartContractAddress;
	private final EventType eventType;
	
	public SmartContractEventDetail(final String smartContractAddress, final String sourceAccount, final String targetAccount,
			final BigInteger sourceAccountBalance, final BigInteger targetAccountBalance, final int amount,
			final Date eventDate, final EventType eventType) {
		super(sourceAccount, targetAccount, amount, eventDate, sourceAccountBalance, targetAccountBalance);
		this.smartContractAddress = smartContractAddress;
		this.eventType = eventType;
	}
	
	public String getSmartContractAddress() {
		return smartContractAddress;
	}

	public EventType getEventType() {
		return eventType;
	}
	
	@Override
	public String toString() {
		return "SmartContractEventDetail [smartContractAddress=" + smartContractAddress + ", eventType=" + eventType
				+ ", " + super.toString() + "]";
	}
}
