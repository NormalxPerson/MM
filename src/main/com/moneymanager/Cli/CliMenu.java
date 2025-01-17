package com.moneymanager.Cli;

import com.moneymanager.core.Account;
import com.moneymanager.core.TransactionFactory;
import com.moneymanager.service.AccountService;
import com.moneymanager.service.TransactionService;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CliMenu {
	private final Scanner scanner;
	private final AccountService accountService;
	private final TransactionService transactionService;
	//private final TransactionService transactionService;
	private boolean running = true;
	
	public CliMenu(AccountService accountService, TransactionService transactionService) {
		this.scanner = new Scanner(System.in);
		this.accountService = accountService;
		this.transactionService = transactionService;
	}
	
	public void start() {
		while (running) {
			displayMenu();
			handleUserInput();
		}
	}
	
	private void displayMenu() {
		System.out.println("\n=== Money Manager CLI ===");
		System.out.println("1. List All Accounts");
		System.out.println("2. Add New Account");
		System.out.println("3. View Account Details");
		System.out.println("4. Add Transaction");
		System.out.println("5. Import CSV");
		System.out.println("0. Exit");
		System.out.print("Select an option: ");
	}
	
	private void handleUserInput() {
		try {
			int choice = Integer.parseInt(scanner.nextLine());
			switch (choice) {
				case 1 -> listAccounts();
				case 2 -> addAccount();
				//case 3 -> viewAccountDetails();
				case 4 -> addUserInputTransaction();
				//case 5 -> importCsv();
				//case 0 -> exit();
				default -> System.out.println("Invalid option. Please try again.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Please enter a valid number.");
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	private void addUserInputTransaction() {
		Map<String, Account> mapOfAccounts = accountService.getAccountMap();
		System.out.println("\n====== Adding Transaction ======");
		
		// Loop until a valid account ID is entered or user cancels
		while (true) {
			System.out.println("Enter Account ID (or 'c' to cancel): ");
			String accountId = scanner.nextLine().trim();
			
			if (accountId.equalsIgnoreCase("c")) {
				System.out.println("Transaction cancelled.");
				return; // Exit the method if the user cancels
			}
			
			if (mapOfAccounts.containsKey(accountId)) {
				// Valid transaction ID, proceed to get other transaction details
				try {
					System.out.println("Enter amount:");
					double amount = Double.parseDouble(scanner.nextLine());
					
					System.out.println("Enter description:");
					String description = scanner.nextLine();
					
					System.out.println("Enter date (MM-dd-yyyy):");
					String date = scanner.nextLine();
					
					System.out.println("Enter type (DEBIT/CREDIT):");
					String type = scanner.nextLine().toUpperCase();
					
					transactionService.addTransaction(amount, description, date, type, accountId);
					System.out.println("Transaction added successfully!");
					return; // Exit the method after adding the transaction
					
				} catch (NumberFormatException e) {
					System.out.println("Invalid input for amount. Please enter a valid number.");
				} catch (Exception e) {
					System.out.println("Failed to add transaction: " + e.getMessage());
				}
			} else {
				System.out.println("Invalid transaction ID. Please try again.");
			}
		}
	}
	
	private void listAccounts() {
		System.out.println("\n=== Accounts List ===");
		accountService.getAccountList().forEach(account ->
				System.out.printf("%s. %s: %s (%.2f)\n",
						account.getAccountId(),
						account.getAccountName(),
						account.getBankName(),
						account.getBalance())
		);
	}
	
	private void addAccount() {
		System.out.println("\n=== Add New Account ===");
		System.out.print("Account Name: ");
		String name = scanner.nextLine();
		System.out.print("Bank Name: ");
		String bank = scanner.nextLine();
		System.out.print("Account Type (DEBT/CREDIT): ");
		String type = scanner.nextLine().toUpperCase();
		
		try {
			accountService.createAccount(name, bank, type);
			System.out.println("Account added successfully!");
		} catch (Exception e) {
			System.out.println("Failed to add account: " + e.getMessage());
		}
	}
}