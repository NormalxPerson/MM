package com.moneymanager.ui.controller;

import com.moneymanager.repos.AccountRepo;
import com.moneymanager.repos.SQLiteAccountRepo;
import com.moneymanager.repos.SQLiteTransactionRepo;
import com.moneymanager.repos.TransactionRepo;
import com.moneymanager.service.AccountService;
import com.moneymanager.service.TransactionService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFx extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// 1. Load the FXML file
		
		AccountRepo accountRepo = new SQLiteAccountRepo();
		AccountService accountService = new AccountService(accountRepo);
		TransactionRepo transactionRepo = new SQLiteTransactionRepo();
		TransactionService transactionService = new TransactionService(transactionRepo, accountService);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
		Parent root = loader.load();
		MainViewController mainViewController = loader.getController();
		
		mainViewController.setTransactionService(transactionService);
		mainViewController.setAccountService(accountService);
		// 2. Create the Scene
		Scene scene = new Scene(root);
		
		// 3. Set up the Stage
		primaryStage.setTitle("Money Manager");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
