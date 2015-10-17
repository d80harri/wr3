package net.d80harri.wr.ui.itemtree;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import net.d80harri.wr.ui.core.BindingUtils;

import org.springframework.stereotype.Component;

@Component
public class ItemTreeCellPresenter implements Initializable {
	
	@FXML
	private BooleanProperty detailVisible = new SimpleBooleanProperty(false);

	@FXML
	public TextField title;
	@FXML
	private TextField description;
	
	private Optional<ItemModel> model;

	private BindingUtils bu = new BindingUtils();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void setModel(Optional<ItemModel> model) {
		bu.unbindAll();
		this.model = model;
		if (model.isPresent()) {
			ItemModel item = model.get();
			bu.bindBidirectional(title.textProperty(), item.titleProperty());
			bu.bindBidirectional(description.textProperty(),
					item.descriptionProperty());
		}
	}

	public void requestFocus() {
		title.setText(title.getText() + " -----");
		title.selectAll();
		title.requestFocus();
	}

	public final BooleanProperty detailVisibleProperty() {
		return this.detailVisible;
	}

	public final boolean isDetailVisible() {
		return this.detailVisibleProperty().get();
	}

	public final void setDetailVisible(final boolean detailVisible) {
		this.detailVisibleProperty().set(detailVisible);
	}

}
