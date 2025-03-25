package com.moneymanager.ui.validation;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.Map;

public class FieldChangeTracker {
	
	private final ObservableMap<String, Control> fieldMap = FXCollections.observableHashMap();
	private final Map<String, BooleanProperty> fieldModifiedMap = new HashMap<>();
	private final Map<String, Object> originalValues = new HashMap<>();
	
	private final BooleanProperty anyFieldModified = new SimpleBooleanProperty();
	
	private static final String MODIFIED_STYLE_CLASS = "field-modified";
	
	public BooleanProperty registerField(String fieldName, Control field) {
		fieldMap.put(fieldName, field);
		
		// Create property to track modification state
		BooleanProperty fieldModified = new SimpleBooleanProperty(false);
		fieldModifiedMap.put(fieldName, fieldModified);
		
		// Track the original value
		captureOriginalValue(fieldName, field);
		
		// Setup listeners based on control type
		setupChangeListener(fieldName, field, fieldModified);
		
		// Update visual state when modified
		fieldModified.addListener((obs, oldVal, newVal) -> {
			updateFieldStyle(field, newVal);
			updateAnyModifiedState();
		});
		
		return fieldModified;
	}
	
	private void captureOriginalValue(String fieldName, Control field) {
		Object value = getFieldValue(field);
		originalValues.put(fieldName, value);
	}
	
	private void setupChangeListener(String fieldName, Control field, BooleanProperty modified) {
		if (field instanceof TextField textField) {
			textField.textProperty().addListener(
					(obs, oldVal, newVal) -> checkIfModified(fieldName, field, modified));
		}
		else if (field instanceof ComboBox<?> comboBox) {
			comboBox.valueProperty().addListener(
					(obs, oldVal, newVal) -> checkIfModified(fieldName, field, modified));
		}
		else if (field instanceof DatePicker datePicker) {
			datePicker.valueProperty().addListener(
					(obs, oldVal, newVal) -> checkIfModified(fieldName, field, modified));
		}
		else if (field instanceof CheckBox checkBox) {
			checkBox.selectedProperty().addListener(
					(obs, oldVal, newVal) -> checkIfModified(fieldName, field, modified));
		}
	}
	
	private void checkIfModified(String fieldName, Control field, BooleanProperty modified) {
		Object currentValue = getFieldValue(field);
		Object originalValue = originalValues.get(fieldName);
		
		// Compare current value with original
		boolean hasChanged = !areValuesEqual(currentValue, originalValue);
		modified.set(hasChanged);
	}
	
	private boolean areValuesEqual(Object value1, Object value2) {
		if (value1 == null && value2 == null) return true;
		if (value1 == null || value2 == null) return false;
		return value1.equals(value2);
	}
	
	private void updateAnyModifiedState() {
		boolean anyModified = fieldModifiedMap.values().stream().anyMatch(BooleanProperty::get);
		anyFieldModified.set(anyModified);
	}
	
	private void updateFieldStyle(Control field, boolean isModified) {
		if (isModified) {
			if (!field.getStyleClass().contains(MODIFIED_STYLE_CLASS)) {
				field.getStyleClass().add(MODIFIED_STYLE_CLASS);
			}
		} else {
			field.getStyleClass().remove(MODIFIED_STYLE_CLASS);
		}
	}
	
	private Object getFieldValue(Control field) {
		if (field instanceof TextField textField) {
			return textField.getText();
		}
		else if (field instanceof ComboBox<?> comboBox) {
			return comboBox.getSelectionModel().getSelectedItem();
		}
		else if (field instanceof DatePicker datePicker) {
			return datePicker.getValue();
		}
		else if (field instanceof CheckBox checkBox) {
			return checkBox.isSelected();
		}
		// Add other control types as needed
		return null;
	}
	
	public Map<String, Object> getModifiedValues() {
		Map<String, Object> modifiedValues = new HashMap<>();
		
		for (String fieldName : fieldMap.keySet()) {
			if (fieldModifiedMap.get(fieldName).get()) {
				Control field = fieldMap.get(fieldName);
				modifiedValues.put(fieldName, getFieldValue(field));
			}
		}
		
		return modifiedValues;
	}
	
	public void resetModifications() {
		// Update original values and reset modification flags
		for (String fieldName : fieldMap.keySet()) {
			Control field = fieldMap.get(fieldName);
			captureOriginalValue(fieldName, field);
			BooleanProperty prop = fieldModifiedMap.get(fieldName);
			prop.set(false);
		}
		
		// Reset the combined property
		anyFieldModified.set(false);
	}
	
	public ReadOnlyBooleanProperty anyFieldModifiedProperty() {
		return anyFieldModified;
	}
	
	public boolean isFieldModified(String fieldName) {
		BooleanProperty prop = fieldModifiedMap.get(fieldName);
		return prop != null && prop.get();
	}
	
	public ReadOnlyBooleanProperty fieldModifiedProperty(String fieldName) {
		return fieldModifiedMap.get(fieldName);
	}
	
	
	
	
	
	
}
