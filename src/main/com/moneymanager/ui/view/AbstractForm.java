package com.moneymanager.ui.view;

import com.moneymanager.ui.event.FormEvent;
import com.moneymanager.ui.validation.FieldChangeTracker;
import com.moneymanager.ui.validation.FormValidationSupport;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.decoration.ValidationDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractForm<T> extends VBox {
	
	protected FormValidationSupport validationSupport;
	protected FieldChangeTracker fieldChangeTracker;

	protected T currentModel;
	protected BooleanProperty isModified = new SimpleBooleanProperty(false);
	protected BooleanProperty isValid = new SimpleBooleanProperty(false);
	protected BooleanBinding isSaveable;
	
	protected Button saveButton;
	protected Button closeButton;
	protected Button deleteButton;
	protected HBox buttonBox;
	protected HBox leftButtonBox;
	protected HBox rightButtonBox;
	
	protected ObservableMap<String, Control> fieldMap = FXCollections.observableHashMap();
	protected Map<String, VBox> fieldContainerMap = new HashMap<>();
	
	protected AbstractForm() {
		setupLayout();
		initializeValidationAndTracking();
		createButtons();
		setupSaveableBinding();
		
		this.setVisible(false);
		this.setManaged(false);
		
	}
	
	private void setupLayout() {
		this.setSpacing(10);
		this.setPadding(new Insets(15));
		this.setAlignment(Pos.TOP_LEFT);
		this.getStyleClass().add("form-container");
	}
	
	private void initializeValidationAndTracking() {
		validationSupport = new FormValidationSupport();
		fieldChangeTracker = new FieldChangeTracker();
		
		validationSupport.validationResultProperty().addListener((observable, oldValue, newValue) -> {
			isValid.set(newValue.getErrors().isEmpty());
		});
		
		isModified.bind(fieldChangeTracker.anyFieldModifiedProperty());
	}
	
	private void createButtons(){
		saveButton = new Button("Save");
		closeButton = new Button("Close");
		deleteButton = new Button("Delete");
		
		saveButton.getStyleClass().addAll("button", "md3-rounded-medium");
		closeButton.getStyleClass().addAll("button", "md3-rounded-medium"); // Apply button styles and rounded corners
		deleteButton.getStyleClass().addAll("button", "md3-rounded-medium", "delete-button");
		
		saveButton.setOnAction(e -> fireSaveEvent());
		deleteButton.setOnAction(e -> onDeleteAction());
		closeButton.setOnAction(e -> fireCloseEvent());
		
		leftButtonBox = new HBox(deleteButton);
		leftButtonBox.setAlignment(Pos.CENTER_LEFT);
		
		rightButtonBox = new HBox(10, saveButton, closeButton);
		rightButtonBox.setAlignment(Pos.CENTER_RIGHT);
		
		buttonBox = new HBox(20, leftButtonBox, rightButtonBox);
		buttonBox.setAlignment(Pos.CENTER);
		
		this.getChildren().add(buttonBox);
	}
	
	private void setupSaveableBinding() {
		isSaveable = isValid.and(isModified);
		saveButton.disableProperty().bind(isSaveable.not());
	}
	
	protected void registerField(String fieldName, Control field, VBox container) {
		validationSupport.registerField(field,container);
		fieldChangeTracker.registerField(fieldName,field);
		
		fieldMap.put(fieldName, field);
		fieldContainerMap.put(fieldName, container);
	}
	
	protected void fireSaveEvent() {
		// Check if form is valid and modified
		if (isSaveable.get()) {
			// Collect current field values
			Map<String, Object> values = fieldChangeTracker.getModifiedValues();
			
			// Create and fire the save event with field values
			FormEvent<T> saveEvent = new FormEvent<>(FormEvent.SAVE, currentModel, values);
			fireEvent(saveEvent);
			// Reset modified flags after firing event
			fieldChangeTracker.resetModifications();		}
	}
	
	protected void fireDeleteEvent() {
		FormEvent<T> deleteEvent = new FormEvent<>(FormEvent.DELETE, currentModel);
		fireEvent(deleteEvent);
	}
	
	public void fireCloseEvent() {
		FormEvent<T> closeEvent = new FormEvent<>(FormEvent.CLOSE, currentModel);
		fireEvent(closeEvent);
		hideForm();
	}
	
	public void setCurrentModel(T selectedModel) {
		this.currentModel = selectedModel;
		if (!this.isVisible()) {
			this.setVisible(true);
			this.setManaged(true);
		}
		loadModelDataIntoForm(selectedModel);
		
		fieldChangeTracker.resetModifications();
		validationSupport.revalidate();
	}
	
	public Control getField(String fieldName) {
		return fieldMap.get(fieldName);
	}
	
	public abstract void openCreationDialog();
	
	public void hideForm() {
		this.currentModel = null;
		this.setVisible(false);
		this.setManaged(false);
		fieldChangeTracker.resetModifications();
	}
	protected abstract void onSaveAction();
	
	protected abstract void setupValidators();
	protected abstract void onDeleteAction();
	protected abstract void loadModelDataIntoForm(T model);
	
}
	
	


