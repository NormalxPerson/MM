package com.moneymanager.ui.view;

import com.moneymanager.core.Account;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.util.List;

public class AccountTableView extends TableView<AccountTableView.AccountModel> {

	public AccountTableView() {
		
		initializeColumns();
		this.getStyleClass().addAll("table-view", "md3-rounded-medium");
		

	
	}
	
	
	private void initializeColumns() {
		TableColumn<AccountModel, String> accountNameColumn = new TableColumn<>("Account Name");
		accountNameColumn.setCellValueFactory(cellData -> cellData.getValue().accountNameProperty());
		
		TableColumn<AccountModel, AccountModel.AccountType> accountTypeColumn = new TableColumn<>("Account Type");
		accountTypeColumn.setCellValueFactory(cellData -> cellData.getValue().accountTypeProperty());
		accountTypeColumn.setCellFactory(createAccountTypeCellFactory());
		
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
	
	private Callback<TableColumn<AccountModel, AccountModel.AccountType>, TableCell<AccountModel, AccountModel.AccountType>> createAccountTypeCellFactory() {
		return column -> { // This is the Callback
			return new TableCell<>() {
				@Override
				protected void updateItem(AccountModel.AccountType item, boolean empty) {
					super.updateItem(item, empty);
					if (empty || item == null) {
						setText(null);
					} else {
						setText(item.getDisplayName()); // Use the display name
					}
				}
			};
		};
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
	
	
	
	
	
	public void populateAccountTable(ObservableList<AccountModel> items) { super.setItems(items); }
	
	
	public static class AccountModel {
		public enum AccountType {
			DEBT("Debit"),
			CREDIT("Credit");
			
			private final String displayName;
			
			AccountType(String displayName) {
				this.displayName = displayName;
			}
			
			public String getDisplayName() {
				return displayName;
			}
			
			@Override
			public String toString() {
				return displayName;
			}
		
		
		}
		private StringProperty accountName;
		private StringProperty bankName;
		private ObjectProperty<AccountType> accountType;
		private DoubleProperty accountBalance;
		private StringProperty accountId;
	
		public AccountModel(String accountName, String bankName, String accountType, Double accountBalance, String accountId) {
			this.accountName = new SimpleStringProperty(accountName);
			this.bankName = new SimpleStringProperty(bankName);
			this.accountType = new SimpleObjectProperty<>(AccountType.valueOf(accountType.toUpperCase()));
			this.accountBalance = new SimpleDoubleProperty(accountBalance);
			this.accountId = new SimpleStringProperty(accountId);
		}
		
		public String getAccountName() { return accountName.get();}
		public void setAccountName(String accountName) { this.accountName.set(accountName);}
		public StringProperty accountNameProperty() { return accountName;}
		
		public String getBankName() { return bankName.get();}
		public void setBankName(String bankName) { this.bankName.set(bankName);}
		public StringProperty bankNameProperty() { return bankName;}
		
		public AccountType getAccountType() { return accountType.get();}
		public void setAccountType(AccountType accountType) { this.accountType.set(accountType);}
		public ObjectProperty<AccountType> accountTypeProperty() { return accountType;}
		
		public Double getAccountBalance() { return accountBalance.get();}
		public void setAccountBalance(Double accountBalance) {this.accountBalance.set(accountBalance);}
		public DoubleProperty accountBalanceProperty() { return accountBalance;}
		
		public String getAccountId() { return accountId.get();}
		public void setAccountId(String accountId) { this.accountId.set(accountId);}
		public StringProperty accountIdProperty() { return accountId;}
		
		public void makeChanges(String change, Object value) {
			if (change.equalsIgnoreCase("accountName")) {
				setAccountName(String.valueOf(value));
			} else if (change.equalsIgnoreCase("bankName")) {
				setBankName(String.valueOf(value));
			} else if (change.equalsIgnoreCase("accountType")) {
				setAccountType((AccountType) value);
			} else if (change.equalsIgnoreCase("accountBalance")) {
				double accountBalance = 0.0;
				String balanceStr = (String) value;
				accountBalance = Double.parseDouble(balanceStr);
				setAccountBalance(accountBalance);
			}
			
		}
		
		
		
		@Override
		public String toString() {
			return accountName.get(); // Display account name in the ComboBox
		}
		
		
	
	
	
	
	}
}
