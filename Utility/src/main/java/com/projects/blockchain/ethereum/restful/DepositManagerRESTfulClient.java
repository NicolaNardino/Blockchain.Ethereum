package com.projects.blockchain.ethereum.restful;

import java.math.BigInteger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

/**
 * Client class for {@code DepositManagerProxImpl} restful web service.
 * */
public final class DepositManagerRESTfulClient {
	
	private final ResteasyClient client;
	private final String restfulBaseURL;
	 
	public DepositManagerRESTfulClient(final String restfulBaseURL) {
		client = new ResteasyClientBuilder().build();
		this.restfulBaseURL = restfulBaseURL;
	}
	
	public String deposit(final DepositData depositData) {
		final ResteasyWebTarget storeMessages = client.target(restfulBaseURL+"deposit");
		final Response response = storeMessages.request().put(Entity.entity(depositData, MediaType.APPLICATION_XML));
		try {
			final String responseOutcome = response.readEntity(String.class);
			final int status = response.getStatus();
			if (status != Response.Status.OK.getStatusCode()) 
				return "Error while depositing "+depositData+", due to: "+responseOutcome+", status code: "+status+"/ "+Response.Status.fromStatusCode(status);
			return responseOutcome;	
		}
		finally {
			response.close();
		}
	}
	
	public BigInteger getAccountBalance(final String id) {
		return getBalance(client.target(restfulBaseURL + "getAccountBalance/"+id).request().get(), "balance of account "+id+" within DepositManager");
	}
	
	public BigInteger getDepositManagerBalance() {
		return getBalance(client.target(restfulBaseURL + "getDepositManagerBalance").request().get(), "DepositManager balance");
	}
	
	private static BigInteger getBalance(final Response response, final String errorMessage) {
		try {
			final int status = response.getStatus();
			if (status != Response.Status.OK.getStatusCode()) {
				System.out.println("Error while getting "+errorMessage+", due to: "+response.readEntity(String.class)+", status code: "+status+"/ "+Response.Status.fromStatusCode(status));
				return null;
			}
			return new BigInteger(response.readEntity(String.class));	
		}
		finally {
			response.close();	
		}
	}

	public static void main(final String[] args) {
		final DepositManagerRESTfulClient client = new DepositManagerRESTfulClient("http://localhost:8080/EthereumDAPP_PoC/rest/depositmanager/");
		//System.out.println(client.deposit(new DepositData("42352353", BigInteger.valueOf(12))));
		//System.out.println(client.getAccountBalance("42352353"));
		System.out.println(client.getDepositManagerBalance());
	}
}

