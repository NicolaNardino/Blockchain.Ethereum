package com.projects.blockchain.ethereum.mongodb;

import java.util.List;

import com.projects.blockchain.ethereum.mongodb.MongoDBImplementation.CollectionType;
import com.projects.blockchain.ethereum.utility.microservices.EtherTransferEvent;
import com.projects.blockchain.ethereum.utility.microservices.EventBase;
import com.projects.blockchain.ethereum.utility.microservices.SmartContractEvent;

/**
 * It provides set of methods to interact with a MongoDB database.
 * */
public interface MongoDBInterface {

	/**
	 * Add events to EventsDatabase/EventsCollection.
	 * 
	 * @param events List of events to add to the MongoDB collection. 
	 * */
	void addEvents(List<EventBase> events);
	
	/**
	 * Gets all events out of EventsDatabase/SmartContractEventDetailsCollection.
	 * 
	 * @return All events out of the SmartContractEventDetailsCollection. 
	 * */
	public List<SmartContractEvent> getSmartContractEvents();
	
	/**
	 * Gets all events out of EventsDatabase/EtherTransferEventDetailsCollection.
	 * 
	 * @return All events out of the EtherTransferEventDetailsCollection. 
	 * */
	public List<EtherTransferEvent> getEtherTransferEvents();

	/**
	 * Deletes the content of a given MongoDB collection.
	 * 
	 * @param collectionType Collection whose content will be removed.
	 * @return Number of deleted items.
	 */
	long deleteCollection(final CollectionType collectionType);
}