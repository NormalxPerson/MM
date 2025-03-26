package com.moneymanager.ui.controller;

import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.event.FormEvent;
import com.moneymanager.ui.view.AccountTableView;
import com.moneymanager.ui.view.TransactionForm;
import com.moneymanager.ui.view.TransactionTableView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

public class TransactionViewController extends AbstractViewController implements Initializable, BaseViewController {
	
	@FXML
	private VBox transactionContainer;
	
	private TransactionTableView transactionTableView;
	private TransactionForm transactionSlidingForm;
	private TransactionForm transactionCreationForm;
	private TransactionService transactionService;
	
	
	public TransactionViewController() {
		this.transactionTableView = new TransactionTableView();
		this.tableView = this.transactionTableView;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.container = transactionContainer;
	}
	
	public void postInitialize() {
		refreshTransactionTable(transactionService.getObservableTransactionModelsList());
		
		transactionSlidingForm = new TransactionForm(transactionService.getAccountModelObservableList());
		this.editingForm = this.transactionSlidingForm;
		
		this.transactionCreationForm = new TransactionForm(transactionService.getAccountModelObservableList());
		this.creationDialogForm = this.transactionCreationForm;
		
		transactionContainer.getChildren().addAll(transactionTableView, transactionSlidingForm);
		VBox.setVgrow(transactionTableView, Priority.ALWAYS);
		setupRowSelection();
	}
	
	
	public void refreshTransactionTable(ObservableList<TransactionTableView.TransactionModel> transactionModels) {
		transactionTableView.populateTransactionTable(transactionModels);
	}
	
	public VBox getTransactionContainer() { return this.transactionContainer; }
	
	public void setTransactionService(TransactionService transactionService) {
		this.transactionService = transactionService;
		postInitialize();
	}
	
	@Override
	protected void handleRowClick(TableRow<?> row, MouseEvent event) { // Implement abstract method
		if (!row.isEmpty() && event.getClickCount() == 1 ) {
			TransactionTableView.TransactionModel selectedTransaction = (TransactionTableView.TransactionModel) row.getItem();
			System.out.println("Clicked: " + selectedTransaction);
			
			transactionSlidingForm.setCurrentModel(selectedTransaction);
		}
	}
	
	@Override
	protected <T> void handleSaveEvent(FormEvent<T> formSaveEvent) {

	}
	
	@Override
	protected <T> void handleDeleteEvent(FormEvent<T> formDeleteEvent) {
	
	}
	
	@Override
	protected <T> void handleCloseEvent(FormEvent<T> formCloseEvent) {
	
	}
	
	@Override
	protected <T> void handleNewSaveEvent(FormEvent<T> formNewSaveEvent) {
		if (formNewSaveEvent.getEventType() == FormEvent.NEWSAVE) {
			Map<String, Object> fieldValues = formNewSaveEvent.getFieldValues();
			TransactionTableView.TransactionModel newModel = transactionService.createAndAddTransaction(fieldValues);
			transactionTableView.getSelectionModel().select(newModel);
			transactionSlidingForm.setCurrentModel(newModel);
		}
	}
}


