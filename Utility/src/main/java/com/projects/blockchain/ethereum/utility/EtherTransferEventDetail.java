package com.projects.blockchain.ethereum.utility;

import java.math.BigInteger;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public final class EtherTransferEventDetail extends EventDetail {
	private final String txHash;
	private final BigInteger gas;
	private final BigInteger gasPrice;
	
	@JsonCreator
	public EtherTransferEventDetail(@JsonProperty("txHash") final String txHash, @JsonProperty("gas") final BigInteger gas, @JsonProperty("gasPrice") final BigInteger gasPrice, 
			@JsonProperty("sourceAccount") final String sourceAccount, @JsonProperty("targetAccount") final String targetAccount, @JsonProperty("sourceAccountBalance") final BigInteger sourceAccountBalance, 
			@JsonProperty("targetAccountBalance") final BigInteger targetAccountBalance, @JsonProperty("amount") final int amount, @JsonProperty("eventDate") final Date eventDate) {
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
