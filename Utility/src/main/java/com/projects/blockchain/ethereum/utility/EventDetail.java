package com.projects.blockchain.ethereum.utility;

import java.math.BigInteger;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ 
		  @Type(value = SmartContractEventDetail.class, name = "SmartContractEventDetail"), 
		  @Type(value = EtherTransferEventDetail.class, name = "EtherTransferEventDetail") 
		})
public class EventDetail {
	private final String sourceAccount;
	private final String targetAccount;
	private final int amount;
	private final Date eventDate;
	private final BigInteger sourceAccountBalance;
	private final BigInteger targetAccountBalance;
	
	@JsonCreator
	public EventDetail(@JsonProperty("sourceAccount") final String sourceAccount, @JsonProperty("targetAccount") final String targetAccount, 
			@JsonProperty("amount") final int amount, @JsonProperty("eventDate") final Date eventDate, 
			@JsonProperty("sourceAccountBalance") final BigInteger sourceAccountBalance, @JsonProperty("targetAccountBalance") final BigInteger targetAccountBalance) {	
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

	@Override
	public String toString() {
		return "EventDetail [sourceAccount=" + sourceAccount + ", targetAccount=" + targetAccount + ", amount=" + amount
				+ ", eventDate=" + eventDate + ", sourceAccountBalance=" + sourceAccountBalance
				+ ", targetAccountBalance=" + targetAccountBalance + "]";
	}
}
