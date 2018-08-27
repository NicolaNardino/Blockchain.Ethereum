package com.projects.blockchain.ethereum.poc.microservices.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.projects.blockchain.ethereum.poc.microservices.mongodb.util.EtherTransferEvent;
import com.projects.blockchain.ethereum.poc.microservices.mongodb.util.SmartContractEvent;

@RestController
@RequestMapping("/events")
public final class EventsRestController {
	
	@Autowired
	private SmartContractEventsRepository smartContractsRepository;
	@Autowired
	private EtherTransferEventsRepository etherTransferRepository;
	
	@RequestMapping(method = RequestMethod.GET, value = "/getEtherTransferEventBySourceAccount/{sourceAccount}", produces = "application/json")
	public List<EtherTransferEvent> getEtherTransferBySourceAccount(@PathVariable final String sourceAccount) {
		return etherTransferRepository.findBySourceAccount(sourceAccount);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getEtherTransferEvents", produces = "application/json")
	public List<EtherTransferEvent> getEtherTransferEvents() {
		return etherTransferRepository.findAll();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/addEtherTransferEvent")
	public void addEtherTransferEvent(@RequestBody final EtherTransferEvent etherTransferEvent) {
		etherTransferRepository.save(etherTransferEvent);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getSmartContractEvents", produces = "application/json")
	public List<SmartContractEvent> getSmartContractEvents() {
		return smartContractsRepository.findAll();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/addSmartContractEvent")
	public void addSmartContractEvent(@RequestBody final SmartContractEvent smartContractEvent) {
		smartContractsRepository.save(smartContractEvent);
	}
}