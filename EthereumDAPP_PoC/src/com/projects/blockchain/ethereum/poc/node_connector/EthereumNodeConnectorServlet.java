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
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

/**
 * This servlet establishes the connection to an Ethereum mode, connects to a wallet account and then transfers some ethers to a target account.
 * Node URL, Account Password, Wallet File Path and Sender Account (PK) are set as web application context parameters.
 * Target Account, Transfer Amount and Unit (WEI, ETH, ...) are passed as request parameters.
 */
@WebServlet("/EthereumNodeConnectorServlet")
public final class EthereumNodeConnectorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default constructor.
	 */
	public EthereumNodeConnectorServlet() {
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		final String nodeURL = request.getServletContext().getInitParameter("NodeURL");
		final String accountPassword = request.getServletContext().getInitParameter("AccountPassword");
		final String walletFilePath = request.getServletContext().getInitParameter("WalletFilePath");
		final String targetAccount = request.getParameter("TargetAccount");
		final BigDecimal transferAmount = new BigDecimal(request.getParameter("TransferAmount"));
		final String transferUnit = request.getParameter("TransferUnit");
		response.setContentType("text/plain");
		run(response.getWriter(), nodeURL, accountPassword, walletFilePath, targetAccount, transferAmount,
				transferUnit);
	}

	private static void run(final PrintWriter writer, final String nodeURL, final String accountPassword,
			final String walletFilePath, final String targetAccount, final BigDecimal transferAmount,
			final String transferUnit) {
		//Connects to an Ethereum node, most probably running on the localhost.
		final Web3j web3j = Web3j.build(new HttpService(nodeURL));
		try {
			writer.println("Connected to Ethereum client version: " + web3j.web3ClientVersion().send().getWeb3ClientVersion());
			final EthAccounts ethAccounts = web3j.ethAccounts().send();
			writer.println("Accounts/ PKs in "+nodeURL);
			ethAccounts.getAccounts().stream().forEach(account -> writer.println("\t"+account));
			final Credentials credentials = WalletUtils.loadCredentials(accountPassword, walletFilePath);
			final long startTime = System.currentTimeMillis();
			final TransactionReceipt transferReceipt = Transfer
					.sendFunds(web3j, credentials, targetAccount, transferAmount, Convert.Unit.fromString(transferUnit))
					.send();// Convert.Unit.fromString(transferUnit)
			writer.println("Transaction completed in: " + (System.currentTimeMillis() - startTime) + " ms. "
					+ "Details at https://rinkeby.etherscan.io/tx/" + transferReceipt.getTransactionHash());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		run(new PrintWriter(System.out), "http://localhost:8545", "xxx", "xxx", "0x9142A699d088be61C993Ace813829D3D25DeAc2d", new BigDecimal("7"), "WEI");
	}

}
