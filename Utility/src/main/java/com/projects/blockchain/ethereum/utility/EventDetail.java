package com.projects.blockchain.ethereum.utility;

import java.math.BigInteger;
import java.util.Date;

public class EventDetail {
	private final String sourceAccount;
	private final String targetAccount;
	private final int amount;
	private final Date eventDate;
	private final BigInteger sourceAccountBalance;
	private final BigInteger targetAccountBalance;
	
	public EventDetail(final String sourceAccount, final String targetAccount, final int amount,
			final Date eventDate, final BigInteger sourceAccountBalance, final BigInteger targetAccountBalance) {	
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
