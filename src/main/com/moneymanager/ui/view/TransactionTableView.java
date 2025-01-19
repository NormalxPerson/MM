package com.moneymanager.ui.view;

import com.moneymanager.ui.model.TransactionModel;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionTableView extends TableView<TransactionModel> {
	private TableColumn<TransactionModel, ObjectProperty<LocalDate>> dateColumn;
	private TableColumn<TransactionModel, DoubleProperty> amountColumn;
	private TableColumn<TransactionModel, StringProperty> descriptionColumn;
	private TableColumn<TransactionModel, StringProperty> accountNameColumn;
	
	public TransactionTableView() {
		initializeColumns();
	}
	
	private void initializeColumns() {
		dateColumn = new TableColumn<>("Date");
		amountColumn = new TableColumn<>("Amount");
		descriptionColumn = new TableColumn<>("Description");
		accountNameColumn = new TableColumn<>("Account Name");
		
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		accountNameColumn.setCellValueFactory(new PropertyValueFactory<>("accountName"));
		
		getColumns().addAll(List.of(dateColumn, amountColumn, descriptionColumn, accountNameColumn));
	}
	
	public void populateTransactionTable(ObservableList<TransactionModel> items) {
		super.setItems(items);
	}
	

	

}
