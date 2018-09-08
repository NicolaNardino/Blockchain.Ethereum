package com.projects.blockchain.ethereum.poc.microservices.mongodb.client;

import java.util.List;

public interface MongoDBMicroserviceClientInterface {

	<T> List<T> getEvents(String uri);

	<T> boolean addEvents(T event, String uri);

}