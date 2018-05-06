package com.projects.blockchain.ethereum.poc.node_connector;

import java.io.IOException;
import java.math.BigInteger;
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

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.Transaction;

import com.projects.blockchain.ethereum.mongodb.MongoDBConnection;
import com.projects.blockchain.ethereum.mongodb.MongoDBImplementation;
import com.projects.blockchain.ethereum.mongodb.MongoDBInterface;
import com.projects.blockchain.ethereum.poc.node_connector.util.ServletContextAttribute;
import com.projects.blockchain.ethereum.smart_contracts.CoinManager;
import com.projects.blockchain.ethereum.smart_contracts.DepositManager;
import com.projects.blockchain.ethereum.utility.EtherTransferEventDetail;
import com.projects.blockchain.ethereum.utility.EventDetail;
import com.projects.blockchain.ethereum.utility.EventType;
import com.projects.blockchain.ethereum.utility.SmartContractEventDetail;
import com.projects.blockchain.ethereum.utility.SmartContractName;
import com.projects.blockchain.ethereum.utility.SmartContractsUtility;
import com.projects.blockchain.ethereum.utility.Utility;
import com.projects.blockchain.ethereum.utility.Web3jContainer;

import rx.Subscription;

/***
 *When servlet context gets initialized, it does the following:
 *<ul>
 *	<li>Subscribes to all transactions executed on the sender account.</li>
 *  <li>Subscribes to Mint and Sent events raised by the smart contract CoinManager.</li>
 *  <li>Publishes to the servlet context, instances of <code>Web3j</code>, <code>CoinManager</code>, <code>DepositManager</code> and <code>Credentials</code>, so to be re-used by all servlets.</li>
 *  <li>Stores smart contract events to a Mongo DB collection.</li>
 *</ul>  
 *
 *Subscriptions get removed when the context gets destroyed. 
 */
@WebListener
public final class TransactionMonitoringContextListener implements ServletContextListener {
    private Subscription etherTransactionsSubscription;
    private Subscription coinManagerMintEventSubscription, coinManagerSentEventSubscription, depositManagerDepositedEventSubscription;
    private MongoDBInterface mongoDB;
    private final BlockingQueue<EventDetail> eventsQueue = new LinkedBlockingQueue<>();
    private final ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
    
    @Override
	public void contextInitialized(final ServletContextEvent sce) {
    	final ServletContext sc = sce.getServletContext();
    	final Web3jContainer web3jContainer = Utility.buildWeb3jContainer(sc.getInitParameter("NodeURL"), sc.getInitParameter("AccountPassword"), sc.getInitParameter("WalletFilePath"));
    	final Web3j web3j = web3jContainer.getWeb3j();
    	final CoinManager coinManager = (CoinManager)SmartContractsUtility.loadSmartContract(web3j, web3jContainer.getCredentials(), SmartContractName.CoinManager);
    	final DepositManager depositManager = (DepositManager)SmartContractsUtility.loadSmartContract(web3j, web3jContainer.getCredentials(), SmartContractName.DepositManager);
		sc.setAttribute(ServletContextAttribute.Web3jContainer.toString(), web3jContainer);
		sc.setAttribute(ServletContextAttribute.CoinManager.toString(), coinManager);
		sc.setAttribute(ServletContextAttribute.DepositManager.toString(), depositManager);
		mongoDB = new MongoDBImplementation(new MongoDBConnection(sc.getInitParameter("mongoDBHost"), 
				Integer.valueOf(sc.getInitParameter("mongoDBPort")), sc.getInitParameter("mongoDBDatabaseName")), 
				sc.getInitParameter("mongoDBSmartContractEventsCollectionName"), sc.getInitParameter("mongoDBEtherTransferEventsCollectionName"));
		sc.setAttribute(ServletContextAttribute.MongoDBConnection.toString(), mongoDB);
		etherTransactionsSubscription = web3j.catchUpToLatestAndSubscribeToNewTransactionsObservable(DefaultBlockParameterName.LATEST)
                .filter(tx -> tx.getFrom().equals(sc.getInitParameter("SenderAccount")))
                .subscribe(tx -> {
                	PrintTransaction(web3j, tx);
                	eventsQueue.offer(new EtherTransferEventDetail(tx.getHash(), tx.getGas(), tx.getGasPrice(),  tx.getFrom(), tx.getTo(), 
                			getEtherBalance(web3j, tx.getFrom()), getEtherBalance(web3j, tx.getTo()),
                			(tx.getValue() == null ? 0 :  tx.getValue().intValue()), new Date()));
                }, 
                		Throwable::printStackTrace, TransactionMonitoringContextListener::onComplete);
		coinManagerMintEventSubscription = coinManager
				.mintEventObservable(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST)
				.subscribe(ser -> {
					System.out.println("Mint Event\nFrom: "+ser.from+", To: "+ser.to+", Amount: "+ser.amount);
					eventsQueue.offer(new SmartContractEventDetail(coinManager.getContractAddress(), 
							ser.from, ser.to, getCoinManagerBalance(coinManager, ser.from), getCoinManagerBalance(coinManager, ser.to), 
							ser.amount.intValue(), new Date(), EventType.Mint));
				});
		coinManagerSentEventSubscription = coinManager
				.sentEventObservable(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST)
				.subscribe(ser -> {
					System.out.println("Sent Event\nFrom: "+ser.from+", To: "+ser.to+", Amount: "+ser.amount);
					eventsQueue.offer(new SmartContractEventDetail(coinManager.getContractAddress(), 
							ser.from, ser.to, getCoinManagerBalance(coinManager, ser.from), getCoinManagerBalance(coinManager, ser.to), 
							ser.amount.intValue(), new Date(), EventType.Sent));
				});
		depositManagerDepositedEventSubscription = depositManager
				.weiReceivedEventObservable(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST)
				.subscribe(ser -> {
					System.out.println("Sent Event\nFrom: "+ser.from+", Amount: "+ser.amount);
				});
		exec.scheduleWithFixedDelay(this::addEventsToMongoDB, 1, 10, TimeUnit.SECONDS);
	}
    
    private void addEventsToMongoDB() {
    	final List<EventDetail> events = new ArrayList<>();
		eventsQueue.drainTo(events);
		if (events.size() > 0) {
			System.out.println("Drained "+events.size()+" events.");
			mongoDB.addEvents(events);
		}
    }
    
    private static void PrintTransaction(final Web3j web3j, final Transaction tx) {
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
    
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		Utility.shutdownExecutorService(exec, 5, TimeUnit.SECONDS);
		etherTransactionsSubscription.unsubscribe();
		coinManagerMintEventSubscription.unsubscribe();
		coinManagerSentEventSubscription.unsubscribe();
		depositManagerDepositedEventSubscription.unsubscribe();
		try {
			((MongoDBImplementation)mongoDB).close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		System.out.println("Context Destroyed");
	}
	
	private static void onComplete() {
		System.out.println("Completed");
	}
	
	private static BigInteger getCoinManagerBalance(final CoinManager coinManager, final String address) {
    	try {
			return coinManager.balances(address).send();
		} catch (final Exception e) {
			System.err.println("Unable to get CoinManager " + address+ "balance.");
			return BigInteger.valueOf(-1);
		}
    }
    
    private static BigInteger getEtherBalance(final Web3j web3j, final String address) {
    	try {
			return web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
		} catch (final Exception e) {
			System.err.println("Unable to get Ethererum " + address+ "balance.");
			return BigInteger.valueOf(-1);
		}
    }
}