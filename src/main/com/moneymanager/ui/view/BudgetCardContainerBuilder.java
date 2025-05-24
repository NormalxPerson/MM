package com.moneymanager.ui.view;

import com.moneymanager.ui.viewModel.BudgetOverviewMod;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.util.Builder;
import javafx.util.StringConverter;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BudgetCardContainerBuilder implements Builder<ScrollPane> {
	
	private final BudgetOverviewMod viewModel;
	private ComboBox<YearMonth> yearMonthComboBox;
	
	private static final DateTimeFormatter MMMM_YY_FORMATTER = DateTimeFormatter.ofPattern("MMMM yy");
	
	public BudgetCardContainerBuilder(BudgetOverviewMod viewModel) {
		if (viewModel == null) {
			throw new IllegalArgumentException("BudgetOverviewMod cannot be null");
		}
		this.viewModel = viewModel;
	}
	
	@Override
	public ScrollPane build() {
/*		VBox mainContainer = new VBox();
		mainContainer.setSpacing(10);
		mainContainer.setPadding(new Insets(15));*/
		
		
		return createScrollPane();
	}
	
	public Node buildHeaderBox() {
		Label titleLabel = new Label("Budget For:");
		titleLabel.getStyleClass().add("label");
		
		yearMonthComboBox = new ComboBox<>();
		yearMonthComboBox.setItems(generateYearMonths());
		yearMonthComboBox.setConverter(createYearMonthConverter());
		yearMonthComboBox.getStyleClass().add("text-field");
		
		setupBindings();
		
		HBox headerBox = new HBox(10, titleLabel, yearMonthComboBox);
		headerBox.setAlignment(Pos.CENTER_LEFT);
		headerBox.setPadding(new Insets(0,0,10,0));

		return headerBox;
	}
	
	private ScrollPane createScrollPane() {
		TilePane cardsContainer = new TilePane();
		cardsContainer.setPadding(new Insets(5,0,5,0));
		cardsContainer.setHgap(10);
		cardsContainer.setVgap(10);
		cardsContainer.setPrefColumns(3);
		cardsContainer.setTileAlignment(Pos.TOP_LEFT);
		cardsContainer.setStyle("-fx-border-color: red; -fx-border-width: 2;");
		
		cardsContainer.setPrefTileWidth(Region.USE_COMPUTED_SIZE);
		
		
		Bindings.bindContent(cardsContainer.getChildren(),viewModel.getCategoryCards());
		
		ScrollPane scrollPane = new ScrollPane(cardsContainer);
		scrollPane.setFitToWidth(true);
		scrollPane.setPannable(false);
		
		//scrollPane.setFitToHeight(true);
		//scrollPane.setFitToWidth(true);
		//scrollPane.setPrefViewportHeight(Double.MAX_VALUE);
		//scrollPane.setPrefViewportWidth(Double.MAX_VALUE);
		//scrollPane.setMaxHeight(Double.MAX_VALUE);
		//scrollPane.setMaxWidth(Double.MAX_VALUE);
		//scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		//scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPane.setPannable(false);
		
		//scrollPane.setStyle("-background-color: -fx-md3-surface-color; -fx-background: -fx-md3-surface-color;");
		//cardsContainer.setStyle("-fx-background-color: transparent;");
		scrollPane.setStyle("-fx-border-color: green; -fx-border-width: 2; -fx-background-color: -fx-md3-surface-color; -fx-background: -fx-md3-surface-color;"); // Add border
		return scrollPane;
	}
	
	private void setupBindings() {
		yearMonthComboBox.valueProperty().bindBidirectional(viewModel.selectedYearMonthProperty());
	}
	
	private ObservableList<YearMonth> generateYearMonths() {
		List<YearMonth> months = new ArrayList<>();
		
		for (int i = 1; i <= 12; i++) {
			months.add(YearMonth.of(YearMonth.now().getYear(), i));
		}
		return FXCollections.observableArrayList(months);
	}
	
	private StringConverter<YearMonth> createYearMonthConverter() {
		return new StringConverter<>() {
			@Override
			public String toString(YearMonth yearMonth) {
				return yearMonth.format(MMMM_YY_FORMATTER);
			}
			
			@Override
			public YearMonth fromString(String string) {
				try {
					return YearMonth.parse(string, MMMM_YY_FORMATTER);
				} catch (Exception e) {
					return null;
				}
			}
		};
	}
}
