package com.moneymanager.service;

import com.moneymanager.core.Transaction;
import com.moneymanager.ui.view.TransactionTableView;
import javafx.collections.ObservableList;

import java.util.List;

public interface TransactionServiceInterface {
	TransactionTableView.TransactionModel createTransactionFromUser(double amount, String description, String date, String type, String accountId, String categoryId);
	List<Transaction> getListAllTransactions();
	ObservableList<TransactionTableView.TransactionModel> getObservableTransactionModelsList();
}
