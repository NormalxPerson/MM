package com.moneymanager.ui.controller;

import com.moneymanager.ui.event.FormOpenedEvent;
import com.moneymanager.ui.view.AbstractSlidingForm;
import javafx.event.Event;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public abstract class AbstractViewController implements BaseViewController {
	
	protected AbstractSlidingForm slidingForm; // Declare slidingForm here, subclasses will initialize it
	protected TableView tableView; // Declare tableView here, subclasses will initialize it
	protected VBox container; // Declare container here, subclasses will initialize it
	
	
	protected void setupRowSelection() {
		tableView.setRowFactory(tv -> {
			TableRow<?> row = new TableRow<>(); // Use wildcard as type is generic
			row.setOnMouseClicked(event -> {
				if (slidingForm.getFormStatus() == AbstractSlidingForm.FormStatus.ADDING) {
					// Prevent selection of any row while adding
					event.consume(); // Consume the event to stop further processing
					selectBlankRow();
					return;
				}
				if (!row.isEmpty() && event.getClickCount() == 1) {
					if (slidingForm.getFormStatus().equals(AbstractSlidingForm.FormStatus.CLOSED)) {
						container.fireEvent(new FormOpenedEvent()); // Use container from abstract class
					}
					handleRowClick(row, event);
				}
			});
			return row;
		});
	}
	
	// Abstract method to handle row click in subclasses
	protected abstract void handleRowClick(TableRow<?> row, MouseEvent event);
	
	
	@Override
	public void hideForm() {
		slidingForm.hideForm();
	}
	
	
	@Override
	public void unselectRow() {
		tableView.getSelectionModel().clearSelection();
	}
	
	@Override
	public void setFormForBlankModel() {
		slidingForm.setUpForAddingModel();
	}
	
	@Override
	public void selectBlankRow() {
		int row = tableView.getItems().size() - 1;
		if (row >= 0) {
			tableView.getSelectionModel().select(row);
		}
	}
	
	public VBox getContainer() { // Generic getContainer method
		return this.container;
	}
}
