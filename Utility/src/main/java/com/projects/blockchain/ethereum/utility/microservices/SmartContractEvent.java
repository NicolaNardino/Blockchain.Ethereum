package com.projects.blockchain.ethereum.utility.microservices;

import java.math.BigInteger;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.projects.blockchain.ethereum.utility.EventType;

@Document(collection = "SmartContractEventsCollection")
public class SmartContractEvent extends EventBase {	
	private String smartContractAddress;
	private EventType eventType;
	
	public SmartContractEvent() {}
	
	public SmartContractEvent(final String sourceAccount, final String targetAccount, final Integer amount, final Date eventDate, 
			final BigInteger sourceAccountBalance, final BigInteger targetAccountBalance, final String smartContractAddress, final EventType eventType) {
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
		return String.format("SmartContractEvent [smartContractAddress=%s, eventType=%s, toString()=%s]",
				smartContractAddress, eventType, super.toString());
	}
}
