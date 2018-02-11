package com.projects.blockchain.ethereum.poc.node_connector;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import com.projects.blockchain.ethereum.smart_contracts.CoinManager;
import com.projects.blockchain.ethereum.smart_contracts.utility.Utility;

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
		final String nodeURL = request.getServletContext().getInitParameter("NodeURL");
		final String accountPassword = request.getServletContext().getInitParameter("AccountPassword");
		final String walletFilePath = request.getServletContext().getInitParameter("WalletFilePath");
		final String targetAccount = request.getParameter("TargetAccount");
		final BigInteger fundAmount = new BigInteger(request.getParameter("FundAmount"));
		response.setContentType("text/plain");
		run(response.getWriter(), nodeURL, accountPassword, walletFilePath, targetAccount, fundAmount);
	}

	private static void run(final PrintWriter writer, final String nodeURL, final String accountPassword,
			final String walletFilePath, final String targetAccount, final BigInteger transferAmount) {
		final Web3j web3j = Web3j.build(new HttpService(nodeURL));
		try {
			writer.println("Connected to Ethereum client version: " + web3j.web3ClientVersion().send().getWeb3ClientVersion());
			final CoinManager coinManager = Utility.loadCoinManager(web3j, WalletUtils.loadCredentials(accountPassword, walletFilePath), Utility.CoinManagerAddress);
			final long startTime = System.currentTimeMillis();
			final TransactionReceipt transferReceipt = coinManager.mint(targetAccount, transferAmount).send();
			writer.println("Transaction completed in: " + (System.currentTimeMillis() - startTime) + " ms. "
					+ "Details at https://rinkeby.etherscan.io/tx/" + transferReceipt.getTransactionHash());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
