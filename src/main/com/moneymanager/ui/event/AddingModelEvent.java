package com.moneymanager.ui.event;

import javafx.event.Event;
import javafx.event.EventType;

public class AddingModelEvent extends Event {
	public static final EventType<AddingModelEvent> ADDING_MODEL = new EventType<>(Event.ANY, "ADDING_MODEL");
	
	public AddingModelEvent() {
		super(ADDING_MODEL);
	}
	
	@Override
	public EventType<? extends AddingModelEvent> getEventType() {
		return ADDING_MODEL;
	}
}

