package com.moneymanager.ui.view;

import com.moneymanager.ui.event.FormEvent;
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
	
	protected ValidationSupport validationSupport;

	protected T currentModel;
	protected BooleanProperty isModified = new SimpleBooleanProperty(false);
	protected BooleanProperty isValid = new SimpleBooleanProperty(false);
	protected BooleanBinding isSaveable;
	
	protected Button saveButton;
	protected Button closeButton;
	protected Button deleteButton;
	//private FloatingActionButton fabButton;
	protected HBox buttonBox;
	protected HBox leftButtonBox;
	protected HBox rightButtonBox;
	protected ObservableMap<String, Control> fieldMap = FXCollections.observableHashMap();
	protected ObservableMap<String, BooleanProperty> fieldModifiedMap = FXCollections.observableHashMap();
	
	protected AbstractForm() {
		setupLayout();
		validationSupport = new ValidationSupport();
		setupValidation();
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

	private void updateModifiedBinding() {
		if (isModified.isBound()) {
			isModified.unbind();
		}
		
		if (fieldModifiedMap.isEmpty()) {
			isModified.set(false);
			return;
		}
		
		List<BooleanProperty> props = new ArrayList<>(fieldModifiedMap.values());
		
		// For a single field, directly bind to its property
		if (props.size() == 1) {
			isModified.bind(props.get(0));
			return;
		}
		
		// For multiple fields, use Bindings utility class
		BooleanBinding result = Bindings.or(props.get(0), props.get(1));
		
		// Add remaining properties
		for (int i = 2; i < props.size(); i++) {
			result = Bindings.or(result, props.get(i));
		}
		
		// Bind to the combined result
		isModified.bind(result);
	}
	
	protected void resetModifiedFlags() {
		// Unbind isModified first if needed
		if (isModified.isBound()) {
			BooleanProperty newProp = new SimpleBooleanProperty(false);
			isModified = newProp;
		} else {
			isModified.set(false);
		}
		
		// Reset all field flags
		for (BooleanProperty prop : fieldModifiedMap.values()) {
			if (!prop.isBound()) {
				prop.set(false);
			}
		}
		
		// Rebuild the binding
		updateModifiedBinding();
	}
	
	protected void setupValidation() {
		validationSupport.validationResultProperty().addListener((o, oldValue, newValue) -> {
			isValid.set(newValue.getErrors().isEmpty());
		});
		// Create custom validation decorator with border-only styling
		ValidationDecoration customValidation = new ValidationDecoration() {
			@Override
			public void applyRequiredDecoration(Control target) {
				// Skip required decoration
			}
			
			@Override
			public void applyValidationDecoration(ValidationMessage message) {
				Control control = message.getTarget();
				if (message.getSeverity() == Severity.ERROR) {
					if (!control.getStyleClass().contains("error-border")) {
						control.getStyleClass().add("error-border");
					}
				}
			}
			/*        validationSupport.setValidationDecorator(
            new StyleClassValidationDecoration("validation-error", null)
        );*/
			@Override
			public void removeDecorations(Control target) {
				target.getStyleClass().remove("error-border");
			}
			
			public void applySuccessDecoration(Control target) {
				target.getStyleClass().remove("error-border");
				if (!target.getStyleClass().contains("success-border")) {
					target.getStyleClass().add("success-border");
				}
			}
		};
//		validationSupport.validationResultProperty().addListener(obs, oldVal, newVal) -> validProperty.set(!newVal.getErrors().isEmpty()));
		
		// Set the custom decorator
		validationSupport.setValidationDecorator(customValidation);
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
		closeButton.setOnAction(e -> onCloseAction());
		
		
		
		leftButtonBox = new HBox(deleteButton);
		leftButtonBox.setAlignment(Pos.CENTER_LEFT);
		
		rightButtonBox = new HBox(10, saveButton, closeButton);
		rightButtonBox.setAlignment(Pos.CENTER_RIGHT);
		
		buttonBox = new HBox(20, leftButtonBox, rightButtonBox);
		buttonBox.setAlignment(Pos.CENTER);
		
		this.getChildren().add(buttonBox);
	}
	
	protected void registerField(String fieldName, Control field) {
		fieldMap.put(fieldName, field);
		
		BooleanProperty fieldModified = new SimpleBooleanProperty(false);
		fieldModifiedMap.put(fieldName, fieldModified);
		
		if (field instanceof TextField textField) {
			textField.textProperty().addListener((obs, oldValue, newValue) -> {
				fieldModified.set(true);
				validationSupport.revalidate();
			});
		} else if (field instanceof ComboBox<?> comboBox) {
			comboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
				fieldModified.set(true);
				validationSupport.revalidate();
			});
		} // Add other control types as needed
		
		updateModifiedBinding();
		
	}
	
	protected void fireSaveEvent() {
		// Check if form is valid and modified
		if (isSaveable.get()) {
			// Collect current field values
			Map<String, Object> values = getModifiedFieldValues();
			
			// Create and fire the save event with field values
			FormEvent<T> saveEvent = new FormEvent<>(FormEvent.SAVE, currentModel, values);
			fireEvent(saveEvent);
			// Reset modified flags after firing event
			resetModifiedFlags();
		}
	}
	
	// Helper method to collect all field values
	protected Map<String, Object> collectFieldValues() {
		Map<String, Object> values = new HashMap<>();
		
		for (Map.Entry<String, Control> entry : fieldMap.entrySet()) {
			String fieldName = entry.getKey();
			Control control = entry.getValue();
			if (fieldModifiedMap.containsKey(fieldName)) {
				if (control instanceof TextField textField) {
					values.put(fieldName, textField.getText());
				} else if (control instanceof ComboBox<?> comboBox) {
					values.put(fieldName, comboBox.getValue());
				} else if (control instanceof DatePicker datePicker) {
					values.put(fieldName, datePicker.getValue());
				} else if (control instanceof CheckBox checkBox) {
					values.put(fieldName, checkBox.isSelected());
				}
				// Add more control types as needed
			}
		}
		
		return values;
	}
	
	public Map<String, Object> getModifiedFieldValues() {
		Map<String, Object> modifiedValues = new HashMap<>();
		
		for (Map.Entry<String, BooleanProperty> entry : fieldModifiedMap.entrySet()) {
			String fieldName = entry.getKey();
			BooleanProperty modifiedProperty = entry.getValue();
			
			if (modifiedProperty.get()) {
				Control control = fieldMap.get(fieldName);
				if (control != null) {
					if (control instanceof TextField textField) {
						modifiedValues.put(fieldName, textField.getText());
					} else if (control instanceof ComboBox<?> comboBox) {
						modifiedValues.put(fieldName, comboBox.getValue());
					} else if (control instanceof DatePicker datePicker) {
						modifiedValues.put(fieldName, datePicker.getValue());
					} else if (control instanceof CheckBox checkBox) {
						modifiedValues.put(fieldName, checkBox.isSelected());
					}
					// Add other control types as needed
				}
			}
		}
		
		return modifiedValues;
	}
	
	
	public void setCurrentModel(T selectedModel) {
		this.currentModel = selectedModel;
		if (!this.isVisible()) {
			this.setVisible(true);
			this.setManaged(true);
		}
		loadModelDataIntoForm(selectedModel);
	}
	
	public Control getField(String fieldName) {
		return fieldMap.get(fieldName);
	}
	
	private void setupSaveableBinding() {
		// Combine isValid and isModified into a single binding
		isSaveable = isValid.and(isModified);
		
		// Bind save button's disabled state to NOT saveable
		saveButton.disableProperty().bind(isSaveable.not());
	}
	public abstract void openCreationDialog();
	
	protected void onCloseAction() {
		hideForm();
	
	}
	public void hideForm() {
		this.setVisible(false);
		this.setManaged(false);
	}
	
	protected abstract void onSaveAction();
	protected abstract void onDeleteAction();
	protected abstract void loadModelDataIntoForm(T model);
	
}
	
	


