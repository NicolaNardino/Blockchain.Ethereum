package com.projects.blockchain.ethereum.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public final class Utility {
	
	public static Properties getApplicationProperties(final String propertiesFileName) throws FileNotFoundException, IOException {
		final Properties p = new Properties();
		try(final InputStream inputStream = ClassLoader.getSystemResourceAsStream(propertiesFileName)) {
			p.load(inputStream);
			return p;
		}
	}
	public static void shutdownExecutorService(final ExecutorService es, long timeout, TimeUnit timeUnit) {
		es.shutdown();
		try {
			if (!es.awaitTermination(timeout, timeUnit))
				es.shutdownNow();
		} catch (final InterruptedException e) {
			System.err.println("Unable to awaitTermination while shutting down the executor service.");
		}
		System.out.println("Terminated ExecutorService "+es.toString());
	}
}
