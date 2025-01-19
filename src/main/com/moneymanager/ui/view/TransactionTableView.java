package com.moneymanager.ui.view;

import com.moneymanager.ui.model.TransactionModel;
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
	private TableColumn<TransactionModel, String> dateColumn;
	private TableColumn<TransactionModel, String> amountColumn;
	private TableColumn<TransactionModel, String> descriptionColumn;
	private TableColumn<TransactionModel, String> accountNameColumn;
	
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
	

	

}
