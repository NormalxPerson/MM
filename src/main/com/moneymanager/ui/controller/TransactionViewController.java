package com.moneymanager.ui.controller;

import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.event.FormEvent;
import com.moneymanager.ui.view.TransactionForm;
import com.moneymanager.ui.view.TransactionTableView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
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
		
		transactionSlidingForm = new TransactionForm(transactionService.getAccountModelObservableMap(), transactionService.getAccountModelObservableList());
		this.editingForm = this.transactionSlidingForm;
		
		this.transactionCreationForm = new TransactionForm(transactionService.getAccountModelObservableMap(), transactionService.getAccountModelObservableList());
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
	protected void handleSaveEvent(FormEvent event) {
		if (event.getEventType() == FormEvent.SAVE) {
			TransactionTableView.TransactionModel transactionModel = (TransactionTableView.TransactionModel) event.getModel();
			Map<String, Object> changedValues = event.getFieldValues();
			
			for (String key : changedValues.keySet()) {
				if (!key.equalsIgnoreCase("updateBalance")) {
					transactionModel.makeChanges(key, changedValues.get(key));
				}
			}
			
			if (changedValues.containsKey("updateBalance")) {
				transactionService.updateAccountBalance(transactionModel.getTransactionAccountId(), transactionModel.getTransactionAmount());
			}
			
			try {
				if (transactionModel == null) {
					System.out.println("model is null in TransactionViewController.handleSaveEvent");
				} else {
					for (String fieldName : changedValues.keySet()) {
						Control field = transactionSlidingForm.getField(fieldName);
						if (field != null) {
							field.getStyleClass().remove("error-border");
							field.getStyleClass().add("success-border");
							
							Timeline timeline = new Timeline(
									new KeyFrame(Duration.seconds(3), e -> {
										field.getStyleClass().remove("success-border");
									})
							);
							timeline.play();
						}
					}
					
					transactionService.updateTransaction(transactionModel);
				}
			} catch (Exception e) {
				System.out.println("Error in TransactionViewController.handleSaveEvent");
			}
		}
	}
	
	@Override
	protected <T> void handleDeleteEvent(FormEvent<T> formDeleteEvent) {
		TransactionTableView.TransactionModel selectedModel = (TransactionTableView.TransactionModel) formDeleteEvent.getModel();
		int rowDeleted = transactionService.deleteTransaction(selectedModel);
		if (rowDeleted == 0) {
			Alert deleteErrorDialog = new Alert(Alert.AlertType.ERROR);
			deleteErrorDialog.setTitle("Error Deleting Account");
			deleteErrorDialog.setHeaderText("I am not sure why but something went wrong!");
			deleteErrorDialog.setContentText("Something went wrong!");
			deleteErrorDialog.showAndWait();
		} else if (rowDeleted == 1) {
			Alert deleteSuccessDialog = new Alert(Alert.AlertType.INFORMATION);
			deleteSuccessDialog.setTitle("Account Deleted!");
			deleteSuccessDialog.setHeaderText("Account Successfully Deleted!");
			deleteSuccessDialog.setContentText("Sheee Gonee!");
			deleteSuccessDialog.showAndWait();
			
			transactionSlidingForm.fireCloseEvent();
		}
	}
	
	
	@Override
	protected <T> void handleCloseEvent(FormEvent<T> formCloseEvent) {
		tableView.getSelectionModel().clearSelection();
	}
	
	@Override
	protected <T> void handleNewSaveEvent(FormEvent<T> formNewSaveEvent) {
		if (formNewSaveEvent.getEventType() == FormEvent.NEWSAVE) {
			Map<String, Object> fieldValues = formNewSaveEvent.getFieldValues();
			TransactionTableView.TransactionModel newModel = transactionService.createFromFormValues(fieldValues);
			transactionTableView.getSelectionModel().select(newModel);
			transactionSlidingForm.setCurrentModel(newModel);
		}
	}
}


