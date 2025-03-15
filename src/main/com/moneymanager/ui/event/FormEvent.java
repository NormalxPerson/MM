package com.moneymanager.ui.event;

import javafx.event.Event;
import javafx.event.EventType;

import java.util.Map;

public class FormEvent<T> extends Event {
	public static final EventType<FormEvent> ANY = new EventType<>(Event.ANY, "FORM");
	public static final EventType<FormEvent> SAVE = new EventType<>(ANY, "SAVE");
	public static final EventType<FormEvent> DELETE = new EventType<>(ANY, "DELETE");
	public static final EventType<FormEvent> CLOSE = new EventType<>(ANY, "CLOSE");
	
	private final T model;
	private final Map<String, Object> fieldValues;
	
	public FormEvent(EventType<? extends FormEvent> eventType, T model) {
		this(eventType, model, null);
	}
	
	public FormEvent(EventType<? extends FormEvent> eventType, T model, Map<String, Object> fieldValues) {
		super(eventType);
		this.model = model;
		this.fieldValues = fieldValues;
	}
	
	public T getModel() {
		return model;
	}
	
	public Map<String, Object> getFieldValues() {
		return fieldValues;
	}
}

