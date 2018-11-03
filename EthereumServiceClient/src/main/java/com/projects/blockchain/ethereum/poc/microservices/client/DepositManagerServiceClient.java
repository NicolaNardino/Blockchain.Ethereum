package com.projects.blockchain.ethereum.poc.microservices.client;

import java.math.BigInteger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * DepositManager service client. 
 * */
public enum DepositManagerServiceClient implements SmartContractManagerServiceClientInterface {
	INSTANCE;
			
	private final RestTemplate restTemplate;
	
	private DepositManagerServiceClient() {
		restTemplate = new RestTemplate();
	}
	
	/* (non-Javadoc)
	 * @see com.projects.blockchain.ethereum.poc.microservices.client.SmartContractManagerServiceClientInterface#getOwner(java.lang.String)
	 */
	@Override
	public String getOwner(final String url) {
		return Utility.getFromRestTemplateForEntity(restTemplate, url);
	}
	
	/* (non-Javadoc)
	 * @see com.projects.blockchain.ethereum.poc.microservices.client.SmartContractManagerServiceClientInterface#getBalance(java.lang.String)
	 */
	@Override
	public BigInteger getBalance(final String url) {
		return  new BigInteger(Utility.getFromRestTemplateForEntity(restTemplate, url));
	}
	
	/* (non-Javadoc)
	 * @see com.projects.blockchain.ethereum.poc.microservices.client.SmartContractManagerServiceClientInterface#getAccountBalance(java.lang.String, java.lang.String)
	 */
	@Override
	public BigInteger getAccountBalance(final String url, final String account) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return new BigInteger(Utility.getFromRestTemplateExchange(restTemplate, headers, UriComponentsBuilder.fromHttpUrl(url).path("/{account}").buildAndExpand(account).toUriString()));
	}
	
	public static void main(final String[] args) {
		System.out.println(DepositManagerServiceClient.INSTANCE.getOwner("http://localhost:9095//ethererum/deposit_manager/getOwner"));
		System.out.println(DepositManagerServiceClient.INSTANCE.getBalance("http://localhost:9095//ethererum/deposit_manager/getBalance"));
		System.out.println(DepositManagerServiceClient.INSTANCE.getAccountBalance("http://localhost:9095//ethererum/deposit_manager/getAccountBalance", "0x0c3d6f479511F1AE5d8bee86E9e13965fB652157"));
	}
}
