package com.projects.blockchain.ethereum.utility.microservices;

import java.util.List;

public class EventsContainer<T> {
	
	private List<T> events;

	public EventsContainer() {}
	
	public EventsContainer(List<T> events) {
		this.events = events;
	}

	public List<T> getEvents() {
		return events;
	}
}
