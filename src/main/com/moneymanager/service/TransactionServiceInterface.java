package com.moneymanager.service;

import com.moneymanager.core.Transaction;
import com.moneymanager.ui.view.TransactionTableView;
import javafx.collections.ObservableList;

import java.util.List;

public interface TransactionServiceInterface {
	void createTransactionFromUser(double amount, String description, String date, String type, String accountId);
	List<Transaction> getListAllTransactions();
	ObservableList<TransactionTableView.TransactionModel> getObservableTransactionModelsList();
}
