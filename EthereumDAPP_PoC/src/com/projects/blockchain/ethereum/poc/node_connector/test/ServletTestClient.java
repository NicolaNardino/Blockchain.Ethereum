package com.projects.blockchain.ethereum.poc.node_connector.test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import com.projects.blockchain.ethereum.poc.node_connector.util.OpType;
import com.projects.blockchain.ethereum.utility.Utility;

/**
 * Allows for testing the interaction with the two servlets EtherTransferServlet and CoinManagerSmartContractServle, 
 * by sending requests and printing servlet replies.
 * */
public final class ServletTestClient {
	private static final String baseURL = "http://localhost:8080/EthereumDAPP_PoC/";	
	
	public static void main(final String[] args) {
		//parallelRuns(10, 9, 0, 0);
		testEtherTransferServlet(5);
		testCoinManagerServlet(15, OpType.RaiseFund);
		testCoinManagerServlet(15, OpType.TransferFund);
	}
	
	private static void testCoinManagerServlet(final int nrRequests, final OpType opType) {
		IntStream.range(1, nrRequests).forEach(i-> {
			System.out.println(Utility.servletCall(baseURL + "CoinManagerSmartContractServlet", "OpType="+opType+"&TargetAccount=0x9142A699d088be61C993Ace813829D3D25DeAc2d&FundAmount=10"));	
		});
	}
	
	private static void testEtherTransferServlet(final int nrRequests) {
		IntStream.range(1, nrRequests).forEach(ServletTestClient::testEtherTransfer);
	}

	private static void testCoinManagerTransferFund(final int i) {
		final int fundAmount = ThreadLocalRandom.current().nextInt(1, 20);
		System.out.println("Request nr.: "+i+" for "+fundAmount+" MyCoins transfer");
		Utility.servletCall(baseURL + "CoinManagerSmartContractServlet", "OpType="+OpType.TransferFund+"&TargetAccount=0x9142A699d088be61C993Ace813829D3D25DeAc2d&FundAmount="+fundAmount);
	}
	
	private static void testCoinManagerRaiseFund(final int i) {
		final int fundAmount = ThreadLocalRandom.current().nextInt(1, 20);
		System.out.println("Request nr.: "+i+" for "+fundAmount+" MyCoins raise.");
		System.out.println(Utility.servletCall(baseURL + "CoinManagerSmartContractServlet", "OpType="+OpType.TransferFund+"&TargetAccount=0x9142A699d088be61C993Ace813829D3D25DeAc2d&FundAmount="+fundAmount));
	}
	
	private static void testEtherTransfer(final int i) {
		final int weis = ThreadLocalRandom.current().nextInt(999) + 1;
		System.out.println("Request nr. "+i+" for "+weis+" WEIs.");
		System.out.println(Utility.servletCall(baseURL + "EtherTransferServlet", "TargetAccount=0x9142A699d088be61C993Ace813829D3D25DeAc2d&TransferAmount="+weis+"&TransferUnit=WEI"));
	}
	
	/**
	 * Hitting this way, in parallel, the Ethereum network wouldn't actually work because it's very likely multiple transactions would get the same nonce. 
	 * */
	private static void parallelRuns(final int parallelism, final int nrEtherTransferRequests, final int nrCoinManagerRaiseFundRequests, final int nrCoinManagerTransferFundRequests) {
		//ForkJoinPool limits the parallelism level to nr_available_processors - 1, which is too low for a client test application hitting an application server. 
		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", String.valueOf(parallelism));
		IntStream.range(1, nrCoinManagerRaiseFundRequests).parallel().forEach(ServletTestClient::testCoinManagerRaiseFund);
		IntStream.range(1, nrCoinManagerTransferFundRequests).parallel().forEach(ServletTestClient::testCoinManagerTransferFund);
		IntStream.range(1, nrEtherTransferRequests).parallel().forEach(ServletTestClient::testEtherTransfer);
	}
}