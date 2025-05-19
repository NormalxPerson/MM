package com.moneymanager.ui.view;

import com.moneymanager.core.BudgetCategory;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionTableView extends TableView<TransactionTableView.TransactionModel> {
	
	public TransactionTableView() {
		initializeColumns(); this.getStyleClass().addAll("table-view", "md3-rounded-medium"); this.setStyle("-fx-: transparent;");}
	
	private void initializeColumns() {
		TableColumn<TransactionModel, LocalDate> dateColumn = new TableColumn<>("Date");
		dateColumn.setCellValueFactory(cellData -> cellData.getValue().transactionDateProperty());
		dateColumn.setCellFactory(this::createFormattedDateCellFactory);
		
		TableColumn<TransactionModel, Double> amountColumn = new TableColumn<>("Amount");
		amountColumn.setCellValueFactory(cellData -> cellData.getValue().transactionAmountProperty().asObject());
		amountColumn.setCellFactory(this::createAmountCellFactory); // Use method reference
		
		TableColumn<TransactionModel, String> descriptionColumn = new TableColumn<>("Description");
		descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().transactionDescriptionProperty());
		
		TableColumn<TransactionModel, String> categoryColumn = new TableColumn<>("Budget Category");
		categoryColumn.setCellValueFactory(cellData -> cellData.getValue().TransactionCategoryNameProperty());
		
		TableColumn<TransactionModel, String> accountNameColumn = new TableColumn<>("Account Name");
		accountNameColumn.setCellValueFactory(cellData -> cellData.getValue().TransactionAccountNameProperty());
		
		this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		
		getColumns().addAll(List.of(dateColumn, amountColumn, descriptionColumn, categoryColumn, accountNameColumn));
		
	}
	
	private TableCell<TransactionModel, LocalDate> createFormattedDateCellFactory(TableColumn<TransactionModel, LocalDate> column) {
		return new TableCell<>() {
			private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M-d-yy");
			
			@Override
			protected void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(formatter.format(item));
				}
			}
		};
	}
	
	private TableCell<TransactionModel, Double> createAmountCellFactory(TableColumn<TransactionModel, Double> column) {
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
	
	public void populateTransactionTable(ObservableList<TransactionModel> items) {
		super.setItems(items);
	}
	
	public static class TransactionModel {
		public enum TransactionType {
			INCOME("Income"),
			EXPENSE("Expense");
			
			private String displayName;
			
			TransactionType(String displayName) {
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
		
		private StringProperty transactionId;
		private DoubleProperty transactionAmount;
		private StringProperty transactionDescription;
		private ObjectProperty<LocalDate> transactionDate;
		private ObjectProperty<TransactionType> transactionType;
		private StringProperty transactionAccountId;
		private StringProperty transactionAccountName;
		private StringProperty transactionCategoryId;
		private StringProperty transactionCategoryName;
		
		//public TransactionModel(String transactionId, LocalDate date, double amount, String description, String type, String accountId, String accountName) {
		private TransactionModel(Builder builder) {
			this.transactionId = new SimpleStringProperty(builder.transactionId);
			this.transactionAmount = new SimpleDoubleProperty(builder.transactionAmount);
			this.transactionDescription = new SimpleStringProperty(builder.transactionDescription);
			this.transactionDate = new SimpleObjectProperty<>(builder.transactionDate);
			this.transactionType = new SimpleObjectProperty<>(builder.transactionType);
			this.transactionAccountId = new SimpleStringProperty(builder.transactionAccountId);
			this.transactionAccountName = new SimpleStringProperty(builder.transactionAccountName);
			this.transactionCategoryId = new SimpleStringProperty(builder.transactionCategoryId);
			this.transactionCategoryName = new SimpleStringProperty(builder.transactionCategoryName);
		}
		
		/*
		public TransactionModel(String transactionId, LocalDate date, double amount, String description, String type) {
			this.transactionId = new SimpleStringProperty(transactionId);
			this.transactionAmount = new SimpleDoubleProperty(amount);
			this.transactionDescription = new SimpleStringProperty(description);
			this.transactionDate = new SimpleObjectProperty<>(date);
			this.transactionType = new SimpleObjectProperty<>(TransactionType.valueOf(type.toUpperCase()));
			this.transactionCategoryId = new SimpleStringProperty(null);
		}*/
		
		
		public String getTransactionId() {
			return transactionId.get();
		}
		
		public void setTransactionId(String transactionId) {
			this.transactionId.set(transactionId);
		}
		
		public StringProperty transactionIdProperty() {
			return transactionId;
		}
		
		public Double getTransactionAmount() {
			return transactionAmount.get();
		}
		
		public void setTransactionAmount(Double transactionAmount) {
			this.transactionAmount.set(transactionAmount);
		}
		
		public DoubleProperty transactionAmountProperty() {
			return transactionAmount;
		}
		
		public String getTransactionDescription() {
			return transactionDescription.get();
		}
		public void setTransactionDescription(String transactionDescription) {this.transactionDescription.set(transactionDescription);}
		public StringProperty transactionDescriptionProperty() {
			return transactionDescription;
		}
		
		public LocalDate getTransactionDate() {
			return transactionDate.get();
		}
		public String getStringDate() {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M-d-yy");
			return transactionDate.get().format(formatter);
		}
		public void setTransactionDate(LocalDate transactionDate) {
			this.transactionDate.set(transactionDate);
		}
		public ObjectProperty<LocalDate> transactionDateProperty() {
			return transactionDate;
		}
		
		public TransactionType getTransactionType() {
			return transactionType.get();
		}
		public void setTransactionType(String transactionType) {this.transactionType.set(TransactionType.valueOf(transactionType.toUpperCase()));}
		public ObjectProperty<TransactionType> transactionTypeProperty() {
			return transactionType;
		}
		
		public String getTransactionAccountId() {
			return transactionAccountId.get();
		}
		public void setTransactionAccountId(String transactionAccountId) {this.transactionAccountId.set(transactionAccountId);}
		public StringProperty TransactionAccountIdProperty() {
			return transactionAccountId;
		}
		
		public String getTransactionAccountName() {
			return transactionAccountName.get();
		}
		public void setTransactionAccountName(String transactionAccountName) {this.transactionAccountName.set(transactionAccountName);}
		public StringProperty TransactionAccountNameProperty() {
			return transactionAccountName;
		}
		
		public String getTransactionCategoryId() {
			return transactionCategoryId.get();
		}
		public void setTransactionCategoryId(String transactionCategoryId) {this.transactionCategoryId.set(transactionCategoryId);}
		public StringProperty TransactionCategoryIdProperty() {
			return transactionCategoryId;
		}
		
		public StringProperty TransactionCategoryNameProperty() {
			return transactionCategoryName;
		}
		public String getTransactionCategoryName() {
			return transactionCategoryName.get();
		}
		public void setTransactionCategoryName(String transactionCategoryName) {
			this.transactionCategoryName.set(transactionCategoryName);
		}
		
		public void makeChanges(String change, Object value) {
			if (change.equalsIgnoreCase("transactionAmount")) {
				double amount = 0.0;
				String balanceStr = (String) value;
				amount = Double.parseDouble(balanceStr);
				setTransactionAmount(amount);
			} else if (change.equalsIgnoreCase("transactionDescription")) {
				setTransactionDescription(String.valueOf(value));
			} else if (change.equalsIgnoreCase("transactionDate")) {
				setTransactionDate((LocalDate) value);
			} else if (change.equalsIgnoreCase("transactionType")) {
				setTransactionType(((TransactionType) value).toString());
			} else if (change.equalsIgnoreCase("budgetCategory")) {
				BudgetCategory cat = (BudgetCategory) value;
				setTransactionCategoryId(cat.getCategoryId());
				setTransactionCategoryName(cat.getCategoryName());
			} else if (change.equalsIgnoreCase("transactionAccount")) {
				AccountTableView.AccountModel accountModel = (AccountTableView.AccountModel) value;
				setTransactionAccountId(accountModel.getAccountId());
				setTransactionAccountName(accountModel.getAccountName());
			}
		}
		
		public boolean hasAccount() {
			String accountId = getTransactionAccountId();
			return accountId != null && !accountId.isEmpty() &&
					!"DELETED_ACCOUNT".equals(getTransactionAccountName());
		}
		
		public boolean hasCategory() {
			String categoryId = getTransactionCategoryId();
			return categoryId != null && !categoryId.isEmpty();
		}
		
		@Override
		public String toString() {
			return "Transaction: " + getTransactionDescription() + " ($" +
					String.format("%.2f", getTransactionAmount()) + ")";
		}
		
		public static class Builder {
			// Required parameters
			private final String transactionId;
			private final LocalDate transactionDate;
			private final double transactionAmount;
			private final TransactionType transactionType;
			
			// Optional parameters - initialized with default values
			private String transactionAccountId = "";
			private String transactionAccountName = "";
			private String transactionCategoryId = "";
			private String transactionCategoryName = "";
			private String transactionDescription = "";
			
			
			// Constructor with required parameters
			public Builder(String id, LocalDate date, double amount, String type) {
				this.transactionId = id;
				this.transactionDate = date;
				this.transactionAmount = amount;
				this.transactionType = TransactionType.valueOf(type.toUpperCase());
			}
			
			// Methods to set optional parameters
			public Builder accountId(String accountId) {
				this.transactionAccountId = accountId;
				return this;
			}
			
			public Builder transactionDescription(String transactionDescription) {
				this.transactionDescription = transactionDescription;
				return this;
			}
			
			public Builder accountName(String accountName) {
				this.transactionAccountName = accountName;
				return this;
			}
			
			public Builder categoryId(String categoryId) {
				this.transactionCategoryId = categoryId;
				return this;
			}
			
			public Builder categoryName(String categoryName) {
				this.transactionCategoryName = categoryName;
				return this;
			}
			
			// Build method to create the TransactionModel object
			public TransactionModel build() {
				return new TransactionModel(this);
			}
			
			
		}
	}
}