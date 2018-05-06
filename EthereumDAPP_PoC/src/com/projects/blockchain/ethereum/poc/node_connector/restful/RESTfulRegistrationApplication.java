package com.projects.blockchain.ethereum.poc.node_connector.restful;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rest")
public final class RESTfulRegistrationApplication extends Application {
	private final Set<Object> singletons = new HashSet<Object>();

	public RESTfulRegistrationApplication() {
		singletons.add(new DepositManagerRESTfulImpl());
		singletons.add(new EventsRESTfulImpl());
	}
	
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
