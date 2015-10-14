package net.d80harri.wr.ui.itemtree;

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import net.d80harri.wr.ui.core.BindingUtils;

import org.springframework.stereotype.Component;

@Component
public class ItemTreeCellPresenter {
	@FXML
	private TextField title;
	@FXML
	private Label description;

	private Optional<ItemModel> model;
	
	private BindingUtils bu = new BindingUtils();

	public void setModel(Optional<ItemModel> model) {
		bu.unbindAll();
		this.model = model;
		if (model.isPresent()) {
			ItemModel item = model.get();
			bu.bindBidirectional(title.textProperty(), item.titleProperty());
			bu.bindBidirectional(description.textProperty(), item.descriptionProperty());
		}
	}

}
