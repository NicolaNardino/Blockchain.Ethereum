package com.projects.blockchain.ethereum.poc.microservices.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public final class Utility {

	private static final Logger LOG = LoggerFactory.getLogger(Utility.class);
	
	public static String getFromRestTemplateForEntity(final RestTemplate restTemplate, final String url) {
		final ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		return ((response.getStatusCode() == HttpStatus.OK) ? response.getBody() : null);
	}
	
	public static String getFromRestTemplateExchange(final RestTemplate restTemplate, final HttpHeaders headers, final String url) {
		try {
			return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class).getBody();
		} catch(final HttpStatusCodeException e){
			LOG.warn(e.getRawStatusCode() + "/"+ e.getResponseBodyAsString());
			return null;
		}
	}
}
