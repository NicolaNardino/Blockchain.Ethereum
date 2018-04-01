package com.projects.blockchain.ethereum.smart_contracts;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.http.HttpService;

import com.projects.blockchain.ethereum.utility.SmartContractsUtility;
import com.projects.blockchain.ethereum.utility.Utility;

import rx.Subscription;

/**
 * Subscribes to the CoinManager events raised after some fund has been granted or transferred to an account.
 */
public final class SmartContractEventsSubscription {

	public static void main(String[] args) throws Exception {
		final Properties properties = Utility.getApplicationProperties("smartContracts.properties");
		final Web3j web3j = Web3j.build(new HttpService(properties.getProperty("nodeURL")));
		subscriptionWithSmartContractWrapper(web3j, properties.getProperty("accountPassword"),
				properties.getProperty("walletFilePath"));
		// subscriptionWithouSmartContractWrapper(web3j);
	}

	private static void subscriptionWithSmartContractWrapper(final Web3j web3j, final String accountPassword,
			final String walletFilePath) throws Exception {
		final Credentials credentials = WalletUtils.loadCredentials(accountPassword, walletFilePath);
		final CoinManager coinManager = SmartContractsUtility.loadCoinManager(web3j, credentials, SmartContractsUtility.CoinManagerAddress);
		final Subscription subscription = coinManager
				.weiDepositedEventObservable(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST)
				.subscribe(ser -> System.out.println(ReflectionToStringBuilder.toString(ser)));
		TimeUnit.MINUTES.sleep(10);
		subscription.unsubscribe();
		System.exit(0);// needed due to the inner workings of Web3j.
	}

	private static void subscriptionWithouSmartContractWrapper(final Web3j web3j) throws InterruptedException {
		final EthFilter filter = new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST,
				SmartContractsUtility.CoinManagerAddress);
		final Event event = new Event("Mint", Arrays.<TypeReference<?>>asList(),
				Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
				}, new TypeReference<Address>() {
				}, new TypeReference<Uint256>() {
				}));
		filter.addSingleTopic(EventEncoder.encode(event));
		final Subscription subscription = web3j.ethLogObservable(filter).subscribe(log -> {
			System.out.println(ReflectionToStringBuilder.toString(log));
			FunctionReturnDecoder.decode(log.getData(), event.getNonIndexedParameters()).stream()
					.forEach(t -> System.out.println(t.getTypeAsString() + "/ " + t.getValue()));
		});
		TimeUnit.MINUTES.sleep(10);
		subscription.unsubscribe();
		System.exit(0);// needed due to the inner workings of Web3j.
	}

}
