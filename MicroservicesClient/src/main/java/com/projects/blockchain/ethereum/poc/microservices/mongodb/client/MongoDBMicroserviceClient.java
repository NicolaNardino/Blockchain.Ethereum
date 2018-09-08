package com.projects.blockchain.ethereum.poc.microservices.mongodb.client;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.projects.blockchain.ethereum.utility.EventType;
import com.projects.blockchain.ethereum.utility.microservices.EtherTransferEvent;
import com.projects.blockchain.ethereum.utility.microservices.EventsContainer;
import com.projects.blockchain.ethereum.utility.microservices.SmartContractEvent;

public enum MongoDBMicroserviceClient implements MongoDBMicroserviceClientInterface{
	INSTANCE;

	/* (non-Javadoc)
	 * @see com.projects.blockchain.ethereum.poc.microservices.mongodb.client.MongoDBMicroserviceClientInterface#getEvents(java.lang.String)
	 */
	@Override
	public <T> List<T> getEvents(final String uri) {
		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
		headers.setContentType(MediaType.APPLICATION_JSON);
		final ResponseEntity<List<T>> response = new RestTemplate().exchange(uri, HttpMethod.GET, new HttpEntity<T[]>(headers), 
				new ParameterizedTypeReference<List<T>>() {});
		return ((response.getStatusCode() == HttpStatus.OK) ? response.getBody() : null);
	}

	
	/* (non-Javadoc)
	 * @see com.projects.blockchain.ethereum.poc.microservices.mongodb.client.MongoDBMicroserviceClientInterface#addEvents(T, java.lang.String)
	 */
	@Override
	public <T> boolean addEvents(final T event, final String uri) {
		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
		headers.setContentType(MediaType.APPLICATION_JSON);	
		final ResponseEntity<String> response = new RestTemplate().exchange(uri, HttpMethod.POST, new HttpEntity<T>(event, headers), String.class);
		return (response.getStatusCode() == HttpStatus.OK); 
	}
	
	public static void main(final String[] args) {
		MongoDBMicroserviceClient.INSTANCE.addEvents(
				 new EventsContainer<EtherTransferEvent>(Arrays.asList(
						 new EtherTransferEvent("1234", "xxxxxxxxxx", 1, new Date(), BigInteger.ONE, BigInteger.ONE, "ewrere", BigInteger.ONE, BigInteger.ONE),
						 new EtherTransferEvent("1234", "xxxxxxxxxx", 1, new Date(), BigInteger.ONE, BigInteger.ONE, "ewrere", BigInteger.ONE, BigInteger.ONE),
						 new EtherTransferEvent("1234", "xxxxxxxxxx", 1, new Date(), BigInteger.ONE, BigInteger.ONE, "ewrere", BigInteger.ONE, BigInteger.TEN)
						 )), 
				"http://localhost:9094/events/addEtherTransferEvents");
		MongoDBMicroserviceClient.INSTANCE.getEvents("http://localhost:9094/events/getEtherTransferEvents").forEach(System.out::println);
		MongoDBMicroserviceClient.INSTANCE.addEvents(
				new EventsContainer<SmartContractEvent> (Arrays.asList(
						new SmartContractEvent("fdds", "fddfs", 1, new Date(), BigInteger.ONE, BigInteger.TEN, "fd", EventType.Mint), 
						new SmartContractEvent("fdds", "fddfs", 1, new Date(), BigInteger.ONE, BigInteger.TEN, "fd", EventType.Sent))), "http://localhost:9094/events/addSmartContractEvents");
		final List<Object> events = MongoDBMicroserviceClient.INSTANCE.getEvents("http://localhost:9094/events/getSmartContractEvents");
		events.forEach(System.out::println);
	}
}
