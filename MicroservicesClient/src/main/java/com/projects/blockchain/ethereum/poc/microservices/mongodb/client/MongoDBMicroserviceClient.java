package com.projects.blockchain.ethereum.poc.microservices.mongodb.client;

import java.util.Arrays;
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
	
	public static void main(final String[] args) {
		MongoDBMicroserviceClient.getEvents("getEtherTransferEvents").forEach(System.out::println);
	}
}
