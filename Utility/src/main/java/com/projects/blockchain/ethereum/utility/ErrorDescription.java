package com.projects.blockchain.ethereum.utility;

import java.util.Date;

public final class ErrorDescription {
	  private final  Date timestamp;
	  private final String message;
	  private final String details;

	  public ErrorDescription(final Date timestamp, final String message, final String details) {
	    this.timestamp = timestamp;
	    this.message = message;
	    this.details = details;
	  }

	public Date getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}
}
