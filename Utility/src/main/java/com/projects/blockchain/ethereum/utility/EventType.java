package com.projects.blockchain.ethereum.utility;

public enum EventType {
	Mint("M"), Sent("S");
	
	private final String code;
	
	private EventType(final String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static EventType getEventType(final String code) {
		switch(code) {
			case "M" : return EventType.Mint;
			case "S" : return EventType.Sent;
			default : throw new RuntimeException(code+" is not an expected value for EventType");
		}
	}
}
