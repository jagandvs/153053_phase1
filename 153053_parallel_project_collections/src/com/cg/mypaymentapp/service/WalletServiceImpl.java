package com.cg.mypaymentapp.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Scanner;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Wallet;
import com.cg.mypaymentapp.exception.InsufficientBalanceException;
import com.cg.mypaymentapp.exception.InvalidInputException;
import com.cg.mypaymentapp.repo.WalletRepo;
import com.cg.mypaymentapp.repo.WalletRepoImpl;

public class WalletServiceImpl implements WalletService 
{


public WalletRepo repo;
WalletRepoImpl obj=new WalletRepoImpl();
	
	public WalletServiceImpl(){
		repo= new WalletRepoImpl();
	}
	public WalletServiceImpl(Map<String, Customer> data){
		repo= new WalletRepoImpl(data);
	}
	public WalletServiceImpl(WalletRepo repo) {
		super();
		this.repo = repo;
	}
	
	
	
	public Customer createAccount(String name, String mobileNo, BigDecimal amount) {
		
		Customer cust=new Customer(name,mobileNo,new Wallet(amount));		
		acceptCustomerDetails(cust);
		boolean result=repo.save(cust);
		if(result==true)
			return cust;
		else
			return null;
			
		}

	public Customer showBalance(String mobileNo) {
		
		Customer customer=repo.findOne(mobileNo);		
		if(customer!=null)
			return customer;
		else
			throw new InvalidInputException("Invalid mobile no ");
	}

	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount) throws InvalidInputException {	
		
		Customer sourceCust=new Customer();
		Customer targetCust=new Customer();
		Wallet sourceWallet=new Wallet();
		Wallet targetWallet=new Wallet();
		sourceCust=repo.findOne(sourceMobileNo);
		targetCust=repo.findOne(targetMobileNo);
		if(sourceCust!=null && targetCust!=null)
		{		
			BigDecimal bal=sourceCust.getWallet().getBalance();
		if(bal.compareTo(amount)>0)
		{
			BigDecimal diff=bal.subtract(amount);
			sourceWallet.setBalance(diff);
			sourceCust.setWallet(sourceWallet);

			BigDecimal baladd=targetCust.getWallet().getBalance();
			BigDecimal sum=baladd.add(amount);			
			targetWallet.setBalance(sum);
			targetCust.setWallet(targetWallet);

			obj.getData().put(targetMobileNo, targetCust);
			obj.getData().put(sourceMobileNo, sourceCust);
		}
		else
		{
			throw new InsufficientBalanceException("Insufficient Balance.Amount Cannot Be Withdraw");
		}
				
			
		}
		else
		{
			throw new InvalidInputException("Account Doesn't Exist");
		}		
		return sourceCust;
	}

	public Customer depositAmount(String mobileNo, BigDecimal amount) {
		
		Customer cust=new Customer();
		Wallet wallet=new Wallet();
		cust=repo.findOne(mobileNo);
		if(cust!=null)
		{
			if(amount.compareTo(new BigDecimal(0)) > 0)
			{
				BigDecimal amtAdd= cust.getWallet().getBalance().add(amount);
				wallet.setBalance(amtAdd);
				cust.setWallet(wallet);
				obj.getData().put(mobileNo, cust);
			}
			else
				throw new InvalidInputException("Deposit amount should be in positive");
			
			
		}
		else
			throw new InvalidInputException("Mobile number not found");
		return cust;
	}

	public Customer withdrawAmount(String mobileNo, BigDecimal amount) {
		Customer cust=new Customer();
		Wallet wallet=new Wallet();
		cust=repo.findOne(mobileNo);
		if(cust!=null)
		{
			BigDecimal bal=cust.getWallet().getBalance();
			BigDecimal amtSub;
			if(bal.compareTo(amount)>0)
			{
				amtSub=bal.subtract(amount);
				wallet.setBalance(amtSub);
				cust.setWallet(wallet);
				obj.getData().put(mobileNo, cust);
			}
			else
			{
				throw new InsufficientBalanceException("Insufficient Balance! Sry Amount Cannot be Withdraw");
			}
			
		}
		else
			throw new InvalidInputException("Mobile number not found");
		return cust;
	}
public boolean validatephone(String phoneno) {
		
		String pattern1="[7-9]?[0-9]{10}";
		if(phoneno.matches(pattern1))
		{
			return true;
		}else {
			return false;
		}
	}
	public boolean validateName(String pName) {
		String pattern="[A-Z][A-Z]*";
		if(pName.matches(pattern))
		{
			return true;
		}
		else {
			return false;
		}
	}

public void acceptCustomerDetails(Customer cust)  {
	Scanner sc=new Scanner(System.in);
	while (true) {
		String str=cust.getMobileNo();
		if(validatephone(str))//method validate name
		{
			
			break;
		}
		else
		{
			System.err.println("Wrong Phone number!!\n Please Start with 9 ");
			System.out.println("Enter Phone number Again eg:9876543210");
			cust.setMobileNo(sc.next());
		}
	}
	while (true) {
		String str1=cust.getName();
		if(validateName(str1))//method validate name
		{
			break;
		}
		else
		{
			System.err.println("Wrong  Name!!\n Please enter name in Capital letters ");
			System.out.println("Enter  Name Again eg:JOHN");
			cust.setName(sc.next());
		}
	}
	while(true)
	{
		BigDecimal amount = cust.getWallet().getBalance();
		if(amount.compareTo(new BigDecimal(0)) > 0)
		{
			break;
		}
		else
			throw new InvalidInputException("balance should be in positive");
			
			
	}
}
}
