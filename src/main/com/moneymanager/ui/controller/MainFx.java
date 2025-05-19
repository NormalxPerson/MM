package com.moneymanager.ui.controller;

import com.moneymanager.core.BudgetCategory;
import com.moneymanager.repos.*;
import com.moneymanager.service.AccountService;
import com.moneymanager.service.BudgetService;
import com.moneymanager.service.TransactionService;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFx extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		AccountRepo accountRepo = new SQLiteAccountRepo();
		TransactionRepo transactionRepo = new SQLiteTransactionRepo();
		BudgetRepo budgetRepo = new SQLBudgetRepo();
		BudgetCategoryRepo budgetCategoryRepo = new SQLBudgetCategoryRepo();
		
		AccountService accountService = new AccountService(accountRepo);
		TransactionService transactionService = new TransactionService(transactionRepo, accountService);
		BudgetService budgetService = new BudgetService(budgetRepo, budgetCategoryRepo, transactionRepo);
		
		transactionService.setBudgetCategoryList(budgetService.getAllCategories());
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
		Parent root = loader.load();
		
		NavigationController navigationController = loader.getController();
		navigationController.setAccountService(accountService);
		navigationController.setTransactionService(transactionService);
		navigationController.setBudgetService(budgetService);
		navigationController.setUpControllers();
		
		// 2. Create the Scene
		Scene scene = new Scene(root, 900, 750);
		scene.getStylesheets().add(getClass().getResource("/stylesheetfx.css").toExternalForm());
		// 3. Set up the Stage
		primaryStage.setTitle("Money Manager");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
