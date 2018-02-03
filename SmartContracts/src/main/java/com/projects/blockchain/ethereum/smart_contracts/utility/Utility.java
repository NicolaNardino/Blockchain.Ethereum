package com.projects.blockchain.ethereum.smart_contracts.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Utility {
	
	public static Properties getApplicationProperties(final String propertiesFileName) throws FileNotFoundException, IOException {
		final Properties p = new Properties();
		try(final InputStream inputStream = ClassLoader.getSystemResourceAsStream(propertiesFileName)) {
			p.load(inputStream);
			return p;
		}
	}
	
}
