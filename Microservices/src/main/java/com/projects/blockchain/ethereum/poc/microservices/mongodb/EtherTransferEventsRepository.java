package com.projects.blockchain.ethereum.poc.microservices.mongodb;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.projects.blockchain.ethereum.utility.microservices.EtherTransferEvent;

public interface EtherTransferEventsRepository extends MongoRepository<EtherTransferEvent, String> {

	public List<EtherTransferEvent> findBySourceAccount(String sourceAccount);
}
