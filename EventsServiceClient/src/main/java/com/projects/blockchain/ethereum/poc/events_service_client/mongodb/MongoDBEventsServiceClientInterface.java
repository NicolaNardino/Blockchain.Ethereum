package com.projects.blockchain.ethereum.poc.events_service_client.mongodb;

import java.util.List;

public interface MongoDBEventsServiceClientInterface {

	<T> List<T> getEvents(String uri);

	<T> boolean addEvents(T event, String uri);

}