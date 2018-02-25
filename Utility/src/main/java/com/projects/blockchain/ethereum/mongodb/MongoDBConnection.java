package com.projects.blockchain.ethereum.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public final class MongoDBConnection implements AutoCloseable {
	
	private final MongoClient mongoClient;
	private final MongoDatabase mongoDatabase;
	
	public MongoDBConnection(final String host, final int port, final String databaseName) {
		mongoClient = new MongoClient(host, port);
		mongoDatabase = mongoClient.getDatabase(databaseName);
	}
	
	@Override
	public void close() throws Exception {
		if (mongoClient != null) {
			mongoClient.close();
			System.out.println("Connection closed.");
		}	
	}
	
	public MongoDatabase getMongoDatabase() {
		return mongoDatabase;
	}
}