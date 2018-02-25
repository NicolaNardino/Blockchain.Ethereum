package com.projects.blockchain.ethereum.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.projects.blockchain.ethereum.utility.EventDetail;
import com.projects.blockchain.ethereum.utility.EventType;

public final class MongoDBImplementation implements AutoCloseable, MongoDBInterface {
	
	private final MongoDBConnection mongoDBConnection;
	private final MongoCollection<Document> eventsCollection;
	
	public MongoDBImplementation(final MongoDBConnection mongoDBConnection, final String eventsCollectionName) {
		this.mongoDBConnection = mongoDBConnection;
		this.eventsCollection = mongoDBConnection.getMongoDatabase().getCollection(eventsCollectionName);
	}

	@Override
	public void addEvents(final List<EventDetail> events) {
		if (events == null || events.size() == 0)
			return;
		final long startTime = System.currentTimeMillis();
		final List<Document> docs = new ArrayList<Document>();
		events.forEach(event -> docs.add(
				new Document("SmartContractAddress", event.getSmartContractAddress())
		        .append("SourceAccount", event.getSourceAccount())
		        .append("TargetAccount",event.getTargetAccount())
				.append("Amount", event.getAmount())
				.append("EventType", event.getEventType().getCode())
				.append("EventDate", event.getEventDate())));
		eventsCollection.insertMany(docs);
		System.out.println(events.size() + " successfully stored in "+(System.currentTimeMillis() - startTime)+" ms.");
		
	}

	@Override
	public List<EventDetail> getEvents() {
		final long startTime = System.currentTimeMillis();
		final List<EventDetail> events = new ArrayList<EventDetail>();
		final MongoCursor<Document> cursor =  eventsCollection.find().iterator();
		try {
		    while (cursor.hasNext()) {
		    	final Document doc = cursor.next();
		    	events.add(new EventDetail(
		    			doc.getString("SmartContractAddress"), doc.getString("SourceAccount"), doc.getString("TargetAccount"), 
		    			doc.getInteger("Amount"), doc.getDate("EventDate"), EventType.getEventType(doc.getString("EventType"))));
		    }
		} finally {
		    cursor.close();
		}
		System.out.println("Time taken to retrieve "+events.size()+" events: "+(System.currentTimeMillis() - startTime)+" ms.");
		return events;
	}
	
	@Override
	public void close() throws Exception {
		mongoDBConnection.close();
	}
}