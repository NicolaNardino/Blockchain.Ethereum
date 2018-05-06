package com.projects.blockchain.ethereum.utility;

import java.math.BigInteger;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public final class SmartContractEventDetail extends EventDetail {	
	private final String smartContractAddress;
	private final EventType eventType;
	
	@JsonCreator
	public SmartContractEventDetail(@JsonProperty("smartContractAddress") final String smartContractAddress, @JsonProperty("sourceAccount") final String sourceAccount, 
			@JsonProperty("targetAccount") final String targetAccount, @JsonProperty("sourceAccountBalance") final BigInteger sourceAccountBalance, @JsonProperty("targetAccountBalance") final BigInteger targetAccountBalance, 
			@JsonProperty("amount") final int amount, @JsonProperty("eventDate") final Date eventDate, @JsonProperty("eventType") final EventType eventType) {
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
