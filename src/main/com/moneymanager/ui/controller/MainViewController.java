package com.moneymanager.ui.controller;

import com.moneymanager.core.Transaction;
import com.moneymanager.repos.AccountRepo;
import com.moneymanager.repos.TransactionRepo;
import com.moneymanager.service.AccountService;
import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.model.TransactionModel;
import com.moneymanager.ui.view.TransactionTableView;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;

import javax.swing.text.TabableView;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
	
	@FXML
	private TransactionTableView transactionTableView;
	private AccountService accountService;
	private TransactionService transactionService;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (transactionService != null) {
			populateTransactionTable();
		}
	}
	
	
	private void populateTransactionTable() {
		ObservableList<TransactionModel> transactionModelObservableList = transactionService.getUnmodifiableTransactionModelsList();
		transactionTableView.setItems(transactionModelObservableList);
		
	}
	
	public void setTransactionService(TransactionService transactionService) {
		this.transactionService = transactionService;
		populateTransactionTable();
	}
	
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	
	
	
}
