package com.projects.blockchain.ethereum.utility;

public enum SmartContractName {
	CoinManager("0x55e7b3536cf2bf4c2610464f0fd163de4c4b673d"), DepositManager("0xe9790e2fda7f591d5cea4f85aeb56c885f981550");
	
	private final String address;
	private SmartContractName(final String address) {
		this.address = address;
	}
	
	public String getAddress() {
		return address;
	}
}