package com.projects.blockchain.ethereum.poc.microservices.client;

import java.math.BigInteger;
import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.projects.blockchain.ethereum.restful.DepositData;

/**
 * Coin Manager service client. 
 * */
@Service
public enum CoinManagerServiceClient implements CoinManagerServiceClientInterface {
	INSTANCE;
			
	private final RestTemplate restTemplate;
	
	private CoinManagerServiceClient() {
		restTemplate = new RestTemplate();
	}
	
	/* (non-Javadoc)
	 * @see com.projects.blockchain.ethereum.poc.microservices.client.CoinManagerServiceClientInterface#getOwner(java.lang.String)
	 */
	@Override
	public String getOwner(final String url) {
		return Utility.getFromRestTemplateForEntity(restTemplate, url);
	}
	
	/* (non-Javadoc)
	 * @see com.projects.blockchain.ethereum.poc.microservices.client.CoinManagerServiceClientInterface#getAccountBalance(java.lang.String, java.lang.String)
	 */
	@Override
	public BigInteger getAccountBalance(final String url, final String account) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return new BigInteger(Utility.getFromRestTemplateExchange(restTemplate, headers, UriComponentsBuilder.fromHttpUrl(url).path("/{account}").buildAndExpand(account).toUriString()));
	}
	
	/* (non-Javadoc)
	 * @see com.projects.blockchain.ethereum.poc.microservices.client.CoinManagerServiceClientInterface#raiseOrTransferFund(java.lang.String, java.lang.String, java.math.BigInteger)
	 */
	@Override
	public String raiseOrTransferFund(final String url, final String targetAccount, final BigInteger amount) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.TEXT_PLAIN_VALUE);
		return Utility.getFromRestTemplateExchange(restTemplate, headers, UriComponentsBuilder.fromHttpUrl(url).queryParam("targetAccount", targetAccount).queryParam("amount", amount).toUriString());
	}
	
	/* (non-Javadoc)
	 * @see com.projects.blockchain.ethereum.poc.microservices.client.CoinManagerServiceClientInterface#deposit(java.lang.String, com.projects.blockchain.ethereum.restful.DepositData)
	 */
	@Override
	public boolean deposit(final String url, final DepositData depositData) {
		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
		headers.setContentType(MediaType.APPLICATION_JSON);	
		final ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.PUT, new HttpEntity<DepositData>(depositData, headers), String.class);
		return (response.getStatusCode() == HttpStatus.OK); 
	}
	
	public static void main(final String[] args) {
		System.out.println(CoinManagerServiceClient.INSTANCE.getOwner("http://localhost:9095//ethererum/coin_manager/getOwner"));
		System.out.println(CoinManagerServiceClient.INSTANCE.getAccountBalance("http://localhost:9095//ethererum/coin_manager/getAccountBalance", "0x0c3d6f479511F1AE5d8bee86E9e13965fB652157"));
		
		System.out.println(CoinManagerServiceClient.INSTANCE.raiseOrTransferFund("http://localhost:9095//ethererum/coin_manager/transferFund", 
				"0x0c3d6f479511F1AE5d8bee86E9e13965fB652157", BigInteger.ONE));
		System.out.println(CoinManagerServiceClient.INSTANCE.raiseOrTransferFund("http://localhost:9095//ethererum/coin_manager/raiseFund", 
				"0x0c3d6f479511F1AE5d8bee86E9e13965fB652157", BigInteger.TEN));
		
		System.out.println(CoinManagerServiceClient.INSTANCE.deposit("http://localhost:9095//ethererum/coin_manager/deposit", 
				new DepositData("0x0c3d6f479511F1AE5d8bee86E9e13965fB652157", BigInteger.TEN)));
	}
}
