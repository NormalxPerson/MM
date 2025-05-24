package com.moneymanager.ui.interactor;

import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.viewModel.CSVParserViewModel;

public class CSVParserInteractor {
	private final TransactionService transactionService;
	private final CSVParserViewModel viewModel;
	
	public CSVParserInteractor(TransactionService transactionService, CSVParserViewModel viewModel) {
		this.transactionService = transactionService;
		this.viewModel = viewModel;
	}
	
}
