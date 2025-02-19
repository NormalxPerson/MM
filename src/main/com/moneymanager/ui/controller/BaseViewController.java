package com.moneymanager.ui.controller;

public interface BaseViewController {
	void hideForm();
	void showForm();
	void setFormStatus(boolean status);
	void unselectRow();
	void setFormForBlankModel();
	void selectBlankRow();
}
