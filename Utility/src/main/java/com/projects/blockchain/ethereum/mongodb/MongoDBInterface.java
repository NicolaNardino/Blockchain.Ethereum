package com.projects.blockchain.ethereum.mongodb;

import java.util.List;

import com.projects.blockchain.ethereum.utility.EventDetail;

/**
 * It provides (a rather limited, for the moment) set of methods to interact with a MongoDB database.
 * */
public interface MongoDBInterface {

	/**
	 * Add events to EventsDatabase/EventsCollection.
	 * 
	 * @param events List of events to add to the MongoDB collection. 
	 * */
	void addEvents(List<EventDetail> events);
	
	/**
	 * Gets all events out of EventsDatabase/EventsCollection.
	 * 
	 * @return All events out of the EventsCollection. 
	 * */
	public List<EventDetail> getEvents(); 

}