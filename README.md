# Welcome to Blockchain.Ethereum DApp PoC
Java web application backed by the Ethereum network.

Key features:
  - Connection to the Ethereum Network via a node running on localhost.
  - Ethers transfer between wallet accounts.
  - Transaction monitoring.
  - CoinManager Smart Contract, able to create/ mint its own coin and transfer them between accounts. Developed in Solidity, it also uses inheritance.
  - CoinManager is able to receive payments (WEIs) by the means of a fallback payable function.
  - DepositManger Smart Contract, able to receive payments from CoinManager. For this purpose, a second payable function has been added to CoinManager.
  - Java servlets to transfer ethers and exercise CoinManager and DepositManager.
  - JAX-RS RESTful web service exposing CoinManager/ DepositManager features.
  - MongoDB data store for Smart Contract and Ethereum events.
  - Spring Boot Microservices, exposing the MongoDB events. Swagger for API documentation.
  - Microservices containerization with Docker.

I've set up my Ethereum local environment as follows:
  - Run a full node by geth on the localhost and created two accounts: geth --rpcapi "personal,db,eth,net,web3" --rpc --rinkeby). Most notable here is the --rpc option by which it gets enabled the HTTP-RPC server on the node, then used by web3j to establish the connection from the web application.
  - For the sake of experimenting, I've installed an ethereum wallet UI, which I then connected to the above node.
  - Created two accounts by the geth command line and got them some Ethers on the Rinkeby testnet, https://faucet.rinkeby.io/. 
  - Fully synchronized on both mainnet and testnet but connected the web application to the testnet only.
  
Web3j is used to interact with the Ethereum Network.
I've added a smart contract, based on a well-known contract, named Coin and extended it by adding inheritance and another event. By Web3j one can deploy and interact with smart contracts within the JVM once a Java wrapper in created from the Solidity bytecode.
Given the CoinManager.sol contract in the resources folder, with the following steps one creates a Java wrapper:
```unix
    solc --overwrite CoinManager.sol --bin --abi --gas --optimize -o . 
    web3j solidity generate <your_path>/SmartContracts/src/main/resources/contracts/coinManager/CoinManager.bin    <your_path>/SmartContracts/src/main/resources/contracts/coinManager/CoinManager.abi -o <your_path>/SmartContracts/src/main/java -p com.projects.blockchain.ethereum.smart_contracts
```
Please, pay attention to the --gas option, which will let you know a rough estimate gas required to create the contract and transact with its methods.
The above requires to have the Solidity compiler (solc) and the Web3j command line tools.
A JUnit test class is available to exercise the various smart contract features.

At the moment, I've set up three externally controlled accounts and one smart contract:

- 0x99fedc28c33a8d00f7f0602baca0d24c3a17d9f6
- 0x9142a699d088be61c993ace813829d3d25deac2d
- 0x0c3d6f479511F1AE5d8bee86E9e13965fB652157
- Smart contract addresses change at every re-deployment. Currently, CoinManager is at 0x55e7b3536cf2bf4c2610464f0fd163de4c4b673d and DepositManager at 0xe9790e2fda7f591d5cea4f85aeb56c885f981550.

## Interactions between Smart Contracts
CoinManager interacts with DepositManager by a DepositManager interface in CoinManager(.sol). Actually, the DepositManager type name in CoinManager doesn't matter, because what it counts is the address of DepositManager that will be used at run-time. This link is established at CoinManager creation time, i.e., the DepositManager address gets passed to CoinManager constructor. Alternatively, one could think of passing the address of DepositManager at run-time by adding an extra parameter to CoinManager.sendToDepositManager and casting it to DepositManager.

## How to run and test the DApp
There are two servlets that can be exercised by direct http requests as follows:
1.  http://host:port/EthereumDAPP_PoC/EtherTransferServlet?TargetAccount=0x9142A699d088be61C993Ace813829D3D25DeAc2d&TransferAmount=10&TransferUnit=WEI: transfers 10 WEIs from the sender account (which is the one used to initialize the Credentials, defined by an account password and a wallet file path) and a target account, passed as servlet request parameter.
2. http://host:port/EthereumDAPP_PoC/CoinManagerSmartContractServlet?OpType=TransferFund&TargetAccount=0x9142A699d088be61C993Ace813829D3D25DeAc2d&FundAmount=21: this uses the CoinManager smart contract and deals with MyCoins. OpType can either be RaiseFund and TransferFund. The former can only makes sense if called by the smart contract owner and it's meant to create MyCoins out os thin air. The latter can then be used to transfer funds between accounts.
3. http://localhost:8080/EthereumDAPP_PoC/DepositManagerSmartContractServlet?OpType=Deposit&TargetAccount=0x9142A699d088be61C993Ace813829D3D25DeAc2d&Amount=23: exercises DepositManager by calling a RESTful web service. OpType can be eitther Deposit (deposits some WEIs to an account within the DepositManager) or DepositAccountBalance (gets the balance of an account within DepositManager). 

The above two servlets can also be exercised by a test client, ServletTestClient, which can programmatically connect to the servlets and send (multiple) requests.
CoinManagerTest can be used to unit-test the CoinManager.
MongoDBEventsRetriever can be used to monitor the Smart Contract and Ethereum events stored by TransactionMonitoringContextListener.

## How to run the Spring Boot Microservices outside a container
### With Maven
```
  cd EventsService
  mvn clean package spring-boot:run -Dserver.port=9094
```
### Without Maven
Maven is only used to build an executable jar.
```
  cd Microservices
  mvn clean package
  java -jar target/Microservices-1.0.jar --> port set in application.properties.
```
## Microservices Containerization with Docker
This is done my building a Docker image with a Dockerfile generated by the [fabric8io](https://github.com/fabric8io/docker-maven-plugin) maven plugin:
```
  mvn clean package docker:build --> builds the image.
  mvn clean package docker:run --> instantiates the image in a container.
```
This way the generated executable Sprig Boot jar isn't hardcoded. For instance, version 1.0 generates the following Dockerfile:
```
  FROM nicolanardino/ubuntu-oracle-java-8:latest
  MAINTAINER Nicola Nardino, https://github.com/NicolaNardino
  COPY maven /deployments/
  RUN apt-get update && apt-get install -y curl && apt-get clean
  HEALTHCHECK --interval=15s --timeout=10s --retries=3 CMD curl -f http://localhost:9094/events/healthcheck || exit 1
  CMD java -jar deployments/events_service-1.0.jar
```
The above Dockerfile derives from [nicolanardino/ubuntu-oracle-java-8:latest](https://hub.docker.com/r/nicolanardino/ubuntu-oracle-java-8/), which is an image starting from ubuntu:latest LTS with Oracle Java 8, stored on my DockerHub account. That was necessary because of the container healthcheck, which required I've implemented, the easiest way, with curl.

### MongoDB access from the containerized EventsService
Given that the events service requires MongoDB, if that doesn't run in a container too, the above run doesn't work. In that case, one would have to run the container with the host network, as follows:
```
docker run -it --name events-service --network=host nicolanardino/events_service:1.0
```
The ideal solution would be to have MongoDB running in its own container, as follows:
```
docker run --network=host --name my-mongo-container -d -v ~/data/docker/mongodb:/data/db -p 27017:27017 mongo
```
In order not to loose data store in the MongoDB EventsDatabase when the MongoDB container gets shut down, I've used a volume to map the localhost data repository ~/data/docker/mongodb to the MongoDB container one, /data/bin.

The linking between the two containers is done by sharing the host network. The now-deprecated solution would have been to use the --link option, so to link a source to a destination container, as follows:
```
docker run --link=my-mongo-container --name events-service -it --network=host nicolanardino/events_service:1.0
```
Another way of building a multi-container application, so also providing the linking between containers, is through [Docker Compose](https://docs.docker.com/compose/overview/).

### Docker Compose
Below docker-compose.yml file links the containers "links" attribute, but it could be done through the networks attributes too.

```
version: '3.0'

services:
   mongodb:
        image: mongo:latest
        container_name: my-mongo-container
        restart: always
        volumes:
          - ~/data/docker/mongodb:/data/db
        ports:
            - 27017:27017
   eventsService:
     image: nicolanardino/events_service:1.0
     container_name: events-service
     depends_on:
       - mongodb
     links:
       - mongodb
     restart: always
     ports:
       - "9094:9094"
```

## Development environment and tools
- Ubuntu. 
- Eclipse. 
- JBoss Wildfly. 
- Spring Boot. 
- Docker. 

## Roadmap

1. Add a back end storage layer to the web application, possibly NoSQL. --> done
2. Containerize the Spring Boot Microservices by Docker. --> done
3. Add container orchestration by Kubernetes.
4. A small GUI allowing to transfer Ethers abd exercise CoinManager and DepositManager.
5. Javadoc.

