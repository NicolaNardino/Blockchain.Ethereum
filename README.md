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
  - JAX-RS RESTful web services exposing CoinManager/ DepositManager features. Those are being replaced by Spring Boot Microservices (EthererumService and EthererumServiceClient projects).
  - MongoDB data store for Smart Contract and Ethereum events.
  - Spring Boot Microservices for interacting with Ethereum in the context of ETHER transfers and smart contracts handling. 
  - Spring Boot Microservices exposing the MongoDB events. 
  - Swagger for API documentation.
  - Microservices (MongoDB events and Ethererum services) containerization with Docker(-Compose).

I've set up my Ethereum local environment as follows:
  - Run a full node by geth on the localhost: ```geth --rpcapi "personal,db,eth,net,web3" --rpc --rinkeby```. 
  The --rpc option enables the HTTP-RPC server on the node, then used by web3j to establish the connection from the web application.
  - For the sake of experimenting, I've installed an ethereum wallet UI, which I then connected to the above node.
  - Created three accounts by the geth command line and got them some Ethers on the Rinkeby testnet, https://faucet.rinkeby.io/. 
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
3. http://localhost:port/EthereumDAPP_PoC/DepositManagerSmartContractServlet?OpType=Deposit&TargetAccount=0x9142A699d088be61C993Ace813829D3D25DeAc2d&Amount=23: exercises DepositManager by calling a RESTful web service. OpType can be eitther Deposit (deposits some WEIs to an account within the DepositManager) or DepositAccountBalance (gets the balance of an account within DepositManager). 

The above two servlets can also be exercised by a test client, ServletTestClient, which can programmatically connect to the servlets and send (multiple) requests.
CoinManagerTest can be used to unit-test the CoinManager.
MongoDBEventsRetriever can be used to monitor the Smart Contract and Ethereum events stored by TransactionMonitoringContextListener.

## Events Service Spring Boot Microservice
### Run outside of a container
#### With Maven
```
  cd EventsService
  mvn clean package spring-boot:run -Dserver.port=9094
```
#### Without Maven
Maven is only used to build an executable jar.
```
  cd EventsService
  mvn clean package
  java -jar target/EventsService-1.0.jar --> port set in application.properties.
```
### Docker Containerization
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

#### MongoDB access from the containerized EventsService
Given that the events service requires MongoDB, if that doesn't run in a container as well, the above run doesn't work. In that case, one would have to run the container with the host network, as follows:
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
In order to build a multi-container application, also providing the linking between containers, is through [Docker Compose](https://docs.docker.com/compose/overview/) (see below).

## Ethererum Service Spring Boot Microservice
I'll skip some general topics already covered for the EventsService an focus on some others.
Instead of the Oracle JDK 8, I've used OpenJDK 8 [nicolanardino/ubuntu-openjdk-8:1.0](https://hub.docker.com/r/nicolanardino/ubuntu-openjdk-8), which I've then used in EventsService too.
Host mode and volumes mapping can be specified in the pom.xml within the run tag, separately from the image build section:
```
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
No need to specify any port mapping because that is an implicit feature of the host network, otherwise the following would be needed:
 ```
<run>
...
   <ports> 
      <port>9095:9095</port> <!--service port-->
      <port>8045:8045</port> <!--Ethererum node-->
    </ports>
 </run>
  ```
 The actual ports are picked up through the Spring Boot application.properties files.
 
 Maver generated Dockerfile:
 ```
 FROM nicolanardino/ubuntu-openjdk-8:1.0
 MAINTAINER Nicola Nardino, https://github.com/NicolaNardino
 COPY maven /deployments/
 RUN apt-get update && apt-get install -y curl && apt-get clean
 HEALTHCHECK --interval=15s --timeout=10s --retries=3 CMD curl -f http://localhost:9095/ethereum/healthcheck || exit 1
 CMD java -jar deployments/ethereum_service-1.0.jar
 ```

## Docker Compose
Below docker-compose.yml file is the way to build a multi-container docker application. The link between mongodb and eventsService is done through the host network, while the start-up order is specified by the depends_on attribute in eventsService.
Note the volume mapping in ethererumService needed to let the container have access to the Ethererum wallet account. 

```
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

Same as discussed above about the ports mapping: it's not needed as long as the network_mode attribute is set to host. Other it'd have been:

mongodb:
```
ports:
       - "27017:27017"
```

eventsService:
```
ports:
       - "9094:9094"
```    
ethererumService:
```
ports:
       - "9095:9095"
```

## Kubernetes Ethereum Service set-up
In order to run a Kubernetes cluster in my Ubuntu machine, I've installed and configured [Minikube](https://kubernetes.io/docs/setup/minikube/) and let it run within the default virtual machine, [virtualbox](https://www.virtualbox.org/wiki/Downloads).
[Kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/) and other tools would have to be installed as well, but I won't go through the whole (painful) process of setting the environment up. There are plenty of books and/ or on-line resources on that.

```
# Start Minikube cluster
minikube start --vm-driver=virtualbox #default value.
# Service configuration
kubectl create -f service.yml
# Pod deployment
kubectl create -f deployment.yml
```
Kubernetes *.yml are in Utility/src/main/resources.

Service.yml:

```
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

Where "type: NodePort" allows to expose the service outside the cluster through a port allocated by the Kubernetes master from a given range of ports (default: 30000-32767).

Deployment.yml:
```
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
Key points in the above deployment:
  - "imagePullPolicy: IfNotPresent": pulls nicolanardino/ethereum_service:1.0 from DockerHub or, if available, from the local file system. For the sake of completeness, I've uploaded [ethereum-service](https://hub.docker.com/r/nicolanardino/ethereum_service/) on my DockerHub account.
  - "hostNetwork: true": it's the same meaning of using the host network in Docker, i.e., needed to access services running on the localhost, which is the case of the Ethereum node.
  - Volume set-up needed to access the Wallet keystore.
  - CPU and Memory limits.
  
A few useful commands:
```
minikube ip
# Get external service URL.
minikube service ethereum-service --url
kubectl get services
kubectl get nodes
kubectl describe service ethereum-service
kubectl get deployments
kubectl describe deployment ethereum-service
kubectl get pods
kubectl delete deployment ethereum-service
# Given Pod name = ethereum-service-6f494c8d9b-vvhrq
# Get logs.
kubectl logs ethereum-service-8465b65f5b-5rvgq
# Open a shell to the running container.
kubectl exec -it ethereum-service-6f494c8d9b-vvhrq -- /bin/bash
```
For instance, the Pod:
```
main@Terminator:~/Projects/BlockchainWorkspace$ kubectl get pods
NAME                                READY     STATUS    RESTARTS   AGE
ethereum-service-6f494c8d9b-vvhrq   1/1       Running   0          1h

main@Terminator:~/Projects/BlockchainWorkspace$ sudo kubectl describe pod ethereum-service-6f494c8d9b-vvhrq
Name:		ethereum-service-6f494c8d9b-vvhrq
Namespace:	default
Node:		minikube/192.168.0.19
Start Time:	Sun, 04 Nov 2018 17:00:15 +0100
Labels:		app=ethereum-service
		pod-template-hash=2905074856
		tier=ethereum-service
Status:		Running
IP:		192.168.0.19
Controllers:	<none>
Containers:
  ethereum-service:
    Container ID:	docker://8497424b578fa870ac19be7fc0bd09a3b329cbea286bc5bb8359b814ed8fa066
    Image:		nicolanardino/ethereum_service:1.0
    Image ID:		docker-pullable://nicolanardino/ethereum_service@sha256:17dd4cfc09e920eb6bfc313c4732d80d28dcfb4c58304d78ce2accedc3ee4731
    Port:		9095/TCP
    Requests:
      cpu:		250m
      memory:		128Mi
    State:		Running
      Started:		Sun, 04 Nov 2018 17:00:24 +0100
    Ready:		True
    Restart Count:	0
    Volume Mounts:
      /home/main/.ethereum/rinkeby/keystore from keystore (rw)
      /var/run/secrets/kubernetes.io/serviceaccount from default-token-9jrj9 (ro)
    Environment Variables:
      GET_HOSTS_FROM:	dns
Conditions:
  Type		Status
  Initialized 	True 
  Ready 	True 
  PodScheduled 	True 
Volumes:
  keystore:
    Type:	HostPath (bare host directory volume)
    Path:	/home/main/.ethereum/rinkeby/keystore
  default-token-9jrj9:
    Type:	Secret (a volume populated by a Secret)
    SecretName:	default-token-9jrj9
QoS Class:	Burstable
Tolerations:	<none>
Events:
  FirstSeen	LastSeen	Count	From			SubObjectPath				Type		Reason			Message
  ---------	--------	-----	----			-------------				--------	------			-------
  1h		1h		1	{default-scheduler }						Normal		Scheduled		Successfully assigned ethereum-service-6f494c8d9b-vvhrq to minikube
  1h		1h		1	{kubelet minikube}						Normal		SuccessfulMountVolume	MountVolume.SetUp succeeded for volume "keystore" 
  1h		1h		1	{kubelet minikube}						Normal		SuccessfulMountVolume	MountVolume.SetUp succeeded for volume "default-token-9jrj9" 
  1h		1h		1	{kubelet minikube}	spec.containers{ethereum-service}	Normal		Pulled			Container image "nicolanardino/ethereum_service:1.0" already present on machine
  1h		1h		1	{kubelet minikube}	spec.containers{ethereum-service}	Normal		Created			Created container
  1h		1h		1	{kubelet minikube}	spec.containers{ethereum-service}	Normal		Started			Started container

```

## Development environment and tools
- Ubuntu. 
- Eclipse. 
- JBoss Wildfly. 
- Spring Boot. 
- Docker and Docker-Compose.
- Kubernetes.

## Roadmap

1. Set up a Kubernetes cluster in AWS cloud.
2. Add a GUI allowing to transfer Ethers and exercise CoinManager and DepositManager.

