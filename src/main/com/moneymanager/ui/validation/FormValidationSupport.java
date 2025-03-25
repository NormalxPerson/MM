package com.moneymanager.ui.validation;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.decoration.GraphicValidationDecoration;
import org.controlsfx.validation.decoration.ValidationDecoration;

import java.util.HashMap;
import java.util.Map;

public class FormValidationSupport extends ValidationSupport {
	
	private final Map<Control, Label> errorLabels = new HashMap<>();
	private final Map<Control, BooleanProperty> fieldValidState = new HashMap<>();
	
	private static final String ERROR_STYLE_CLASS = "validation-error";
	private static final String SUCCESS_STYLE_CLASS = "validation-success";
	
	private final EnhancedValidationDecoration decorator = new EnhancedValidationDecoration();
	
	public FormValidationSupport() {
		super();
		setValidationDecorator(decorator);
	}
	
	public BooleanProperty registerField(Control field, VBox container) {
		Label errorLabel = new Label();
		errorLabel.getStyleClass().add("error-label");
		errorLabel.setVisible(false);
		errorLabel.setManaged(false);
		
		container.getChildren().add(errorLabel);
		errorLabels.put(field, errorLabel);
		
		BooleanProperty isValid = new SimpleBooleanProperty(true);
		fieldValidState.put(field, isValid);
		
		return isValid;
	}
	
	private class EnhancedValidationDecoration implements ValidationDecoration {
		
		private final GraphicValidationDecoration graphicDecoration = new GraphicValidationDecoration();
		
		@Override
		public void removeDecorations(Control target) {
			target.getStyleClass().remove(ERROR_STYLE_CLASS);
			Label errorLabel = errorLabels.get(target);
			if (errorLabel != null) {
				errorLabel.setVisible(false);
				errorLabel.setManaged(false);
				errorLabel.setText("");
			}
			if (fieldValidState.containsKey(target)) {
				fieldValidState.get(target).set(true);
				
				if (!target.getStyleClass().contains(SUCCESS_STYLE_CLASS)) {
					target.getStyleClass().add(SUCCESS_STYLE_CLASS);
				}
			}
		}
		
		@Override
		public void applyValidationDecoration(ValidationMessage message) {
			Control control = message.getTarget();
			
			if (message.getSeverity() == Severity.ERROR) {
				control.getStyleClass().remove(SUCCESS_STYLE_CLASS);
				if (!control.getStyleClass().contains(ERROR_STYLE_CLASS)) {
					control.getStyleClass().add(ERROR_STYLE_CLASS);
				}
				
				fieldValidState.get(control).set(false);
			}
		}
		
		@Override
		public void applyRequiredDecoration(Control target) {
		
		}
	}
	
	public Map<Control, String> getErrorMessages() {
		Map<Control, String> errors = new HashMap<>();
		
		for (ValidationMessage message : getValidationResult().getMessages()) {
			if (message.getSeverity() == Severity.ERROR) {
				errors.put(message.getTarget(), message.getText());
			}
		}
		
		return errors;
	}
	
	public void resetValidation() {
		for (Control field : errorLabels.keySet()) {
			decorator.removeDecorations(field);
		}
	}
}


	

