package com.projects.blockchain.ethereum.mongodb;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.projects.blockchain.ethereum.utility.EtherTransferEventDetail;
import com.projects.blockchain.ethereum.utility.EventDetail;
import com.projects.blockchain.ethereum.utility.EventType;
import com.projects.blockchain.ethereum.utility.SmartContractEventDetail;

public final class MongoDBImplementation implements AutoCloseable, MongoDBInterface {
	public static enum CollectionType {
		EtherTransfer, CoinManager
	}
	
	private final MongoDBConnection mongoDBConnection;
	private final MongoCollection<Document> smartContractEventsCollection;
	private final MongoCollection<Document> etherTransferEventsCollection;
	
	public MongoDBImplementation(final MongoDBConnection mongoDBConnection, final String smartContractEventsCollection, 
			final String etherTransferEventsCollection) {
		this.mongoDBConnection = mongoDBConnection;
		this.smartContractEventsCollection = mongoDBConnection.getMongoDatabase().getCollection(smartContractEventsCollection);
		this.etherTransferEventsCollection = mongoDBConnection.getMongoDatabase().getCollection(etherTransferEventsCollection);
	}

	@Override
	public void addEvents(final List<EventDetail> events) {
		if (events == null || events.size() == 0)
			return;
		addSmartContractEventDetails(events.stream().
				filter(SmartContractEventDetail.class::isInstance).
				map(SmartContractEventDetail.class::cast).
				collect(Collectors.toList()));
		addEtherTransferEventDetails(events.stream().
				filter(EtherTransferEventDetail.class::isInstance).
				map(EtherTransferEventDetail.class::cast).
				collect(Collectors.toList()));
	}
	
	@Override
	public List<SmartContractEventDetail> getSmartContractEvents() {
		final long startTime = System.currentTimeMillis();
		final List<SmartContractEventDetail> events = new ArrayList<>();
		final MongoCursor<Document> cursor =  smartContractEventsCollection.find().iterator();
		try {
		    while (cursor.hasNext()) {
		    	final Document doc = cursor.next();
		    	final String eventType = doc.getString("EventType");
		    	events.add(new SmartContractEventDetail(
		    			doc.getString("SmartContractAddress"), doc.getString("SourceAccount"), doc.getString("TargetAccount"),
		    			BigInteger.valueOf(doc.getInteger("SourceAccountBalance").intValue()), 
		    			BigInteger.valueOf(doc.getInteger("TargetAccountBalance").intValue()),
		    			doc.getInteger("Amount"), doc.getDate("EventDate"), eventType == null ? null : EventType.getEventType(eventType)));
		    }
		} finally {
		    cursor.close();
		}
		System.out.println("Time taken to retrieve "+events.size()+" SmartContractEvents: "+(System.currentTimeMillis() - startTime)+" ms.");
		return events;
	}
	
	@Override
	public List<EtherTransferEventDetail> getEtherTransferEvents() {
		final long startTime = System.currentTimeMillis();
		final List<EtherTransferEventDetail> events = new ArrayList<>();
		final MongoCursor<Document> cursor =  etherTransferEventsCollection.find().iterator();
		try {
		    while (cursor.hasNext()) {
		    	final Document doc = cursor.next();
		    	events.add(new EtherTransferEventDetail(
		    			doc.getString("TxHash"), BigInteger.valueOf(doc.getInteger("Gas").intValue()), 
		    			BigInteger.valueOf(doc.getInteger("GasPrice").intValue()), 
		    			doc.getString("SourceAccount"), doc.getString("TargetAccount"), 
		    			BigInteger.valueOf(doc.getInteger("SourceAccountBalance").intValue()), 
		    			BigInteger.valueOf(doc.getInteger("TargetAccountBalance").intValue()),  
		    			doc.getInteger("Amount"), doc.getDate("EventDate")));
		    }
		} finally {
		    cursor.close();
		}
		System.out.println("Time taken to retrieve "+events.size()+" EtherTransferEventDetails: "+(System.currentTimeMillis() - startTime)+" ms.");
		return events;
	}
	
	@Override
	public long deleteCollection(final CollectionType collectionType) {
		switch(collectionType) {
			case EtherTransfer: return etherTransferEventsCollection.deleteMany(new Document()).getDeletedCount();
			case CoinManager: return smartContractEventsCollection.deleteMany(new Document()).getDeletedCount();
			default:  
				throw new IllegalArgumentException(collectionType + "isn't an allowed collection type");
		}
	}
	
	private void addSmartContractEventDetails(final List<SmartContractEventDetail> smartContractEventDetails) {
		if (smartContractEventDetails.size() == 0)
			return;
		final long startTime = System.currentTimeMillis();
		final List<Document> docs = new ArrayList<>();
		smartContractEventDetails.forEach(event -> docs.add(
				new Document("SmartContractAddress", event.getSmartContractAddress())
		        .append("SourceAccount", event.getSourceAccount())
		        .append("TargetAccount",event.getTargetAccount())
		        .append("SourceAccountBalance", event.getSourceAccountBalance().intValue())
		        .append("TargetAccountBalance",event.getTargetAccountBalance().intValue())
				.append("Amount", event.getAmount().intValue())
				.append("EventType", event.getEventType().getCode())
				.append("EventDate", event.getEventDate())));
		smartContractEventsCollection.insertMany(docs);
		System.out.println(smartContractEventDetails.size() + " SmartContractEventDetails successfully stored in "+(System.currentTimeMillis() - startTime)+" ms.");
	}

	private void addEtherTransferEventDetails(final List<EtherTransferEventDetail> etherTransferEventDetails) {
		if (etherTransferEventDetails.size() == 0)
			return;
		final long startTime = System.currentTimeMillis();
		final List<Document> docs = new ArrayList<>();
		etherTransferEventDetails.forEach(event -> docs.add(
				new Document("TxHash", event.getTxHash())
				.append("Gas", event.getGas().intValue())
				.append("GasPrice", event.getGasPrice().intValue())
		        .append("SourceAccount", event.getSourceAccount())
		        .append("TargetAccount",event.getTargetAccount())
		        .append("SourceAccountBalance", event.getSourceAccountBalance().intValue())
		        .append("TargetAccountBalance",event.getTargetAccountBalance().intValue())
				.append("Amount", event.getAmount().intValue())
				.append("EventDate", event.getEventDate())));
		etherTransferEventsCollection.insertMany(docs);
		System.out.println(etherTransferEventDetails.size() + " EtherTransferEventDetails successfully stored in "+(System.currentTimeMillis() - startTime)+" ms.");
	}
	
	@Override
	public void close() throws Exception {
		mongoDBConnection.close();
	}
	
}