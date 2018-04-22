package com.projects.blockchain.ethereum.poc.node_connector.restful;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rest")
public final class DepositManagerProxyApplication extends Application {
	private final Set<Object> singletons = new HashSet<Object>();

	public DepositManagerProxyApplication() {
		singletons.add(new DepositManagerProxyImpl());
	}
	
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
