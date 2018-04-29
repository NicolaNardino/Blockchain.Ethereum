package com.projects.blockchain.ethereum.restful;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DepositData", propOrder = {"targetAccount", "amount"})
@XmlRootElement(name="DepositData")
public class DepositData {

	private String targetAccount;
	private BigInteger amount;
	
	public DepositData() {	
	}

	public DepositData(String targetAccount, BigInteger amount) {
		this.targetAccount = targetAccount;
		this.amount = amount;
	}
	
	public String getTargetAccount() {
		return targetAccount;
	}

	public BigInteger getAmount() {
		return amount;
	}

	@Override
	public String toString() {
		return String.format("DepositData [targetAccount=%s, amount=%s]", targetAccount, amount);
	}
}
