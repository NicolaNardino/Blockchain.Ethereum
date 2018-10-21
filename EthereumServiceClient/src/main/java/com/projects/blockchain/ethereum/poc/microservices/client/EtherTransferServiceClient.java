package com.projects.blockchain.ethereum.poc.microservices.client;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Ether transfer client. 
 * */
@Service
public enum EtherTransferServiceClient implements EtherTransferServiceClientInterface {
	INSTANCE;
			
	private final RestTemplate restTemplate;
	
	private EtherTransferServiceClient() {
		restTemplate = new RestTemplate();
	}
	
	/* (non-Javadoc)
	 * @see com.projects.blockchain.ethereum.poc.microservices.client.EtherTransferClientInterface#getInfo(java.lang.String)
	 */
	@Override
	public String getInfo(final String url) {
		return Utility.getFromRestTemplateForEntity(restTemplate, url);
	}
	
	/* (non-Javadoc)
	 * @see com.projects.blockchain.ethereum.poc.microservices.client.EtherTransferClientInterface#transfer(java.lang.String, java.lang.String, java.math.BigDecimal, java.lang.String)
	 */
	@Override
	public String transfer(final String url, final String targetAccount, final BigDecimal transferAmount, final String transferUnit) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.TEXT_PLAIN_VALUE);
		final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
		        .queryParam("targetAccount", targetAccount)
		        .queryParam("transferAmount", transferAmount)
		        .queryParam("transferUnit", transferUnit);
		return Utility.getFromRestTemplateExchange(restTemplate, headers, builder.toString());
	} 
	
	public static void main(final String[] args) {
		System.out.println(EtherTransferServiceClient.INSTANCE.getInfo("http://localhost:9095/ethererum/getInfo"));
		System.out.println(EtherTransferServiceClient.INSTANCE.transfer("http://localhost:9095/ethererum/transfer", 
				"0x0c3d6f479511F1AE5d8bee86E9e13965fB652157", BigDecimal.TEN, "WEI"));
	}
}
