package com.moneymanager.ui.controller;

import com.moneymanager.core.Transaction;
import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.view.AccountTableView;
import com.moneymanager.ui.view.TransactionSlidingForm;
import com.moneymanager.ui.view.TransactionTableView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionViewController implements Initializable {
	
	@FXML
	private VBox transactionContainer;
	
	private TransactionTableView transactionTableView;
	private TransactionSlidingForm transactionSlidingForm;
	private TransactionService transactionService;


	public TransactionViewController() {
		this.transactionTableView = new TransactionTableView();
		this.transactionContainer = new VBox();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	public void postInitialize(TransactionService transactionService) {
		this.transactionService = transactionService;
		transactionTableView.populateTransactionTable(transactionService.getObservableTransactionModelsList());
		this.transactionSlidingForm = new TransactionSlidingForm(transactionService.getAccountModelObservableList());
		transactionContainer.getChildren().addAll(transactionTableView, transactionSlidingForm);
		transactionSlidingForm.hideForm();
		VBox.setVgrow(transactionTableView, Priority.ALWAYS);
		
	}
	

	
	public void showTransactionForm() {
		transactionSlidingForm.showForm();
	}
	
	public void hideTransactionForm() {
		transactionSlidingForm.hideForm();
	}
	
	public void refreshTransactionTable() {
		transactionTableView.populateTransactionTable(transactionService.getObservableTransactionModelsList());
	}
	
	public VBox getTransactionContainer() { return this.transactionContainer; }


}


