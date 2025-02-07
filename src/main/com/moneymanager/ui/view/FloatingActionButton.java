package com.moneymanager.ui.view;

import javafx.scene.control.Button;

public class FloatingActionButton extends Button {
	
	private static FloatingActionButton instance;
	
	public FloatingActionButton() {
		super("+");
		this.getStyleClass().add("floating-action-button");
		this.getStyleClass().add("md3-rounded-large");
	}
	
	
	public static FloatingActionButton getInstance() {
		if (instance == null) {
			instance = new FloatingActionButton();
		}
		return instance;
	}
	
	public void showFab(){
		this.setVisible(true);
	}
	
	public void hideFab(){
		this.setVisible(false);
	}
	

}
