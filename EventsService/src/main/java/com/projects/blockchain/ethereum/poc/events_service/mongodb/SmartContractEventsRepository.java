package com.projects.blockchain.ethereum.poc.events_service.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.projects.blockchain.ethereum.utility.microservices.SmartContractEvent;

public interface SmartContractEventsRepository extends MongoRepository<SmartContractEvent, String> {

}
