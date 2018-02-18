# Welcome to Blockchain.Ethereum Web Application DApp PoC
Java web application interacting with the Ethereum network.

For the time being its key features are:
  - Connection to the Ethereum Network via a node running on localhost.
  - Transfer ethers between wallet accounts.
  - Transaction monitoring.
  - CoinManager Smart Contract, which is able to create its own coin, mint coins and transfer such coins between accounts.   Developed in Solidity, it also uses inheritance.
  - Java Servlets to transfer ethers and exercise the CoinManager.

I've set up my Ethereum local environment as follows:
  - Run a full node by geth on the localhost and created two accounts: geth --rpcapi "personal,db,eth,net,web3" --rpc --rinkeby). Most notable here is the --rpc option by which it gets enabled the HTTP-RPC server on the node, then used by web3j to establish the connection from the web application.
  - For the sake of experimenting, I've installed an ethereum wallet UI, which I then connected to the above node.
  - Created two accounts by the geth command line and got them some Ethers on the Rinkeby testnet, https://faucet.rinkeby.io/. 
  - Fully synchronized on both mainnet and testnet but connected the web application to the testnet only.
  
Web3j is used to interact with the Ethereum Network.
I've added a smart contract, not yet integrated in the web app, based on a well-known contract, named Coin. I've extended it by adding inheritance and another event. By Web3j one can deploy and interact with smart contracts within the JVM once a Java wrapper in created from the Solidity bytecode.
Given the CoinManager.sol contract in the resources folder, with the following steps one creates a Java wrapper:
  - "solc --overwrite CoinManager.sol --bin --abi --gas --optimize -o .". Please, pay attention to the --gas option, which will let you know a rough estimate gas required to create the contract and transact with its methods.
  - "web3j solidity generate <your_path>/SmartContracts/src/main/resources/contracts/coinManager/CoinManager.bin   <your_path>/SmartContracts/src/main/resources/contracts/coinManager/CoinManager.abi -o <your_path>/SmartContracts/src/main/java -p com.projects.blockchain.ethereum.smart_contracts".

The above requires to have the Solidity compiler (solc) and the Web3j command line tools.
A JUnit test class is available to exercise the various smart contract features.

At the moment, I've set up three externally controlled accounts and one smart contract:

- 0x99fedc28c33a8d00f7f0602baca0d24c3a17d9f6
- 0x9142a699d088be61c993ace813829d3d25deac2d
- 0x0c3d6f479511F1AE5d8bee86E9e13965fB652157
- 0xf4729c2807fd0f4431004146ecfc4a47578aaeea --> smart contract.

## How to run and test the DAPP
There are two servlets that can be exercised by direct http requests as follows:
1.  http://host:port/EthereumDAPP_PoC/EtherTransferServlet?TargetAccount=0x9142A699d088be61C993Ace813829D3D25DeAc2d&TransferAmount=10&TransferUnit=WEI : transfers 10 WEIs from the sender account (which is the one used to initialize the Credentials, defined by an account password and a wallet file path) and a target account, passed as servlet request parameter.
2. http://host:port/EthereumDAPP_PoC/CoinManagerSmartContractServlet?OpType=TransferFund&TargetAccount=0x9142A699d088be61C993Ace813829D3D25DeAc2d&FundAmount=21 : this uses the CoinManager smart contract and deals with MyCoins. OpType can either be RaiseFund and TransferFund. The former can only makes sense if called by the smart contract owner and it's meant to create MyCoins out os thin air. The latter can then be used to transfer funds between accounts.

The above two servlets can also be exercised by a test client, ServletTestClient, which can programmatically connect to the servlets and send (multiple) requests.
Furthermore, CoinManagerTest can be used to unit-test the CoinManager.

## Development environment and tools
- Ubuntu 16.04.3 LTS.
- Eclipse Neon.
- JBoss Wildfly 10.1.

## Roadmap

1. Add a back end storage layer to the web application, possibly NoSQL.
2. Add a UI.
