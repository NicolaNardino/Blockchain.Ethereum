package com.projects.blockchain.ethereum.utility;

public enum SmartContractName {
	CoinManager("0x418e027dead601abd75eae1ddad598a94dfff42f"), DepositManager("0x2e645c1c57150e532bcc861dd3576762ed48a86e");
	
	private final String address;
	private SmartContractName(final String address) {
		this.address = address;
	}
	
	public String getAddress() {
		return address;
	}
}