package com.projects.blockchain.ethereum.utility;

import java.math.BigInteger;
import java.util.Date;

public final class EtherTransferEventDetail extends EventDetail {
	private final String txHash;
	private final BigInteger gas;
	private final BigInteger gasPrice;
	
	public EtherTransferEventDetail(final String txHash, final BigInteger gas, final BigInteger gasPrice, 
			final String sourceAccount, final String targetAccount, final BigInteger sourceAccountBalance, final BigInteger targetAccountBalance, 
			final int amount, final Date eventDate) {
		super(sourceAccount, targetAccount, amount, eventDate, sourceAccountBalance, targetAccountBalance);
		this.txHash = txHash;
		this.gas = gas;
		this.gasPrice = gas;
	}

	public String getTxHash() {
		return txHash;
	}

	public BigInteger getGas() {
		return gas;
	}

	public BigInteger getGasPrice() {
		return gasPrice;
	}

	@Override
	public String toString() {
		return "EtherTransferEventDetail [txHash=" + txHash + ", gas=" + gas + ", gasPrice=" + gasPrice
				+ ", " + super.toString() + "]";
	}
}
