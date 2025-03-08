package com.moneymanager.ui.controller;

import com.moneymanager.service.AccountService;
import com.moneymanager.ui.event.FormOpenedEvent;
import com.moneymanager.ui.view.AccountSlidingForm;
import com.moneymanager.ui.view.AccountTableView;
import com.moneymanager.ui.view.AbstractSlidingForm;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountViewController extends AbstractViewController implements Initializable, BaseViewController {
	
	@FXML
	private VBox accountContainer;
	private boolean formOpened = false;
	
	private AccountTableView accountTableView;
	private AccountSlidingForm accountSlidingForm;
	private AccountService accountService;
	
	private AccountTableView.AccountModel selectedAccountModel;
	
	
	public AccountViewController() {
		this.accountTableView = new AccountTableView();
		this.tableView = this.accountTableView;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.container = this.accountContainer;
		
//		accountTableView.setRowFactory(tv -> {
//			TableRow<AccountTableView.AccountModel> row = new TableRow<>();
//
//			row.setOnMouseClicked(event -> {
//				if (accountSlidingForm.getFormStatus() == AbstractSlidingForm.FormStatus.ADDING) {
//					// Prevent selection of any row while adding
//					event.consume(); // Consume the event to stop further processing
//					selectBlankRow();
//					return;
//				}
//				if (!row.isEmpty() && event.getClickCount() == 1 ) {
//					if (accountSlidingForm.getFormStatus().equals(AbstractSlidingForm.FormStatus.CLOSED)) {
//						accountContainer.fireEvent(new FormOpenedEvent());
//					}
//					this.selectedAccountModel = row.getItem();
//					// handle click
//					System.out.println("Clicked row: " + selectedAccountModel);
//					accountSlidingForm.showForm(AbstractSlidingForm.FormStatus.EDITING, selectedAccountModel);
//				}
//			});
//			return row;
//		});
		
		
	}
	
	public void postInitialize() {
		refreshAccountTable(accountService.getAccountModelObservableList());
		accountSlidingForm = new AccountSlidingForm(accountService);
		this.slidingForm = accountSlidingForm;
		accountContainer.getChildren().addAll(accountTableView, accountSlidingForm);
		VBox.setVgrow(accountTableView, Priority.ALWAYS);
		setupRowSelection();
	}
	
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
		postInitialize();
	}
	

	@Override
	protected void handleRowClick(TableRow<?> row, MouseEvent event) { // Implement abstract method
		if (!row.isEmpty() && event.getClickCount() == 1 ) {
			if (slidingForm.getFormStatus().equals(AbstractSlidingForm.FormStatus.CLOSED)) {
				accountContainer.fireEvent(new FormOpenedEvent());
			}
			this.selectedAccountModel = (AccountTableView.AccountModel) row.getItem(); // Cast to AccountModel
			// handle click
			System.out.println("Clicked row: " + selectedAccountModel);
			accountSlidingForm.showForm(AbstractSlidingForm.FormStatus.EDITING, selectedAccountModel);
		}
	}
	
	public VBox getAccountContainer() { return this.accountContainer;}
	
	public void refreshAccountTable(ObservableList<AccountTableView.AccountModel> accountModelObservableList) {
		accountTableView.populateAccountTable(accountModelObservableList);
		
	}
	
	
	
}

