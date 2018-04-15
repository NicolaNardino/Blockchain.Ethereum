package com.projects.blockchain.ethereum.utility;

public enum SmartContractName {
	CoinManager("0xea69de4c779d0d2f5945e9a3d1d677ac5e403b2c"), DepositManager("0xe9790e2fda7f591d5cea4f85aeb56c885f981550");
	
	private final String address;
	private SmartContractName(final String address) {
		this.address = address;
	}
	
	public String getAddress() {
		return address;
	}
}