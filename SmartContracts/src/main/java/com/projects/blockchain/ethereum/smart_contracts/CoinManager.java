package com.projects.blockchain.ethereum.smart_contracts;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.2.0.
 */
public class CoinManager extends Contract {
    private static final String BINARY = "606060405260008054600160a060020a033316600160a060020a03199091161790556104c6806100306000396000f3006060604052600436106100825763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166327e235e38114610087578063368b8772146100b857806340c10f191461010b57806341c0e1b51461012d5780638da5cb5b14610140578063d0679d341461016f578063e21f37ce14610191575b600080fd5b341561009257600080fd5b6100a6600160a060020a036004351661021b565b60405190815260200160405180910390f35b34156100c357600080fd5b61010960046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061022d95505050505050565b005b341561011657600080fd5b610109600160a060020a0360043516602435610244565b341561013857600080fd5b610109610281565b341561014b57600080fd5b6101536102a8565b604051600160a060020a03909116815260200160405180910390f35b341561017a57600080fd5b610109600160a060020a03600435166024356102b7565b341561019c57600080fd5b6101a4610361565b60405160208082528190810183818151815260200191508051906020019080838360005b838110156101e05780820151838201526020016101c8565b50505050905090810190601f16801561020d5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b60026020526000908152604090205481565b60018180516102409291602001906103ff565b5050565b60005433600160a060020a0390811691161461025f57610240565b600160a060020a03821660009081526002602052604090208054820190555050565b60005433600160a060020a03908116911614156102a657600054600160a060020a0316ff5b565b600054600160a060020a031681565b600160a060020a033316600090815260026020526040902054819010156102dd57610240565b600160a060020a03338181166000908152600260205260408082208054869003905592851681528290208054840190557f3990db2d31862302a685e8086b5755072a6e2b5b780af1ee81ece35ee3cd3345918490849051600160a060020a039384168152919092166020820152604080820192909252606001905180910390a15050565b60018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103f75780601f106103cc576101008083540402835291602001916103f7565b820191906000526020600020905b8154815290600101906020018083116103da57829003601f168201915b505050505081565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061044057805160ff191683800117855561046d565b8280016001018555821561046d579182015b8281111561046d578251825591602001919060010190610452565b5061047992915061047d565b5090565b61049791905b808211156104795760008155600101610483565b905600a165627a7a72305820167994cd20fea66b5747933af3a9d58e8ec91f620583478663dbf861f8ec327c0029";

    protected CoinManager(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected CoinManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<SentEventResponse> getSentEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Sent", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<SentEventResponse> responses = new ArrayList<SentEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            SentEventResponse typedResponse = new SentEventResponse();
            typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<SentEventResponse> sentEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Sent", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, SentEventResponse>() {
            @Override
            public SentEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                SentEventResponse typedResponse = new SentEventResponse();
                typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<BigInteger> balances(String param0) {
        Function function = new Function("balances", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> setMessage(String _message) {
        Function function = new Function(
                "setMessage", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_message)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> mint(String receiver, BigInteger amount) {
        Function function = new Function(
                "mint", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(receiver), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> kill() {
        Function function = new Function(
                "kill", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> owner() {
        Function function = new Function("owner", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> send(String receiver, BigInteger amount) {
        Function function = new Function(
                "send", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(receiver), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> message() {
        Function function = new Function("message", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static RemoteCall<CoinManager> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(CoinManager.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<CoinManager> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(CoinManager.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static CoinManager load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new CoinManager(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static CoinManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new CoinManager(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class SentEventResponse {
        public String from;

        public String to;

        public BigInteger amount;
    }
}
