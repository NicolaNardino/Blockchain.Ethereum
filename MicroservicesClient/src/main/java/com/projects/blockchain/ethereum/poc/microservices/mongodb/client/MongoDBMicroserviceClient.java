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

import com.projects.blockchain.ethereum.utility.microservices.EtherTransferEvent;

public final class MongoDBMicroserviceClient {

	private static final String baseURL = "http://localhost:9094/events/";

	public static <T> List<T> getEvents(final String eventsMethod) {
		final RestTemplate rt = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
		headers.setContentType(MediaType.APPLICATION_JSON);
		final HttpEntity<EtherTransferEvent[]> entity = new HttpEntity<EtherTransferEvent[]>(headers);
		final ResponseEntity<List<T>> response = rt.exchange(baseURL+eventsMethod, HttpMethod.GET, entity, new ParameterizedTypeReference<List<T>>() {});
		final HttpStatus statusCode = response.getStatusCode();        
		if (statusCode == HttpStatus.OK) 
			return response.getBody();
		return null;
	}

	public static boolean addEtherTransferEvent(final EtherTransferEvent etherTransferEvent) {
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		headers.setContentType(MediaType.APPLICATION_JSON);
		final RestTemplate rt = new RestTemplate();	
		final EtherTransferEvent convertedObject = rt.postForObject(baseURL+"addEtherTransferEvent", new HttpEntity<>(etherTransferEvent, headers), 
				EtherTransferEvent.class);
		return (convertedObject != null);
	}

	public static void main(final String[] args) {
		MongoDBMicroserviceClient.addEtherTransferEvent(new EtherTransferEvent("123", "xxxxxxxxxx", 1, new Date(), BigInteger.ONE, BigInteger.ONE, "ewrere", BigInteger.ONE, BigInteger.ONE));
		MongoDBMicroserviceClient.getEvents("getEtherTransferEvents").forEach(System.out::println);
	}
}
