package com.projects.blockchain.ethereum.smart_contracts.utility;

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
public final class MongoDBEventsRetriever {

	public static void main(final String[] args) throws NumberFormatException, Exception {
		final ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
		final Properties properties = Utility.getApplicationProperties("mongoDB.properties");
		exec.scheduleAtFixedRate(() -> {
			try(final MongoDBImplementation mongoDB = new MongoDBImplementation(
					new MongoDBConnection(properties.getProperty("host"), Integer.valueOf(properties.getProperty("port")), 
					properties.getProperty("eventsDatabaseName")), properties.getProperty("eventsCollectionName"))) {
				mongoDB.getEvents().stream().forEach(System.out::println);
			} catch (final Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} 
		}, 1, 5, TimeUnit.SECONDS);
		TimeUnit.SECONDS.sleep(60);
		Utility.shutdownExecutorService(exec, 10, TimeUnit.SECONDS);
	}

}
