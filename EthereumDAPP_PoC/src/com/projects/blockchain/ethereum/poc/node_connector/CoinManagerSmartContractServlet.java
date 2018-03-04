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

import com.projects.blockchain.ethereum.poc.node_connector.util.OpType;
import com.projects.blockchain.ethereum.poc.node_connector.util.ServletContextAttribute;
import com.projects.blockchain.ethereum.smart_contracts.CoinManager;
import com.projects.blockchain.ethereum.utility.Web3jContainer;

/**
 * It exercises all the functionalities for the <code>CoinManager</code> smart contract: 
 * <ul>
 * 	<li>Raise MyCoin funds to a given account: only valid when the sender is the contract creator.</li>
 *  <li>Transfer funds from the sender account to a target account.</li>
 *  <li>Check account balances.</li>
 * </ul>
 */
@WebServlet("/CoinManagerSmartContractServlet")
public final class CoinManagerSmartContractServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default constructor.
	 */
	public CoinManagerSmartContractServlet() {
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		final Web3jContainer web3jContainer = (Web3jContainer)request.getServletContext().getAttribute(ServletContextAttribute.Web3jContainer.toString());
		final CoinManager coinManager = (CoinManager)request.getServletContext().getAttribute(ServletContextAttribute.CoinManager.toString());
		final OpType opType = Enum.valueOf(OpType.class, request.getParameter("OpType"));
		final String targetAccount = request.getParameter("TargetAccount");
		final BigInteger fundAmount = new BigInteger(request.getParameter("FundAmount"));
		response.setContentType("text/plain");
		run(response.getWriter(), web3jContainer, coinManager, opType, targetAccount, fundAmount);
	}

	private static void run(final PrintWriter writer, final Web3jContainer web3jContainer, final CoinManager coinManager, final OpType opType,
			final String targetAccount, final BigInteger transferAmount) {
		try {
			writer.println("Connected to Ethereum client version: " + web3jContainer.getWeb3j().web3ClientVersion().send().getWeb3ClientVersion());
			final long startTime = System.currentTimeMillis();
			final TransactionReceipt transferReceipt;
			switch(opType) {
				case RaiseFund: transferReceipt = coinManager.mint(targetAccount, transferAmount).send(); break;
				case TransferFund: transferReceipt = coinManager.send(targetAccount, transferAmount).send(); break;
				default: transferReceipt = null;
			}
			final StringBuilder sb = new StringBuilder();
			sb.append("Transaction completed in: " + (System.currentTimeMillis() - startTime) + " ms. "+ "Details at https://rinkeby.etherscan.io/tx/" + transferReceipt.getTransactionHash()+"\n")
			.append("Account balances (in "+coinManager.getCoinName().send()+"), contract owner ("+coinManager.owner().send()+"): ")
			.append(coinManager.balances(coinManager.owner().send()).send()+", target account ("+targetAccount+"): "+coinManager.balances(targetAccount).send());
			writer.println(sb.toString());			
		} catch (final Exception e) {
			e.printStackTrace();
			writer.println("Error while processing CoinManagerSmartContractServlet\n"+e.getMessage());
		}
	}
}
