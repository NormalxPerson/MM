package com.moneymanager.ui.view;

import com.moneymanager.ui.event.FormClosedEvent;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SlidingForm<T> extends VBox {
	public enum FormStatus {
		CLOSED, EDITING, ADDING
	}
	protected T currentModel;
	
	protected FormStatus status;
	
	protected Button saveButton;
	protected Button closeButton;
	protected Button cancelButton;
	protected Button addButton;
	protected Button deleteButton;
	
	protected HBox buttonBox;
	protected HBox leftButtonBox;
	protected HBox rightButtonBox;
	
	public SlidingForm() {
		this.setSpacing(10);
		this.setPadding(new Insets(10));
		this.setAlignment(Pos.CENTER);
		this.getStyleClass().add("sliding-form");

		addButton = new Button("Add");
		saveButton = new Button("Save");
		closeButton = new Button("Close");
		cancelButton = new Button("Cancel");
		deleteButton = new Button("Delete");
		
		addButton.getStyleClass().addAll("button", "md3-rounded-medium");
		saveButton.getStyleClass().addAll("button", "md3-rounded-medium");
		closeButton.getStyleClass().addAll("button", "md3-rounded-medium"); // Apply button styles and rounded corners
		cancelButton.getStyleClass().addAll("button", "md3-rounded-medium");
		deleteButton.getStyleClass().addAll("button", "md3-rounded-medium", "delete-button");
		
		addButton.setOnAction(e -> onAddAction());
		saveButton.setOnAction(e -> onSaveAction());
		cancelButton.setOnAction(e -> onCancelAction());
		deleteButton.setOnAction(e -> onDeleteAction());
		
		closeButton.setOnAction(event -> {
			
			this.status = FormStatus.CLOSED;
			hideForm();
			this.currentModel = null;
			Event.fireEvent(this, new FormClosedEvent());
		});
		
		leftButtonBox = new HBox(deleteButton);
		leftButtonBox.setAlignment(Pos.CENTER_LEFT);
		
		rightButtonBox = new HBox(10, saveButton, closeButton);
		rightButtonBox.setAlignment(Pos.CENTER_RIGHT);
		
		buttonBox = new HBox(20, leftButtonBox, rightButtonBox);
		buttonBox.setAlignment(Pos.CENTER);
		
		this.getChildren().add(buttonBox);
		
		this.setVisible(false);
		this.setManaged(false);
		this.status = FormStatus.CLOSED;
		
	}
	
	protected void initializeLayout() {
		//this.getChildren().add(buttonBox);
		updateButtonBox();
	}
	
	public void showForm(FormStatus status, T model) {
		setFormStatus(status);
		updateSelectedModel(model);
		loadModelDataIntoForm(this.currentModel);
		this.setVisible(true);
		this.setManaged(true);
	}
	
	private void updateButtonBox() {
		leftButtonBox.getChildren().clear();
		rightButtonBox.getChildren().clear();
		switch (status) {
			case EDITING:
				leftButtonBox.getChildren().add(deleteButton);
				rightButtonBox.getChildren().addAll(addButton, saveButton, closeButton);
				break;
			case ADDING:
				rightButtonBox.getChildren().addAll(saveButton, cancelButton);
				break;
			case CLOSED:
				rightButtonBox.getChildren().addAll(saveButton, closeButton);
				break;
		}
	}

	protected void updateSelectedModel(T model) {
		this.currentModel = model;
	}
	
	protected void setFormStatus(FormStatus status) {
		this.status = status;
		updateButtonBox();
		if (status == FormStatus.CLOSED) {
			currentModel = null;
		}
	}
	
	public FormStatus getFormStatus() {
		return this.status;
	}
	
	public abstract void hideForm();
	
	protected abstract void loadModelDataIntoForm(T model);
	
	protected abstract void onAddAction();
	protected abstract void onSaveAction();
	protected abstract void onCancelAction();
	protected abstract void onDeleteAction();
	
	protected Map<String, String> validateFields(Map<String, String> fieldValues, Map<String, List<String>> constraints) {
		Map<String, String> errors = new HashMap<>();
		
		for (Map.Entry<String, String> entry : fieldValues.entrySet()) {
			String field = entry.getKey();
			String value = entry.getValue();
			List<String> rules = constraints.get(field);
			
			if (rules != null) {
				for (String rule : rules) {
					if (rule.equals("required") && (value == null || value.trim().isEmpty())) {
						errors.put(field, field + " cannot be empty.");
					} else if (rule.equals("double")) {
						try {
							Double.parseDouble(value);
						} catch (NumberFormatException e) {
							errors.put(field, field + " must be a valid number.");
						}
					} else if (rule.startsWith("options:")) {
						List<String> options = List.of(rule.replace("options:", "").split(","));
						if (!options.contains(value)) {
							errors.put(field, field + " must be one of: " + options);
						}
					}
				}
			}
		}
		
		return errors;
	}
	
	protected void showValidationErrors(Map<String, String> errors, Map<String, Control> fieldMap) {
		// Reset field styles
		for (Control field : fieldMap.values()) {
			field.setStyle("");
		}
		
		// Apply error styles
		for (Map.Entry<String, String> entry : errors.entrySet()) {
			if (fieldMap.containsKey(entry.getKey())) {
				fieldMap.get(entry.getKey()).setStyle("-fx-border-color: red; -fx-border-width: 2px;");
			}
		}
	}
	
	protected Map<String, String> hasModelChanged(Map<String, String> oldValues, Map<String, String> newValues) {
		Map<String, String> changes = new HashMap<>();
		
		for (String field : oldValues.keySet()) {
			if (!oldValues.get(field).equals(newValues.get(field))) {
				changes.put(field, "Changed " + field);
			}
		}
		
		return changes;
	}
	
}

