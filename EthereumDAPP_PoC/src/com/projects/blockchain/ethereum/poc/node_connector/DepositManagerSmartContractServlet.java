package com.projects.blockchain.ethereum.poc.node_connector;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.projects.blockchain.ethereum.poc.node_connector.util.OpType;
import com.projects.blockchain.ethereum.restful.DepositData;
import com.projects.blockchain.ethereum.restful.DepositManagerRESTfulClient;
import com.projects.blockchain.ethereum.smart_contracts.DepositManager;

/**
 * It interacts with @{code {@link DepositManager} through a RESTful web service. 
 * </ul>
 */
@WebServlet("/DepositManagerSmartContractServlet")
public final class DepositManagerSmartContractServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default constructor.
	 */
	public DepositManagerSmartContractServlet() {
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		final OpType opType = Enum.valueOf(OpType.class, request.getParameter("OpType"));
		final String targetAccount = request.getParameter("TargetAccount");
		final BigInteger amount = new BigInteger(request.getParameter("Amount"));
		response.setContentType("text/plain");
		run(response.getWriter(), request.getServletContext().getInitParameter("depositManagerRESTfulBaseURI"), opType, targetAccount, amount);
	}

	private static void run(final PrintWriter writer, final String depositManagerRESTfulBaseURL, final OpType opType, final String targetAccount, final BigInteger amount) {
		try {
			final DepositManagerRESTfulClient restfulClient = new DepositManagerRESTfulClient(depositManagerRESTfulBaseURL);
			final StringBuilder sb = new StringBuilder();
			switch(opType) {
				case Deposit: sb.append(restfulClient.deposit(new DepositData(targetAccount, amount))); break;
				case DepositAccountBalance: sb.append(restfulClient.getAccountBalance(targetAccount)); break;
				default: throw new IllegalArgumentException("Unknown OpType: "+opType);
			}
			sb.append("\nDepositManager balance: "+restfulClient.getDepositManagerBalance());
			writer.println(sb.toString());
		} catch (final Exception e) {
			e.printStackTrace();
			writer.println("Error while processing DepositManagerSmartContractServlet\n"+e.getMessage());
		}
	}
}
