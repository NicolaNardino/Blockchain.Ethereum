package com.projects.blockchain.ethereum.utility;

import java.util.Date;

public class EventDetail {
	private final String sourceAccount;
	private final String targetAccount;
	private final int amount;
	private final Date eventDate;	
	
	public EventDetail(final String sourceAccount, final String targetAccount, final int amount,
			final Date eventDate) {	
		this.sourceAccount = sourceAccount;
		this.targetAccount = targetAccount;
		this.amount = amount;
		this.eventDate = eventDate;
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

	@Override
	public String toString() {
		return "EventDetail [sourceAccount=" + sourceAccount + ", targetAccount=" + targetAccount + ", amount=" + amount
				+ ", eventDate=" + eventDate + "]";
	}
}
