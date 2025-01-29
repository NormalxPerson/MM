package com.moneymanager.ui.view;

import com.moneymanager.ui.model.TransactionModelOLD;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionTableView extends TableView<TransactionTableView.TransactionModel> {
	
	public TransactionTableView() {
		initializeColumns();
	}
	
	private void initializeColumns() {
		TableColumn<TransactionModel, LocalDate> dateColumn = new TableColumn<>("Date");
		dateColumn.setCellValueFactory(cellData -> cellData.getValue().transactionDateProperty());
		dateColumn.setCellFactory(this::createFormattedDateCellFactory);
		
		TableColumn<TransactionModel, Double> amountColumn = new TableColumn<>("Amount");
		amountColumn.setCellValueFactory(cellData -> cellData.getValue().transactionAmountProperty().asObject());
		amountColumn.setCellFactory(this::createAmountCellFactory); // Use method reference
		
		TableColumn<TransactionModel, String> descriptionColumn = new TableColumn<>("Description");
		descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().transactionDescriptionProperty());
		
		TableColumn<TransactionModel, String> accountNameColumn = new TableColumn<>("Account Name");
		accountNameColumn.setCellValueFactory(cellData -> cellData.getValue().TransactionAccountNameProperty());
		
		this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		
		getColumns().addAll(List.of(dateColumn, amountColumn, descriptionColumn, accountNameColumn));
		
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
		private StringProperty transactionId;
		private DoubleProperty transactionAmount;
		private StringProperty transactionDescription;
		private ObjectProperty<LocalDate> transactionDate;
		private StringProperty TransactionType;
		private StringProperty transactionAccountId;
		private StringProperty transactionAccountName;
		
		public TransactionModel(String transactionId, LocalDate date, double amount, String description, String type, String accountId, String accountName) {
			this.transactionId = new SimpleStringProperty(transactionId);
			this.transactionAmount = new SimpleDoubleProperty(amount);
			this.transactionDescription = new SimpleStringProperty(description);
			this.transactionDate = new SimpleObjectProperty<>(date);
			this.TransactionType = new SimpleStringProperty(type);
			this.transactionAccountId = new SimpleStringProperty(accountId);
			this.transactionAccountName = new SimpleStringProperty(accountName);
		}
		
		public String getTransactionId() {return transactionId.get();}
		public void setTransactionId(String transactionId) {this.transactionId.set(transactionId);}
		public StringProperty transactionIdProperty() {return transactionId;}
		
		public Double getTransactionAmount() {return transactionAmount.get();}
		public void setTransactionAmount(Double transactionAmount) {this.transactionAmount.set(transactionAmount);}
		public DoubleProperty transactionAmountProperty() {return transactionAmount;}
		
		public String getTransactionDescription() {return transactionDescription.get();}
		public void setTransactionDescription(String transactionDescription) {this.transactionDescription.set(transactionDescription);}
		public StringProperty transactionDescriptionProperty() {return transactionDescription;}
		
		public LocalDate getTransactionDate() {return transactionDate.get();}
		public void setTransactionDate(LocalDate transactionDate) {this.transactionDate.set(transactionDate);}
		public ObjectProperty<LocalDate> transactionDateProperty() {return transactionDate;}
		
		public String getTransactionType() {return TransactionType.get();}
		public void setTransactionType(String transactionType) {this.TransactionType.set(transactionType);}
		public StringProperty TransactionTypeProperty() {return TransactionType;}
		
		public String getTransactionAccountId() {return transactionAccountId.get();}
		public void setTransactionAccountId(String transactionAccountId) {this.transactionAccountId.set(transactionAccountId);}
		public StringProperty TransactionAccountIdProperty() {return transactionAccountId;}
		
		public String getTransactionAccountName() {return transactionAccountName.get();}
		public void setTransactionAccountName(String transactionAccountName) {this.transactionAccountName.set(transactionAccountName);}
		public StringProperty TransactionAccountNameProperty() {return transactionAccountName;}
		
		
	}
}