package com.projects.blockchain.ethereum.poc.microservices.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.projects.blockchain.ethereum.poc.microservices.mongodb.util.SmartContractEvent;

public interface SmartContractEventsRepository extends MongoRepository<SmartContractEvent, String> {

}
