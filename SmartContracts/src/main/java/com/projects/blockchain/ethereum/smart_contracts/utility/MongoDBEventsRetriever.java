package com.projects.blockchain.ethereum.smart_contracts.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.projects.blockchain.ethereum.mongodb.MongoDBConnection;
import com.projects.blockchain.ethereum.mongodb.MongoDBImplementation;
import com.projects.blockchain.ethereum.utility.EventType;
import com.projects.blockchain.ethereum.utility.SmartContractEventDetail;
import com.projects.blockchain.ethereum.utility.Utility;

/**
 * Every X seconds, it extracts prints out all <code>CoinManager</code> and
 * EtherTransfer events retrieved from a MongoDB database.
 */
public final class MongoDBEventsRetriever implements AutoCloseable {

	private final ScheduledExecutorService exec;
	private final MongoDBImplementation mongoDB;

	public MongoDBEventsRetriever() throws FileNotFoundException, IOException {
		final Properties properties = Utility.getApplicationProperties("mongoDB.properties");
		exec = Executors.newScheduledThreadPool(1);
		mongoDB = new MongoDBImplementation(
				new MongoDBConnection(properties.getProperty("host"), Integer.valueOf(properties.getProperty("port")),
						properties.getProperty("eventsDatabaseName")),
				properties.getProperty("smartContractEventsCollectionName"),
				properties.getProperty("etherTransferEventsCollectionName"));
	}

	public void start(final int period, final int runtimeInSeconds) {
		try {
			exec.scheduleAtFixedRate(this::printEvents, 1, period, TimeUnit.SECONDS);
			TimeUnit.SECONDS.sleep(runtimeInSeconds);
		} catch (final Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void printEvents() {
		System.out.println("Ether transfer events.");
		mongoDB.getEtherTransferEvents().stream().forEach(System.out::println);
		final Stream<SmartContractEventDetail> smartContractEvents = mongoDB.getSmartContractEvents().stream()
				.filter(e -> e.getEventType() != null);
		final Map<EventType, List<SmartContractEventDetail>> smartContractEventsGroupByEventType = smartContractEvents
				.collect(Collectors.groupingBy(SmartContractEventDetail::getEventType));
		System.out.println("Coin Manager raise fund events.");
		final List<SmartContractEventDetail> mintEvents = smartContractEventsGroupByEventType.get(EventType.Mint);
		if (mintEvents != null)
			mintEvents.stream().forEach(System.out::println);
		System.out.println("Coin Manager transfer fund events.");
		final List<SmartContractEventDetail> sentEvents = smartContractEventsGroupByEventType.get(EventType.Sent);
		if (sentEvents != null)
			sentEvents.stream().forEach(System.out::println);
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