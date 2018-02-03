# Welcome to Blockchain.Ethereum Web Application PoC
Simple Java web application interacting with the Ethereum network, meant to become a DAPP as soon as I'll introduce smart contracts.

For the time being its key features are:
  - Connection to the Ethereum Network via a node running on localhost.
  - Transfer funds between wallet accounts.
  - Transaction monitoring.

I've set up my Ethereum local environment as follows:
  - Run a full node by geth on the localhost and created two accounts: geth --rpcapi "personal,db,eth,net,web3" --rpc --rinkeby). Most notable here is the --rpc option by which it gets enabled the HTTP-RPC server on the node, then used by web3j to establish the connection from the web application.
  - For the sake of experimenting, I've installed an ethereum wallet UI, which I then connected to the above node.
  - Created two accounts by the geth command line and got them some Ethers on the Rinkeby testnet, https://faucet.rinkeby.io/. 
  - Fully synchronized on both mainnet and testnet but connected the web application to the testnet only.
  
Web3j is used to interact with the Ethereum Network.

## Development environment and tools
- Ubuntu 16.04.3 LTS.
- Eclipse Neon.
- JBoss Wildfly 10.1.

## Roadmap

1. Add a back end storage layer to the web application, possibly NoSQL.
2. Add smart contracts.
