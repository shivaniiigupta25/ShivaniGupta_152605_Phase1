package com.cg.paymentwalletapplication.dto;

import java.util.ArrayList;

public class Wallet{
	private double balance;
	private ArrayList<String> transactions;
	
	public Wallet() {
		
	}

	public Wallet(double balance) {
		this.balance = balance;
	}

	public double getBalance() {
		return balance;
	}

	public ArrayList<String> getTransactions() {
		return transactions;
	}

	public void setTransactions(ArrayList<String> transactions) {
		this.transactions = transactions;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "Wallet [balance=" + balance + ", transactions=" + transactions + "]";
	}
}
