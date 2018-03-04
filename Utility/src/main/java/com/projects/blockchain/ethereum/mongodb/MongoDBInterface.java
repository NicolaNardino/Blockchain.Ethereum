package com.projects.blockchain.ethereum.mongodb;

import java.util.List;

import com.projects.blockchain.ethereum.utility.EtherTransferEventDetail;
import com.projects.blockchain.ethereum.utility.EventDetail;
import com.projects.blockchain.ethereum.utility.SmartContractEventDetail;

/**
 * It provides set of methods to interact with a MongoDB database.
 * */
public interface MongoDBInterface {

	/**
	 * Add events to EventsDatabase/EventsCollection.
	 * 
	 * @param events List of events to add to the MongoDB collection. 
	 * */
	void addEvents(List<EventDetail> events);
	
	/**
	 * Gets all events out of EventsDatabase/SmartContractEventDetailsCollection.
	 * 
	 * @return All events out of the SmartContractEventDetailsCollection. 
	 * */
	public List<SmartContractEventDetail> getSmartContractEvents();
	
	/**
	 * Gets all events out of EventsDatabase/EtherTransferEventDetailsCollection.
	 * 
	 * @return All events out of the EtherTransferEventDetailsCollection. 
	 * */
	public List<EtherTransferEventDetail> getEtherTransferEvents();
}