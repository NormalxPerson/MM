package com.moneymanager.ui.controller;

import com.moneymanager.ui.event.FormEvent;
import com.moneymanager.ui.view.AbstractForm;
import com.moneymanager.ui.view.FloatingActionButton;
import javafx.fxml.Initializable;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.slf4j.helpers.FormattingTuple;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractViewController implements Initializable, BaseViewController {
	
	protected AbstractForm editingForm;
	protected AbstractForm creationDialogForm;
	protected TableView tableView;
	protected VBox container;
	protected FloatingActionButton floatingActionButton;
	
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	protected void setupRowSelection() {
		tableView.setRowFactory(tv -> {
			TableRow<?> row = new TableRow<>(); // Use wildcard as type is generic
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getClickCount() == 1) {
					handleRowClick(row, event);
				}
			});
			return row;
		});
	}
	
	protected abstract void handleRowClick(TableRow<?> row, MouseEvent event);
	
	protected void setFloatingActionButton(FloatingActionButton floatingActionButton) {
		this.floatingActionButton = floatingActionButton;
	}
	
	protected void setUpHandlers() {
		editingForm.addEventHandler(FormEvent.SAVE, this::handleSaveEvent);
		editingForm.addEventHandler(FormEvent.DELETE, this::handleDeleteEvent);
		editingForm.addEventHandler(FormEvent.CLOSE, this::handleCloseEvent);
	}
	
	
	@Override
	public void hideForm() {
		editingForm.hideForm();
	}
	
	
	@Override
	public void unselectRow() {
		tableView.getSelectionModel().clearSelection();
	}
	
	@Override
	public void showCreationDialog() {
		//creationDialogForm.resetFormFields();
		creationDialogForm.openCreationDialog();	}
	
	public VBox getContainer() { // Generic getContainer method
		return this.container;
	}
	
	protected abstract <T> void handleSaveEvent(FormEvent<T> formSaveEvent);
	protected abstract <T> void handleDeleteEvent(FormEvent<T> formDeleteEvent);
	protected abstract <T> void handleCloseEvent(FormEvent<T> formCloseEvent);
}
