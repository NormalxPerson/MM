package com.moneymanager.core;

import com.moneymanager.Cli.CliMenu;
import com.moneymanager.database.DatabaseConnection;
import com.moneymanager.repos.AccountRepo;
import com.moneymanager.repos.SQLiteAccountRepo;
import com.moneymanager.repos.SQLiteTransactionRepo;
import com.moneymanager.repos.TransactionRepo;
import com.moneymanager.service.AccountService;
import com.moneymanager.service.TransactionService;

import java.io.File;
import java.sql.SQLException;

public class First {
    public static void main(String[] args) {
        File csvFile = new File("src/main/resources/truistNovToJan.csv");
	    AccountRepo accountRepo = new SQLiteAccountRepo();
        AccountService accountService = new AccountService(accountRepo);
		TransactionRepo transactionRepo = new SQLiteTransactionRepo();
		TransactionService transactionService = new TransactionService(transactionRepo);
	    CliMenu menu = new CliMenu(accountService, transactionService);
		menu.start();
	    // CsvParser csvParser = new CsvParser(csvFile);

    }
}