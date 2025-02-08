package com.moneymanager.ui.event;

import javafx.event.Event;
import javafx.event.EventType;

public class FormOpenedEvent extends Event {
	
	public static final EventType<com.moneymanager.ui.event.FormOpenedEvent> FORM_OPENED = new EventType<>(Event.ANY, "FORM_OPENED");
	
	public FormOpenedEvent() {
			super(FORM_OPENED);
		}
		
		@Override
		public EventType<? extends com.moneymanager.ui.event.FormOpenedEvent> getEventType() {
			return FORM_OPENED;
		}
	}

