package com.projects.blockchain.ethereum.poc.node_connector;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import com.projects.blockchain.ethereum.poc.node_connector.util.ServletContextAttribute;
import com.projects.blockchain.ethereum.utility.Utility;
import com.projects.blockchain.ethereum.utility.Web3jContainer;

/**
 * This servlet establishes the connection to an Ethereum mode, connects to a wallet account and then transfers some ethers to a target account.
 * Node URL, Account Password, Wallet File Path and Sender Account (PK) are set as web application context parameters.
 * Target Account, Transfer Amount and Unit (WEI, ETH, ...) are passed as request parameters.
 */
@WebServlet("/EtherTransferServlet")
public final class EtherTransferServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default constructor.
	 */
	public EtherTransferServlet() {
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
		final BigDecimal transferAmount = new BigDecimal(request.getParameter("TransferAmount"));
		final String transferUnit = request.getParameter("TransferUnit");
		response.setContentType("text/plain");
		run(response.getWriter(), web3jContainer, targetAccount, transferAmount, transferUnit);
	}

	private static void run(final PrintWriter writer, final Web3jContainer web3jContainer, final String targetAccount, final BigDecimal transferAmount,
			final String transferUnit) {
		//Connects to an Ethereum node, most probably running on the localhost.
		try {
			final Web3j web3j = web3jContainer.getWeb3j();
			writer.println("Connected to Ethereum client version: " + web3j.web3ClientVersion().send().getWeb3ClientVersion());
			final EthAccounts ethAccounts = web3j.ethAccounts().send();
			writer.println("List of Accounts/ PKs");
			ethAccounts.getAccounts().stream().forEach(account -> writer.println("\t"+account));
			final Credentials credentials = web3jContainer.getCredentials();
			final long startTime = System.currentTimeMillis();
			final TransactionReceipt transferReceipt = Utility.ethTransferImplicitTransaction(web3j, credentials, targetAccount, transferAmount, 10, 150, Convert.Unit.fromString(transferUnit));
			writer.println("Transaction completed in: " + (System.currentTimeMillis() - startTime) + " ms. "
					+ "Details at https://rinkeby.etherscan.io/tx/" + transferReceipt.getTransactionHash());
		} catch (final Exception e) {
			writer.println("Error while processing EtherTransferServlet\n"+e.getMessage());
			e.printStackTrace();
		}
	}
}
