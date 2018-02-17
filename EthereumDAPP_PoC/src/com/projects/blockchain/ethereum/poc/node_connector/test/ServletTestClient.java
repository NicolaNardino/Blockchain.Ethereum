package com.projects.blockchain.ethereum.poc.node_connector.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Allows for testing the interaction with the EthereumNodeConnectorServlet by sending requests and printing servlet replies.
 * */
public final class ServletTestClient {
	private static final String baseURL = "http://localhost:8080/EthereumDAPP_PoC/EtherTransferServlet";
	//CoinManagerSmartContractRaiseFundServlet --> TargetAccount=0x9142A699d088be61C993Ace813829D3D25DeAc2d&FundAmount=10
	private static final int nrRequests = 5; 
	private static final Random random = new Random();
	
	public static void main(final String[] args) {
		IntStream.range(0,nrRequests).forEach(i->
		{
			final int weis = random.nextInt(10) + 1;
			System.out.println("Sending request for "+weis+" WEIs.\n");
	        System.out.println(servletCall(baseURL, "TargetAccount=0x9142A699d088be61C993Ace813829D3D25DeAc2d&TransferAmount="+weis+"&TransferUnit=WEI"));
	    });
	}

	public static String servletCall(final String baseURL, final String parameters) {
		HttpURLConnection con = null;
		try {
			final StringBuilder response = new StringBuilder();
			try (final BufferedReader responseStream = new BufferedReader(new InputStreamReader(new URL(baseURL + "?" + parameters).openStream()))) {
				String line;
				while ((line = responseStream.readLine()) != null) 
					response.append(line).append('\r');
			}
			return response.toString();
		} catch (final Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			if (con != null)
				con.disconnect();
		}
	}
}