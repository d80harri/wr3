package net.d80harri.wr.ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.FontAwesome.Glyph;

public class FontAwesomeTable extends Application {
	TextField searchTerm = new TextField();
	Button searchButton = new Button();
	FlowPane grid = new FlowPane();
	HBox searchPane = new HBox(searchTerm, searchButton);

	@Override
	public void start(Stage stage) throws Exception {
		searchTerm.prefWidth(100);
		searchButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				doFilter();
			}
		});
		doFilter();

		Scene scene = new Scene(new BorderPane(new ScrollPane(grid),
				searchPane, null, null, null));
		stage.setTitle("Font-Awesome");
		stage.setScene(scene);
		stage.setWidth(768);
		stage.setHeight(1024);
		stage.show();
	}

	private void doFilter() {
		grid.getChildren().clear();
		for (Glyph g : FontAwesome.Glyph.values()) {
			if (g.name().toLowerCase()
					.contains(searchTerm.getText().toLowerCase())) {
				Label l = new Label("", new org.controlsfx.glyphfont.Glyph(
						"FontAwesome", g));
				l.setTooltip(new Tooltip(g.name()));
				l.setPadding(new Insets(5, 5, 5, 5));
				l.setFont(new Font(15));
				grid.getChildren().add(l);
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
