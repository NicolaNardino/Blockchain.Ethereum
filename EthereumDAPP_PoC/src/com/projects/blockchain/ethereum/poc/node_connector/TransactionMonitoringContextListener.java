package com.projects.blockchain.ethereum.poc.node_connector;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.http.HttpService;

import com.projects.blockchain.ethereum.poc.node_connector.util.ServletContextAttribute;
import com.projects.blockchain.ethereum.poc.node_connector.util.Web3jContainer;
import com.projects.blockchain.ethereum.smart_contracts.CoinManager;
import com.projects.blockchain.ethereum.smart_contracts.utility.Utility;

import rx.Observable;
import rx.Subscription;

/***
 *
 *When initializing the servlet context, it establishes a subscription to monitor all transactions executed on a sender account 
 *defined as web application context parameter.
 *Furthermore, it publishes to the servlet context instances of <code>Web3j</code>, <code>CoinManager</code> and <code>Credentials</code>. 
 *
 *The subscription gets removed when the context gets destroyed. 
 */
@WebListener
public final class TransactionMonitoringContextListener implements ServletContextListener {
    private Subscription subscription;
    private Web3j web3j;

    @Override
	public void contextInitialized(final ServletContextEvent sce) {
    	final ServletContext sc = sce.getServletContext();
    	final Web3jContainer web3jContainer = buildWeb3jContainer(sc);
		sc.setAttribute(ServletContextAttribute.Web3jContainer.toString(), web3jContainer);
        final Observable<Transaction> observable = web3j.catchUpToLatestAndSubscribeToNewTransactionsObservable(DefaultBlockParameterName.LATEST);
		subscription = observable
                .filter(tx -> tx.getFrom().equals(sc.getInitParameter("SenderAccount")))
                .subscribe(tx -> PrintTransaction(tx), Throwable::printStackTrace, TransactionMonitoringContextListener::onComplete);
	}
    
    private void PrintTransaction(final Transaction tx) {
    	final StringBuilder sb = new StringBuilder("\n");
    	sb.append("From: "+tx.getFrom()).append(", To: "+tx.getTo()+"").append(", Value: "+tx.getValue())
    	.append(", Gas: "+tx.getGas()).append(", GasPrice: "+tx.getGasPrice()).append(", TxFee: "+tx.getGas().multiply(tx.getGasPrice()))
    	.append(", TxHash: "+tx.getHash());
    	try {
    		final long startTime = System.currentTimeMillis();
			sb.append("\nSender account balance: "+web3j.ethGetBalance(tx.getFrom(), DefaultBlockParameterName.LATEST).send().getBalance() + " WEIs.")
			.append("\nTarget account balance: "+web3j.ethGetBalance(tx.getTo(), DefaultBlockParameterName.LATEST).send().getBalance() + " WEIs.")
			.append("\nTime take to get accounts balances: " + (System.currentTimeMillis() - startTime) + " ms.");
		} catch (final IOException e) {
			e.printStackTrace();
		}
    	System.out.println(sb.toString());
    }
    
    private Web3jContainer buildWeb3jContainer(final ServletContext sc) {
    	web3j = Web3j.build(new HttpService(sc.getInitParameter("NodeURL")));
		try {
			final Credentials credentials = WalletUtils.loadCredentials(sc.getInitParameter("AccountPassword"), sc.getInitParameter("WalletFilePath"));
			final CoinManager coinManager = Utility.loadCoinManager(web3j, credentials, Utility.CoinManagerAddress);
			return new Web3jContainer(web3j, credentials, coinManager);	
		}
    	catch (final Exception e) {
    		throw new RuntimeException(e);
    	}
    }
    
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		subscription.unsubscribe();
		System.out.println("Context Destroyed");
	}
	
	private static void onComplete() {
		System.out.println("Completed");
	}
}