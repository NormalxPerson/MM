package com.moneymanager.ui.event;

import javafx.event.Event;
import javafx.event.EventType;

public class FormClosedEvent extends Event {
	
	public static final EventType<FormClosedEvent> FORM_CLOSED = new EventType<>(Event.ANY, "FORM_CLOSED");
	
	public FormClosedEvent() {
		super(FORM_CLOSED);
	}
	
	@Override
	public EventType<? extends FormClosedEvent> getEventType() {
		return FORM_CLOSED;
	}
}
