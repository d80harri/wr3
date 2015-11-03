package net.d80harri.wr.ui.components;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.StackPaneBuilder;
import javafx.scene.text.Font;

public class FittingHeightTextArea extends StackPane {
	private Label label;
	private TextArea textArea;
	private Character enterChar = new Character((char) 10);
	private Region content;
	private SimpleDoubleProperty contentHeight = new SimpleDoubleProperty();

	private final double NEW_LINE_HEIGHT = 18D;
	private final double TOP_PADDING = 3D;
	private final double BOTTOM_PADDING = 6D;

	public FittingHeightTextArea() {
		super();
		configure();
	}

	private void configure() {
		setAlignment(Pos.TOP_LEFT);

		this.textArea = new TextArea() {
			@Override
			protected void layoutChildren() {
				super.layoutChildren();
				if (content == null) {
					content = (Region) lookup(".content");
					content.getStyleClass().add("input-label");
					contentHeight.bind(content.heightProperty());
					content.heightProperty().addListener(
							new ChangeListener<Number>() {
								@Override
								public void changed(
										ObservableValue<? extends Number> paramObservableValue,
										Number paramT1, Number paramT2) {
									// System.out.println("Content View Height :"+paramT2.doubleValue());
								}
							});
				}
			};
		};
		this.textArea.setWrapText(true);
		this.textArea.getStyleClass().add("input-label");

		this.label = new Label();
		this.label.setWrapText(true);
		this.label.prefWidthProperty().bind(this.textArea.widthProperty());
		this.label.getStyleClass().add("input-label");
		label.textProperty().bind(new StringBinding() {
			{
				bind(textArea.textProperty());
			}

			@Override
			protected String computeValue() {
				if (textArea.getText() != null
						&& textArea.getText().length() > 0) {
					if (!((Character) textArea.getText().charAt(
							textArea.getText().length() - 1)).equals(enterChar)) {
						return textArea.getText() + enterChar;
					}
				}
				return textArea.getText();
			}
		});

		StackPane lblContainer = StackPaneBuilder.create()
				.alignment(Pos.TOP_LEFT).padding(new Insets(4, 7, 7, 7))
				.children(label).build();
		// Binding the container width/height to the TextArea width.
		lblContainer.maxWidthProperty().bind(textArea.widthProperty());

		textArea.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(
					ObservableValue<? extends String> paramObservableValue,
					String paramT1, String value) {
				layoutForNewLine(textArea.getText());
			}
		});

		label.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(
					ObservableValue<? extends Number> paramObservableValue,
					Number paramT1, Number paramT2) {
				layoutForNewLine(textArea.getText());
			}
		});

		getChildren().addAll(lblContainer, textArea);
	}

	@Override
	public void requestFocus() {
		super.requestFocus();
		textArea.requestFocus();
	}

	private void layoutForNewLine(String text) {
		if (text != null
				&& text.length() > 0
				&& ((Character) text.charAt(text.length() - 1))
						.equals(enterChar)) {
			textArea.setPrefHeight(label.getHeight() + NEW_LINE_HEIGHT
					+ TOP_PADDING + BOTTOM_PADDING);
			textArea.setMinHeight(textArea.getPrefHeight());
		} else {
			textArea.setPrefHeight(label.getHeight() + TOP_PADDING
					+ BOTTOM_PADDING);
			textArea.setMinHeight(textArea.getPrefHeight());
		}
	}

	public TextArea getTextArea() {
		return textArea;
	}

	public StringProperty promptTextProperty() {
		return textArea.promptTextProperty();
	}

	public String getPromptText() {
		return promptTextProperty().get();
	}

	public void setPromptText(String value) {
		promptTextProperty().set(value);
	}
	
	public final ObjectProperty<Font> fontProperty() {
		return textArea.fontProperty();
	}

	public final javafx.scene.text.Font getFont() {
		return this.fontProperty().get();
	}

	public final void setFont(final javafx.scene.text.Font font) {
		this.fontProperty().set(font);
	}
	

}
