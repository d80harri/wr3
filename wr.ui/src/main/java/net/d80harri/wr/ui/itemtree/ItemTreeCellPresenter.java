package net.d80harri.wr.ui.itemtree;

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import org.springframework.stereotype.Component;

@Component
public class ItemTreeCellPresenter {
	@FXML
	private Label title;
	@FXML
	private Label description;

	private Optional<ItemModel> model;

	public void setModel(Optional<ItemModel> model) {
		this.model = model;
		if (model.isPresent()) {
			ItemModel item = model.get();
			title.setText(item.getTitle());
			description.setText(item.getDescription());
		}
	}

}
