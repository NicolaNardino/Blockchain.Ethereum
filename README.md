# Blockchain.Ethereum

A proof-of-concept decentralised application (DApp) in Java, backed by the Ethereum network. It transfers ether between accounts, deploys and calls Solidity smart contracts through [web3j](https://github.com/web3j/web3j), records blockchain and contract events in MongoDB, and exposes everything through Spring Boot microservices that run in Docker and on Kubernetes.

## Features

- Connection to Ethereum through a local `geth` node over JSON-RPC.
- Ether transfers between wallet accounts, with transaction monitoring.
- **CoinManager** smart contract (Solidity): mints its own coin and transfers it between accounts. It uses contract inheritance and accepts payments in wei through a payable fallback function.
- **DepositManager** smart contract: receives payments forwarded by CoinManager through a second payable function.
- Java servlets (deployed on WildFly) to transfer ether and exercise both contracts.
- JAX-RS RESTful services for the contracts, since superseded by Spring Boot microservices.
- MongoDB store for smart contract and Ethereum events.
- **EthereumService**: Spring Boot microservice for ether transfers and smart contract operations.
- **EventsService**: Spring Boot microservice exposing the events stored in MongoDB.
- Swagger API documentation.
- Containerisation with Docker and Docker Compose, plus a Kubernetes (Minikube) deployment.

## Architecture

```
            ┌──────────────────────┐      ┌──────────────────────┐
 HTTP ─────▶│ EthereumDAPP_PoC     │      │ EthereumService      │◀──── HTTP :9095
            │ (servlets, WildFly)  │      │ (Spring Boot)        │
            └──────────┬───────────┘      └──────────┬───────────┘
                       │ web3j JSON-RPC              │ web3j JSON-RPC
                       ▼                             ▼
                 ┌───────────────────────────────────────────┐
                 │ geth node (localhost:8545) ── Rinkeby     │
                 │   CoinManager.sol ──▶ DepositManager.sol  │
                 └───────────────────────────────────────────┘
                       │ events
                       ▼
            ┌──────────────────────┐      ┌──────────────────────┐
            │ MongoDB              │◀─────│ EventsService        │◀──── HTTP :9094
            │ EventsDatabase       │      │ (Spring Boot)        │
            └──────────────────────┘      └──────────────────────┘
```

## Project structure

| Module | Description |
|---|---|
| `SmartContracts` | Solidity sources (`CoinManager.sol`, `DepositManager.sol`), generated web3j Java wrappers and JUnit tests. |
| `EthereumDAPP_PoC` | Java EE web application: servlets, JAX-RS services, transaction monitoring context listener. |
| `EthereumService` | Spring Boot microservice for ether transfers, CoinManager and DepositManager. |
| `EthereumServiceClient` | Client for EthereumService. |
| `EventsService` | Spring Boot microservice over the MongoDB events store. |
| `EventsServiceClient` | Client for EventsService. |
| `Utility` | Shared code plus Dockerfiles, `docker-compose.yml` and Kubernetes manifests under `src/main/resources`. |

## Tech stack

- Java 8, Maven
- web3j 3.2, Solidity 0.4
- geth (Go Ethereum), Rinkeby testnet
- JBoss WildFly, JAX-RS (RESTEasy)
- Spring Boot 2.0, Spring Data MongoDB, Swagger
- MongoDB
- Docker, Docker Compose (images built with the fabric8 `docker-maven-plugin`), Kubernetes (Minikube)

## Local Ethereum setup

1. Run a full node on localhost with `geth`, enabling the HTTP-RPC server that web3j connects to:

   ```bash
   geth --rinkeby --rpc --rpcapi "personal,db,eth,net,web3"
   ```

2. Optionally, connect an Ethereum wallet UI to the node to experiment.
3. Create accounts from the `geth` console and fund them with test ether from a Rinkeby faucet.

The node was fully synchronised on both mainnet and testnet, but the application only ever connected to the testnet.

## Smart contracts

CoinManager is based on the well-known `Coin` example contract, extended with inheritance and an additional event. web3j deploys and calls contracts from the JVM through a Java wrapper generated from the compiled contract.

To regenerate the CoinManager wrapper, with the Solidity compiler (`solc`) and the web3j CLI installed:

```bash
cd SmartContracts/src/main/resources/contracts/coinManager
solc --overwrite CoinManager.sol --bin --abi --gas --optimize -o .
web3j solidity generate CoinManager.bin CoinManager.abi \
  -o <repo>/SmartContracts/src/main/java \
  -p com.projects.blockchain.ethereum.smart_contracts
```

The `--gas` option prints a rough estimate of the gas needed to create the contract and call its functions.

`CoinManagerTest` exercises the contract's features.

### Interaction between contracts

CoinManager calls DepositManager through a `DepositManager` interface declared in `CoinManager.sol`. The interface name itself is irrelevant: what matters is the DepositManager address used at run time. That address is passed to the CoinManager constructor on deployment. An alternative would be to pass the address on each call, as an extra parameter to `CoinManager.sendToDepositManager`, and cast it to `DepositManager`.

### Test accounts

Three externally owned accounts were set up on Rinkeby:

- `0x99fedc28c33a8d00f7f0602baca0d24c3a17d9f6`
- `0x9142a699d088be61c993ace813829d3d25deac2d`
- `0x0c3d6f479511F1AE5d8bee86E9e13965fB652157`

Contract addresses change on every redeployment. The last deployments were CoinManager at `0x55e7b3536cf2bf4c2610464f0fd163de4c4b673d` and DepositManager at `0xe9790e2fda7f591d5cea4f85aeb56c885f981550`.

## Running the DApp servlets

Deploy `EthereumDAPP_PoC` to WildFly, then call the servlets directly:

1. **Ether transfer.** Sends 10 wei from the sender account (the one whose credentials, a password and wallet file, initialise the application) to the target account:

   ```
   http://host:port/EthereumDAPP_PoC/EtherTransferServlet?TargetAccount=0x9142A699d088be61C993Ace813829D3D25DeAc2d&TransferAmount=10&TransferUnit=WEI
   ```

2. **CoinManager.** `OpType` is `RaiseFund` or `TransferFund`. `RaiseFund` mints new coins and only works for the contract owner; `TransferFund` moves coins between accounts:

   ```
   http://host:port/EthereumDAPP_PoC/CoinManagerSmartContractServlet?OpType=TransferFund&TargetAccount=0x9142A699d088be61C993Ace813829D3D25DeAc2d&FundAmount=21
   ```

3. **DepositManager**, through a RESTful service. `OpType` is `Deposit` (deposit wei for an account) or `DepositAccountBalance` (read an account's balance):

   ```
   http://host:port/EthereumDAPP_PoC/DepositManagerSmartContractServlet?OpType=Deposit&TargetAccount=0x9142A699d088be61C993Ace813829D3D25DeAc2d&Amount=23
   ```

Helpers:

- `ServletTestClient` sends one or many requests to the servlets programmatically.
- `MongoDBEventsRetriever` reads the contract and Ethereum events that `TransactionMonitoringContextListener` stores.

## Microservices

### EthereumService (port 9095)

| Endpoint | Description |
|---|---|
| `GET /ethererum/getInfo` | Node and client information |
| `GET /ethererum/transfer` | Transfer ether |
| `GET /ethererum/coin_manager/raiseFund`, `/transferFund` | Mint and transfer coins |
| `GET /ethererum/coin_manager/getOwner`, `/getBalance`, `/getAccountBalance/{account}` | CoinManager queries |
| `PUT /ethererum/coin_manager/deposit` | Forward a deposit to DepositManager |
| `GET /ethererum/deposit_manager/getOwner`, `/getBalance`, `/getAccountBalance/{account}` | DepositManager queries |
| `GET /ethererum/healthcheck` | Health check |

The `ethererum` spelling in the paths is as implemented.

Configure `node.url`, `account.password` and `wallet.file.path` in `EthereumService/src/main/resources/application.properties` before running.

### EventsService (port 9094)

| Endpoint | Description |
|---|---|
| `GET /events/getEtherTransferEvents` | All ether transfer events (JSON or XML) |
| `GET /events/getEtherTransferEventBySourceAccount/{sourceAccount}` | Transfers from an account |
| `POST /events/addEtherTransferEvent`, `/addEtherTransferEvents` | Store transfer events |
| `GET /events/getSmartContractEvents` | All smart contract events (JSON or XML) |
| `POST /events/addSmartContractEvent`, `/addSmartContractEvents` | Store contract events |
| `GET /events/healthcheck` | Health check |

### Running outside a container

With Maven:

```bash
cd EventsService
mvn clean package spring-boot:run -Dserver.port=9094
```

As an executable jar (the port comes from `application.properties`):

```bash
cd EventsService
mvn clean package
java -jar target/EventsService-1.0.jar
```

EthereumService works the same way.

## Docker

Images are built from Dockerfiles generated by the [fabric8 docker-maven-plugin](https://github.com/fabric8io/docker-maven-plugin), so the Spring Boot jar name is never hard-coded:

```bash
mvn clean package docker:build   # build the image
mvn clean package docker:run     # run it in a container
```

For example, EthereumService 1.0 generates:

```dockerfile
FROM nicolanardino/ubuntu-openjdk-8:1.0
MAINTAINER Nicola Nardino, https://github.com/NicolaNardino
COPY maven /deployments/
RUN apt-get update && apt-get install -y curl && apt-get clean
HEALTHCHECK --interval=15s --timeout=10s --retries=3 CMD curl -f http://localhost:9095/ethereum/healthcheck || exit 1
CMD java -jar deployments/ethereum_service-1.0.jar
```

The base images, [`nicolanardino/ubuntu-openjdk-8`](https://hub.docker.com/r/nicolanardino/ubuntu-openjdk-8) and the earlier `nicolanardino/ubuntu-oracle-java-8`, are Ubuntu LTS with Java 8 and `curl`, which the container health check needs. Their Dockerfiles are in `Utility/src/main/resources/docker`.

### Host network and volumes

The services reach MongoDB and the `geth` node on localhost, so their containers use the host network. With host networking no port mapping is needed. Network mode and volumes can be set in the `<run>` section of the plugin configuration, separately from the image build:

```xml
<run>
  <network>
    <mode>host</mode>
  </network>
  <volumes>
    <bind>
      <volume>/home/main/.ethereum/rinkeby/keystore:/home/main/.ethereum/rinkeby/keystore</volume>
    </bind>
  </volumes>
</run>
```

The keystore volume gives EthereumService access to the wallet. Without host networking, ports would need mapping explicitly (`9095:9095` for the service, `8545:8545` for the node).

MongoDB can run in its own container, with a volume so data survives container restarts:

```bash
docker run --network=host --name my-mongo-container -d -v ~/data/docker/mongodb:/data/db mongo
docker run -it --name events-service --network=host nicolanardino/events_service:1.0
```

Sharing the host network replaces the deprecated `--link` option.

## Docker Compose

[`Utility/src/main/resources/docker/compose/docker-compose.yml`](Utility/src/main/resources/docker/compose/docker-compose.yml) runs MongoDB, EventsService and EthereumService together. They communicate over the host network, and `depends_on` starts MongoDB before EventsService:

```yaml
version: '3.4'

services:
  mongodb:
    image: mongo:latest
    container_name: my-mongo-container
    restart: always
    volumes:
      - ~/data/docker/mongodb:/data/db
    network_mode: "host"
  eventsService:
    image: nicolanardino/events_service:1.0
    container_name: events-service
    depends_on:
      - mongodb
    restart: always
    network_mode: "host"
  ethereumService:
    image: nicolanardino/ethereum_service:1.0
    container_name: ethereum-service
    restart: always
    volumes:
      - /home/main/.ethereum/rinkeby/keystore:/home/main/.ethereum/rinkeby/keystore
    network_mode: "host"
```

## Kubernetes

EthereumService also runs on a local Kubernetes cluster with [Minikube](https://minikube.sigs.k8s.io/) on VirtualBox, deployed with [`kubectl`](https://kubernetes.io/docs/tasks/tools/). The manifests are in `Utility/src/main/resources/kubernetes`.

```bash
minikube start --vm-driver=virtualbox
kubectl create -f service.yml
kubectl create -f deployment.yml
```

`service.yml` exposes the service as a `NodePort`, reachable outside the cluster on a port the control plane allocates from 30000–32767:

```yaml
apiVersion: v1
kind: Service
metadata:
  name: ethereum-service
  labels:
    app: ethereum-service
    tier: ethereum
spec:
  type: NodePort
  ports:
    - port: 9095
  selector:
    app: ethereum-service
    tier: ethereum
```

`deployment.yml`:

```yaml
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: ethereum-service
spec:
  replicas: 1
  template:
    metadata:
      labels:
        app: ethereum-service
        tier: ethereum
    spec:
      hostNetwork: true
      containers:
      - name: ethereum-service
        image: nicolanardino/ethereum_service:1.0
        imagePullPolicy: IfNotPresent
        volumeMounts:
        - mountPath: /home/main/.ethereum/rinkeby/keystore
          name: keystore
        resources:
          requests:
            cpu: 250m
            memory: 128Mi
        env:
        - name: GET_HOSTS_FROM
          value: dns
        ports:
        - containerPort: 9095
      volumes:
      - name: keystore
        hostPath:
          path: /home/main/.ethereum/rinkeby/keystore
          type: Directory
```

Key points:

- `imagePullPolicy: IfNotPresent` uses a local image if present, otherwise pulls [`nicolanardino/ethereum_service`](https://hub.docker.com/r/nicolanardino/ethereum_service/) from Docker Hub.
- `hostNetwork: true` has the same meaning as Docker's host network and is needed to reach the `geth` node on the host.
- The `hostPath` volume gives the pod access to the wallet keystore.
- CPU and memory requests size the pod.

Useful commands:

```bash
minikube ip
minikube service ethereum-service --url     # external service URL
kubectl get services|nodes|deployments|pods
kubectl describe service ethereum-service
kubectl describe deployment ethereum-service
kubectl logs <pod-name>
kubectl exec -it <pod-name> -- /bin/bash    # shell into the container
kubectl delete deployment ethereum-service
```

## Development environment

- Ubuntu, Eclipse
- JBoss WildFly, Spring Boot
- Docker, Docker Compose, Kubernetes (Minikube)

## Roadmap

1. Run the Kubernetes cluster on AWS.
2. Add a GUI to transfer ether and exercise CoinManager and DepositManager.

## Status

This proof of concept dates from 2018 and is no longer maintained. Several parts of its environment no longer exist or have changed:

- The **Rinkeby testnet was shut down in 2023**. A revival would target Sepolia (or a local chain such as Anvil or Hardhat).
- `geth` replaced the `--rpc` / `--rpcapi` flags with `--http` / `--http.api`, and Ethereum moved to proof of stake, so a node now also needs a consensus client.
- The contracts use Solidity 0.4, and web3j, Spring Boot 2.0 and Java 8 are all far behind current versions.
- The Kubernetes `extensions/v1beta1` Deployment API was removed in Kubernetes 1.16; use `apps/v1` (which also requires a `selector`).
- Known issue: the EthereumService container health check calls `/ethereum/healthcheck`, while the controller is mapped to `/ethererum/healthcheck`, so the container reports unhealthy. One of the two paths in `EthereumService/pom.xml` or `EthereumRestController` needs aligning.

## License

See [LICENSE](LICENSE).
