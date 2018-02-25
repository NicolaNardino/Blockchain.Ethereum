package com.projects.blockchain.ethereum.smart_contracts.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.projects.blockchain.ethereum.mongodb.MongoDBConnection;
import com.projects.blockchain.ethereum.mongodb.MongoDBImplementation;
import com.projects.blockchain.ethereum.utility.Utility;

/**
 * Every X seconds, it extracts prints out all <code>CoinManager</code> events retrieved from a MongoDB database.
 * */
public final class MongoDBEventsRetriever implements AutoCloseable {

	private final ScheduledExecutorService exec;
	private final MongoDBImplementation mongoDB;
	
	public MongoDBEventsRetriever () throws FileNotFoundException, IOException {
		final Properties properties = Utility.getApplicationProperties("mongoDB.properties");
		exec = Executors.newScheduledThreadPool(1);
		mongoDB = new MongoDBImplementation(
				new MongoDBConnection(properties.getProperty("host"), Integer.valueOf(properties.getProperty("port")), 
				properties.getProperty("eventsDatabaseName")), properties.getProperty("eventsCollectionName"));
	}
	
	public void start(final int period, final int runtimeInSeconds) {
		try {
			exec.scheduleAtFixedRate(() -> mongoDB.getEvents().stream().forEach(System.out::println) , 1, period, TimeUnit.SECONDS);
			TimeUnit.SECONDS.sleep(runtimeInSeconds);	
		}
		catch(final Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public void stop() throws Exception {
		Utility.shutdownExecutorService(exec, 10, TimeUnit.SECONDS);
		mongoDB.close();
	}
	
	@Override
	public void close() throws Exception {
		stop();
	}
	
	public static void main(final String[] args) throws NumberFormatException, Exception {
		try (final MongoDBEventsRetriever eventsRetriever = new MongoDBEventsRetriever()) {
			eventsRetriever.start(10, 120);	
		}
	}
}
