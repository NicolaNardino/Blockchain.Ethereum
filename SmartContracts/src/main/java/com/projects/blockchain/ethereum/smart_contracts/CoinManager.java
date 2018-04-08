package com.projects.blockchain.ethereum.smart_contracts;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionEncoder;
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
    private static final String BINARY = "6060604052341561000f57600080fd5b6040516108b33803806108b38339810160405280805182019190602001805160008054600160a060020a03191633600160a060020a031617905591508290506002818051610061929160200190610089565b505060058054600160a060020a031916600160a060020a039290921691909117905550610124565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100ca57805160ff19168380011785556100f7565b828001600101855582156100f7579182015b828111156100f75782518255916020019190600101906100dc565b50610103929150610107565b5090565b61012191905b80821115610103576000815560010161010d565b90565b610780806101336000396000f3006060604052600436106100ae5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166306bb7e55811461013257806312065fe01461013c57806327e235e314610161578063368b87721461018057806340c10f19146101d157806341c0e1b5146101f35780636feb527b14610206578063707d2ae2146102255780638da5cb5b146102af578063ce6d41de146102de578063d0679d34146102f1575b600160a060020a0333818116600090815260046020526040908190208054349081019091557f1f50245378c75895dc7bf30d1f28169f5f0d0ab6149340964721a6b9b3c1e5fe939091309091163190518084600160a060020a0316600160a060020a03168152602001838152602001828152602001935050505060405180910390a1005b61013a610313565b005b341561014757600080fd5b61014f6103d3565b60405190815260200160405180910390f35b341561016c57600080fd5b61014f600160a060020a03600435166103e2565b341561018b57600080fd5b61013a60046024813581810190830135806020601f820181900481020160405190810160405281815292919060208401838380828437509496506103f495505050505050565b34156101dc57600080fd5b61013a600160a060020a036004351660243561040b565b34156101fe57600080fd5b61013a61049d565b341561021157600080fd5b61014f600160a060020a03600435166104c4565b341561023057600080fd5b6102386104d6565b60405160208082528190810183818151815260200191508051906020019080838360005b8381101561027457808201518382015260200161025c565b50505050905090810190601f1680156102a15780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156102ba57600080fd5b6102c261057e565b604051600160a060020a03909116815260200160405180910390f35b34156102e957600080fd5b61023861058d565b34156102fc57600080fd5b61013a600160a060020a0360043516602435610600565b600554600160a060020a031663f340fa0134336040517c010000000000000000000000000000000000000000000000000000000063ffffffff8516028152600160a060020a0390911660048201526024016000604051808303818588803b151561037c57600080fd5b5af1151561038957600080fd5b505050507f52fcc3f597ad0a050c3cda46989cdaa9ff810249b1c54b91855e79bfcfa9285f3334604051600160a060020a03909216825260208201526040908101905180910390a1565b600160a060020a033016315b90565b60036020526000908152604090205481565b60018180516104079291602001906106aa565b5050565b60005433600160a060020a039081169116141561042757600080fd5b600160a060020a038216600090815260036020526040908190208054830190557fab8530f87dc9b59234c4623bf917212bb2536d647574c8e7e5da92c2ede0c9f89033908490849051600160a060020a039384168152919092166020820152604080820192909252606001905180910390a15050565b60005433600160a060020a03908116911614156104c257600054600160a060020a0316ff5b565b60046020526000908152604090205481565b6104de610728565b60028054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105745780601f1061054957610100808354040283529160200191610574565b820191906000526020600020905b81548152906001019060200180831161055757829003601f168201915b5050505050905090565b600054600160a060020a031681565b610595610728565b60018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105745780601f1061054957610100808354040283529160200191610574565b600160a060020a0333166000908152600360205260409020548190101561062657610407565b600160a060020a03338181166000908152600360205260408082208054869003905592851681528290208054840190557f3990db2d31862302a685e8086b5755072a6e2b5b780af1ee81ece35ee3cd3345918490849051600160a060020a039384168152919092166020820152604080820192909252606001905180910390a15050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106106eb57805160ff1916838001178555610718565b82800160010185558215610718579182015b828111156107185782518255916020019190600101906106fd565b5061072492915061073a565b5090565b60206040519081016040526000815290565b6103df91905b8082111561072457600081556001016107405600a165627a7a723058209fc28362658a0e8123b661fe3f56fc112214025d753015b533a7bb42b09f7c9b0029";

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

    public List<MintEventResponse> getMintEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Mint", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<MintEventResponse> responses = new ArrayList<MintEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            MintEventResponse typedResponse = new MintEventResponse();
            typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<MintEventResponse> mintEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Mint", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, MintEventResponse>() {
            @Override
            public MintEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                MintEventResponse typedResponse = new MintEventResponse();
                typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public List<WeiDepositedEventResponse> getWeiDepositedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("WeiDeposited", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<WeiDepositedEventResponse> responses = new ArrayList<WeiDepositedEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            WeiDepositedEventResponse typedResponse = new WeiDepositedEventResponse();
            typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.balance = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<WeiDepositedEventResponse> weiDepositedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("WeiDeposited", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, WeiDepositedEventResponse>() {
            @Override
            public WeiDepositedEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                WeiDepositedEventResponse typedResponse = new WeiDepositedEventResponse();
                typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.balance = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public List<SentToDepositManagerEventResponse> getSentToDepositManagerEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("SentToDepositManager", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<SentToDepositManagerEventResponse> responses = new ArrayList<SentToDepositManagerEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            SentToDepositManagerEventResponse typedResponse = new SentToDepositManagerEventResponse();
            typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<SentToDepositManagerEventResponse> sentToDepositManagerEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("SentToDepositManager", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, SentToDepositManagerEventResponse>() {
            @Override
            public SentToDepositManagerEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                SentToDepositManagerEventResponse typedResponse = new SentToDepositManagerEventResponse();
                typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<TransactionReceipt> sendToDepositManager(BigInteger weiValue) {
        Function function = new Function(
                "sendToDepositManager", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<BigInteger> getBalance() {
        Function function = new Function("getBalance", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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

    public RemoteCall<BigInteger> weiDeposits(String param0) {
        Function function = new Function("weiDeposits", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> getCoinName() {
        Function function = new Function("getCoinName", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> owner() {
        Function function = new Function("owner", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> getMessage() {
        Function function = new Function("getMessage", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
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

    public static RemoteCall<CoinManager> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _coinName, String _depositManager) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_coinName), 
                new org.web3j.abi.datatypes.Address(_depositManager)));
        return deployRemoteCall(CoinManager.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<CoinManager> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _coinName, String _depositManager) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_coinName), 
                new org.web3j.abi.datatypes.Address(_depositManager)));
        return deployRemoteCall(CoinManager.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
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

    public static class MintEventResponse {
        public String from;

        public String to;

        public BigInteger amount;
    }

    public static class WeiDepositedEventResponse {
        public String from;

        public BigInteger amount;

        public BigInteger balance;
    }

    public static class SentToDepositManagerEventResponse {
        public String from;

        public BigInteger amount;
    }
}
