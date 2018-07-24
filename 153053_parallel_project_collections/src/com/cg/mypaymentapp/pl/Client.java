package com.cg.mypaymentapp.pl;

import java.math.BigDecimal;
import java.util.Scanner;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Wallet;
import com.cg.mypaymentapp.exception.InsufficientBalanceException;
import com.cg.mypaymentapp.exception.InvalidInputException;
import com.cg.mypaymentapp.service.WalletService;
import com.cg.mypaymentapp.service.WalletServiceImpl;

public class Client {
	Scanner sc = new Scanner(System.in);
	Customer customer=new Customer();
	String ans="";

	private WalletService walletService;
	public Client()
	{
		walletService=new WalletServiceImpl();
	}
	public void menu() 
	{
			
		System.out.println("********Payment wallet Application*********");
		
		System.out.println("-------------------------------------------");
		do {
			int choice;
			try {
				System.out.println();
				System.out.println("1. Create Account ");
				System.out.println("2. Show Balance");
				System.out.println("3. Fund Transfer");
				System.out.println("4. Deposit amount");
				System.out.println("5. Withdraw amount");
				System.out.println("6. Exit");			
				System.out.print("\nPlease Select an option : ");
				choice = sc.nextInt();
				switch (choice) {
				case 1:
					createAccount();
				break;				
				case 2:		
					showBalance();
				break;
				case 3:
					fundTransfer();
				break;
				case 4:
					depositAmount();
				break;
				case 5:
					withdrawAmount();
				break;
				case 6:
					System.out.println("Thank You, Visit again");
					System.exit(0);
					break;
				default:
					System.out.println("Invalid options,please select valid option");
					break;
				}
			} 
			catch (Exception e) 
			{
				System.out.println("");
			}
			System.out.println();
			
			System.out.print("\nDo you want to continue: Y/N    ");
			ans=sc.next();
			
		}while(ans.equalsIgnoreCase("y")||ans.equalsIgnoreCase("yes"));
			
	}
	
	public void createAccount()
	{
		try {
			System.out.print("Enter Name:(CAPITALS)            : ");
			String name=sc.next();
			System.out.print("\n");
			System.out.print("Enter Mobile Number              : ");
			String mobileNo=sc.next();
			System.out.print("\n");
			System.out.print("Enter Amount to deposit          : ");
			BigDecimal amount=sc.nextBigDecimal();
			System.out.println("\n");
			customer=walletService.createAccount(name, mobileNo, amount);
			if(customer!=null){
				System.out.println("Your account is successfully created!");
				System.out.println("Name     : "+customer.getName());
				System.out.println("Mobile NO: "+customer.getMobileNo());
				System.out.println("Balance  : " +customer.getWallet().getBalance());
				}
				else
					System.out.println("Account Already Exist,Plz Change mobileno");
		} 
		catch (InvalidInputException e) 
		{
			
			System.out.println("something went wrong "+e.getMessage());
		}
		
		
		
	}
	public void showBalance()
	{
		String mobileNo2;
		try 
		{
			System.out.print("Enter the mobile number to view balance : ");
			mobileNo2 = sc.next();
			System.out.println();
			customer=walletService.showBalance(mobileNo2);
			System.out.println("Your balance for the account liked with mobile number : "+mobileNo2+" is "+customer.getWallet());
		}
		catch (InvalidInputException e) 
		{
			System.out.println("something went wrong "+e.getMessage());
		}
	}
	public void fundTransfer()
	{
		try {
			System.out.print("Enter Source Mobile Number : ");
			String sourceMobileNo=sc.next();
			System.out.println();
			System.out.print("Enter Target Mobile Number : ");
			String targetMobileNo=sc.next();
			System.out.println();
			System.out.print("Enter amount to transfer   : ");
			BigDecimal amount1=sc.nextBigDecimal();	
			System.out.println();
			customer=walletService.fundTransfer(sourceMobileNo, targetMobileNo, amount1);
			
			System.out.println(amount1+" was successfully transfered to "+targetMobileNo);
			System.out.println("Name     : "+customer.getName());
			System.out.println("Mobile NO: "+customer.getMobileNo());
			System.out.println("Balance  : " +customer.getWallet().getBalance());				
	
			}
		catch (Exception e) 
		{
			
			System.out.println("something went wrong : "+e.getMessage());
		}
	}
	public void depositAmount()
	{
		try 
		{
			System.out.print("Enter Mobile Number     : ");
			String mobileNo3=sc.next();
			System.out.println();
			System.out.print("Enter amount to deposit :");
			BigDecimal amount3=sc.nextBigDecimal();
			System.out.println();
			customer=walletService.depositAmount(mobileNo3, amount3);
			System.out.println("Name     : "+customer.getName());
			System.out.println("Mobile NO: "+customer.getMobileNo());
			System.out.println("Balance  : " +customer.getWallet().getBalance());
			 
			
		} 
		catch (InsufficientBalanceException| InvalidInputException e) {
			System.out.println("something went wrong "+e.getMessage());
		}
	}
	public void withdrawAmount()
	{
		try {
			System.out.print("Enter Mobile Number      : ");
			String mobileNo4=sc.next();
			System.out.println();
			System.out.print("Enter amount to withdraw : ");
			BigDecimal amount4=sc.nextBigDecimal();
			System.out.println();
			customer=walletService.withdrawAmount(mobileNo4, amount4);
			System.out.println("Name     : "+customer.getName());
			System.out.println("Mobile NO: "+customer.getMobileNo());
			System.out.println("Balance  : " +customer.getWallet().getBalance());
		} 
		catch (InsufficientBalanceException | InvalidInputException e) {
			System.out.println("something went wrong :"+e.getMessage());
		}
	}
	
	public static void main(String[] args) 
	{
		Scanner sc = new Scanner(System.in);
		Client client=new Client();
		client.menu();
		System.out.println("\nThank you for using our payment wallet!");

	}

}
