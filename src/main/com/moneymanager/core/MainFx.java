package com.moneymanager.core;

import com.moneymanager.repos.AccountRepo;
import com.moneymanager.repos.SQLiteAccountRepo;
import com.moneymanager.repos.SQLiteTransactionRepo;
import com.moneymanager.repos.TransactionRepo;
import com.moneymanager.service.AccountService;
import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.controller.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainFx extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// 1. Load the FXML file
		FXMLLoader loader = new FXMLLoader(getClass().getResource("views/MainView.fxml"));
		Parent root = loader.load();
		
		MainViewController mainViewController = loader.getController();
		
		AccountRepo accountRepo = new SQLiteAccountRepo();
		AccountService accountService = new AccountService(accountRepo);
		TransactionRepo transactionRepo = new SQLiteTransactionRepo();
		TransactionService transactionService = new TransactionService(transactionRepo, accountService);
		
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
		AccountRepo accountRepo = new SQLiteAccountRepo();
		AccountService accountService = new AccountService(accountRepo);
		TransactionRepo transactionRepo = new SQLiteTransactionRepo();
		TransactionService transactionService = new TransactionService(transactionRepo, accountService);
		launch(args);
	}
}
