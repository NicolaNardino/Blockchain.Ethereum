package com.projects.blockchain.ethereum.poc.node_connector;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.projects.blockchain.ethereum.poc.node_connector.util.ServletContextAttribute;
import com.projects.blockchain.ethereum.poc.node_connector.util.Web3jContainer;

/**
 * It raises fund on a given account, by using a smart contract that manipulates its own coin.
 */
@WebServlet("/CoinManagerSmartContractRaiseFundServlet")
public final class CoinManagerSmartContractRaiseFundServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default constructor.
	 */
	public CoinManagerSmartContractRaiseFundServlet() {
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		final Web3jContainer web3jContainer = (Web3jContainer)request.getServletContext().getAttribute(ServletContextAttribute.Web3jContainer.toString());
		final String targetAccount = request.getParameter("TargetAccount");
		final BigInteger fundAmount = new BigInteger(request.getParameter("FundAmount"));
		response.setContentType("text/plain");
		run(response.getWriter(), web3jContainer, targetAccount, fundAmount);
	}

	private static void run(final PrintWriter writer, final Web3jContainer web3jContainer, 
			final String targetAccount, final BigInteger transferAmount) {
		try {
			writer.println("Connected to Ethereum client version: " + web3jContainer.getWeb3j().web3ClientVersion().send().getWeb3ClientVersion());
			final long startTime = System.currentTimeMillis();
			final TransactionReceipt transferReceipt = web3jContainer.getCoinManager().mint(targetAccount, transferAmount).send();
			writer.println("Transaction completed in: " + (System.currentTimeMillis() - startTime) + " ms. "
					+ "Details at https://rinkeby.etherscan.io/tx/" + transferReceipt.getTransactionHash());
		} catch (final Exception e) {
			e.printStackTrace();
			writer.println("Error while processing CoinManagerSmartContractRaiseFundServlet\n"+e.getMessage());
		}
	}
}
