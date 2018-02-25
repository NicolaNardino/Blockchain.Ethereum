package com.projects.blockchain.ethereum.poc.node_connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

import com.projects.blockchain.ethereum.mongodb.MongoDBConnection;
import com.projects.blockchain.ethereum.mongodb.MongoDBImplementation;
import com.projects.blockchain.ethereum.mongodb.MongoDBInterface;
import com.projects.blockchain.ethereum.poc.node_connector.util.ServletContextAttribute;
import com.projects.blockchain.ethereum.poc.node_connector.util.Web3jContainer;
import com.projects.blockchain.ethereum.smart_contracts.CoinManager;
import com.projects.blockchain.ethereum.smart_contracts.utility.SmartContractsUtility;
import com.projects.blockchain.ethereum.utility.EventDetail;
import com.projects.blockchain.ethereum.utility.EventType;
import com.projects.blockchain.ethereum.utility.Utility;

import rx.Subscription;

/***
 *When servlet context gets initialized, it does the following:
 *<ul>
 *	<li>Subscribes to all transactions executed on the sender account.</li>
 *  <li>Subscribes to Mint and Sent events raised by the smart contract CoinManager.</li>
 *  <li>Publishes to the servlet context, instances of <code>Web3j</code>, <code>CoinManager</code> and <code>Credentials</code>, so to be re-used by all servlets.</li>
 *  <li>Stores smart contract events to a Mongo DB collection.</li>
 *</ul>  
 *
 *Subscriptions get removed when the context gets destroyed. 
 */
@WebListener
public final class TransactionMonitoringContextListener implements ServletContextListener {
    private Subscription etherTransactionsSubscription;
    private Subscription coinManagerMintEventSubscription, coinManagerSentEventSubscription;
    private Web3j web3j;
    private final BlockingQueue<EventDetail> eventsQueue = new LinkedBlockingQueue<EventDetail>();
    private MongoDBInterface mongoDB;
    private final ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
    
    @Override
	public void contextInitialized(final ServletContextEvent sce) {
    	final ServletContext sc = sce.getServletContext();
    	final Web3jContainer web3jContainer = buildWeb3jContainer(sc);
    	mongoDB = new MongoDBImplementation(new MongoDBConnection(sc.getInitParameter("mongodb_host"), 
				Integer.valueOf(sc.getInitParameter("mongodb_port")), sc.getInitParameter("mongodb_database_name")), 
				sc.getInitParameter("mongodb_events_collection_name"));
		sc.setAttribute(ServletContextAttribute.Web3jContainer.toString(), web3jContainer);
		etherTransactionsSubscription = web3j.catchUpToLatestAndSubscribeToNewTransactionsObservable(DefaultBlockParameterName.LATEST)
                .filter(tx -> tx.getFrom().equals(sc.getInitParameter("SenderAccount")))
                .subscribe(tx -> PrintTransaction(tx), Throwable::printStackTrace, TransactionMonitoringContextListener::onComplete);
		coinManagerMintEventSubscription = web3jContainer.getCoinManager()
				.mintEventObservable(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST)
				.subscribe(ser -> {
					System.out.println("Mint Event\nFrom: "+ser.from+", To: "+ser.to+", Amount: "+ser.amount);
					eventsQueue.offer(new EventDetail(web3jContainer.getCoinManager().getContractAddress(), 
							ser.from, ser.to, ser.amount.intValue(), new Date(), EventType.Mint));
				});
		coinManagerSentEventSubscription = web3jContainer.getCoinManager()
				.sentEventObservable(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST)
				.subscribe(ser -> {
					System.out.println("Sent Event\nFrom: "+ser.from+", To: "+ser.to+", Amount: "+ser.amount);
					eventsQueue.offer(new EventDetail(web3jContainer.getCoinManager().getContractAddress(), 
							ser.from, ser.to, ser.amount.intValue(), new Date(), EventType.Sent));
				});
		exec.scheduleWithFixedDelay(() -> {
			final List<EventDetail> events = new ArrayList<EventDetail>();
			eventsQueue.drainTo(events);
			if (events.size() > 0)
				System.out.println("Drained "+events.size()+" events.");
			mongoDB.addEvents(events);
		}, 1, 10, TimeUnit.SECONDS);
	}
    
    private void PrintTransaction(final Transaction tx) {
    	final StringBuilder sb = new StringBuilder("Transactions subscriber\n");
    	sb.append("From: "+tx.getFrom()).append(", To: "+tx.getTo()+"").append(", Value: "+tx.getValue())
    	.append(", Gas: "+tx.getGas()).append(", GasPrice: "+tx.getGasPrice()).append(", TxFee: "+tx.getGas().multiply(tx.getGasPrice()))
    	.append(", TxHash: "+tx.getHash());
    	try {
    		final long startTime = System.currentTimeMillis();
			sb.append("\nSender account balance: "+web3j.ethGetBalance(tx.getFrom(), DefaultBlockParameterName.LATEST).send().getBalance() + " WEIs.")
			.append("\nTarget account balance: "+web3j.ethGetBalance(tx.getTo(), DefaultBlockParameterName.LATEST).send().getBalance() + " WEIs.")
			.append("\nTime take to get accounts balances: " + (System.currentTimeMillis() - startTime) + " ms.");
		} catch (final IOException e) {
			sb.append("\nUnable to get balances.\n"+e.getMessage());
			e.printStackTrace();
		}
    	System.out.println(sb.toString());
    }
    
    private Web3jContainer buildWeb3jContainer(final ServletContext sc) {
    	web3j = Web3j.build(new HttpService(sc.getInitParameter("NodeURL")));
		try {
			final Credentials credentials = WalletUtils.loadCredentials(sc.getInitParameter("AccountPassword"), sc.getInitParameter("WalletFilePath"));
			final CoinManager coinManager = SmartContractsUtility.loadCoinManager(web3j, credentials, SmartContractsUtility.CoinManagerAddress);
			return new Web3jContainer(web3j, credentials, coinManager);	
		}
    	catch (final Exception e) {
    		throw new RuntimeException(e);
    	}
    }
    
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		Utility.shutdownExecutorService(exec, 5, TimeUnit.SECONDS);
		etherTransactionsSubscription.unsubscribe();
		coinManagerMintEventSubscription.unsubscribe();
		coinManagerSentEventSubscription.unsubscribe();
		System.out.println("Context Destroyed");
	}
	
	private static void onComplete() {
		System.out.println("Completed");
	}
}