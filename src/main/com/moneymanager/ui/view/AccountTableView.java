package com.moneymanager.ui.view;

import com.moneymanager.core.Account;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class AccountTableView extends TableView<AccountTableView.AccountModel> {

	public AccountTableView() {
		
		initializeColumns();
	}
	
	private void initializeColumns() {
		TableColumn<AccountModel, String> accountNameColumn = new TableColumn<>("Account Name");
		accountNameColumn.setCellValueFactory(cellData -> cellData.getValue().accountNameProperty());
		
		TableColumn<AccountModel, String> accountTypeColumn = new TableColumn<>("Account Type");
		accountTypeColumn.setCellValueFactory(cellData -> cellData.getValue().accountTypeProperty());
		
		TableColumn<AccountModel, Double> accountBalanceColumn = new TableColumn<>("Balance");
		accountBalanceColumn.setCellValueFactory(cellData -> cellData.getValue().accountBalanceProperty().asObject());
		accountBalanceColumn.setCellFactory(this::createBalanceCellFactory); // Use method reference
		
		//width of columns
		this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		accountNameColumn.setPrefWidth(110);
		accountTypeColumn.setPrefWidth(110);
		accountBalanceColumn.setPrefWidth(130);
		
		getColumns().addAll(List.of(accountNameColumn, accountTypeColumn, accountBalanceColumn));
	}
	
	private TableCell<AccountModel, Double> createBalanceCellFactory(TableColumn<AccountModel, Double> column) {
		return new TableCell<>() {
			@Override
			protected void updateItem(Double item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText("$" + String.format("%.2f", item));
				}
			}
		};
	}
	

	
	
	public static class AccountModel {
		private StringProperty accountName;
		private StringProperty bankName;
		private StringProperty accountType;
		private DoubleProperty accountBalance;
		private StringProperty accountId;
	
		public AccountModel(String accountName, String bankName, String accountType, Double accountBalance, String accountId) {
			this.accountName = new SimpleStringProperty(accountName);
			this.bankName = new SimpleStringProperty(bankName);
			this.accountType = new SimpleStringProperty(accountType);
			this.accountBalance = new SimpleDoubleProperty(accountBalance);
			this.accountId = new SimpleStringProperty(accountId);
		}
		
		public String getAccountName() { return accountName.get();}
		public void setAccountName(String accountName) { this.accountName.set(accountName);}
		public StringProperty accountNameProperty() { return accountName;}
		
		public String getBankName() { return bankName.get();}
		public void setBankName(String bankName) { this.bankName.set(bankName);}
		public StringProperty bankNameProperty() { return bankName;}
		
		public String getAccountType() { return accountType.get();}
		public void setAccountType(String accountType) { this.accountType.set(accountType);}
		public StringProperty accountTypeProperty() { return accountType;}
		
		public Double getAccountBalance() { return accountBalance.get();}
		public void setAccountBalance(Double accountBalance) {this.accountBalance.set(accountBalance);}
		public DoubleProperty accountBalanceProperty() { return accountBalance;}
		
		public String getAccountId() { return accountId.get();}
		public void setAccountId(String accountId) { this.accountId.set(accountId);}
		public StringProperty accountIdProperty() { return accountId;}
		@Override
		public String toString() {
			return accountName.get(); // Display account name in the ComboBox
		}
		
	
	
	
	
	}
}
