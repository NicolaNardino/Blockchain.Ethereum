package com.projects.blockchain.ethereum.poc.microservices.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.projects.blockchain.ethereum.utility.microservices.EtherTransferEvent;
import com.projects.blockchain.ethereum.utility.microservices.SmartContractEvent;

@RestController
@RequestMapping("/events")
public final class EventsRestController {
	
	@Autowired
	private SmartContractEventsRepository smartContractsRepository;
	@Autowired
	private EtherTransferEventsRepository etherTransferRepository;
	
	@RequestMapping(method = RequestMethod.GET, value = "/getEtherTransferEventBySourceAccount/{sourceAccount}", 
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public List<EtherTransferEvent> getEtherTransferBySourceAccount(@PathVariable final String sourceAccount) {
		return etherTransferRepository.findBySourceAccount(sourceAccount);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getEtherTransferEvents", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public List<EtherTransferEvent> getEtherTransferEvents() {
		return etherTransferRepository.findAll();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/addEtherTransferEvent")
	public void addEtherTransferEvent(@RequestBody final EtherTransferEvent etherTransferEvent) {
		etherTransferRepository.save(etherTransferEvent);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getSmartContractEvents", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public List<SmartContractEvent> getSmartContractEvents() {
		return smartContractsRepository.findAll();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/addSmartContractEvent")
	public void addSmartContractEvent(@RequestBody final SmartContractEvent smartContractEvent) {
		smartContractsRepository.save(smartContractEvent);
	}
}