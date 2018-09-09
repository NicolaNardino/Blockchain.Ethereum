package com.projects.blockchain.ethereum.restful;

import java.util.List;

import javax.ws.rs.core.GenericType;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.projects.blockchain.ethereum.mongodb.MongoDBImplementation.CollectionType;
import com.projects.blockchain.ethereum.utility.microservices.EtherTransferEvent;
import com.projects.blockchain.ethereum.utility.microservices.SmartContractEvent;

/**
 * Client class for {@code EventsRESTfulImpl} restful web service.
 * */
public final class EventsRetrieverRESTfulClient {
	
	private final ResteasyClient client;
	private final String restfulBaseURL;
	 
	public EventsRetrieverRESTfulClient(final String restfulBaseURL) {
		client = new ResteasyClientBuilder().build();
		this.restfulBaseURL = restfulBaseURL;
	}
	
	public List<SmartContractEvent> getSmartContractEvents() {
		return client.target(restfulBaseURL + "getSmartContractEvents").request().get().readEntity(new GenericType<List<SmartContractEvent>>() {});
	}
	
	public List<EtherTransferEvent> getEtherTransferEvents() {
		return client.target(restfulBaseURL + "getEtherTransferEvents").request().get().readEntity(new GenericType<List<EtherTransferEvent>>() {});
	}
	
	public long deleteCollection(final CollectionType collectionType) {
		return client.target(restfulBaseURL + "deleteCollection/"+collectionType).request().delete(Long.class).longValue();
	}

	public static void main(final String[] args) throws JsonProcessingException {
		final EventsRetrieverRESTfulClient client = new EventsRetrieverRESTfulClient("http://localhost:8080/EthereumDAPP_PoC/rest/events/");
		client.getSmartContractEvents().stream().forEach(System.out::println);
	}
}

