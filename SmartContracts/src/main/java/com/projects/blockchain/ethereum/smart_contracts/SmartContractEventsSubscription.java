package com.projects.blockchain.ethereum.smart_contracts;

import java.util.Arrays;
import java.util.Properties;

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
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;

import com.projects.blockchain.ethereum.smart_contracts.utility.Utility;

import rx.functions.Action1;

/**
 * Subscribes to the CoinManager event raised after some fund has been granted to an account.
 * */
public final class SmartContractEventsSubscription {

	public static void main(String[] args) throws Exception {
		final Properties properties = Utility.getApplicationProperties("smartContracts.properties");
		final Web3j web3j = Web3j.build(new HttpService(properties.getProperty("nodeURL")));
		final Credentials credentials = WalletUtils.loadCredentials(properties.getProperty("accountPassword"), properties.getProperty("walletFilePath"));
		final CoinManager coinManager = Utility.loadCoinManager(web3j, credentials, Utility.CoinManagerAddress);
		final EthFilter filter = new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, coinManager.getContractAddress());
		final Event event = new Event("Mint", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
		filter.addSingleTopic(EventEncoder.encode(event));
	    web3j.ethLogObservable(filter).subscribe(new Action1<Log>() {
	        @Override    
	        public void call(Log log) {
	            System.out.println(ReflectionToStringBuilder.toString(log));
	            FunctionReturnDecoder.decode(log.getData(), event.getNonIndexedParameters()).stream().forEach(t -> System.out.println(t.getValue()));
	        }
	    });

	}

}
