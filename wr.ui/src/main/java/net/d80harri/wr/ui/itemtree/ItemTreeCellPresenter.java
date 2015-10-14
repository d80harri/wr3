package net.d80harri.wr.ui.itemtree;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import net.d80harri.wr.ui.core.BindingUtils;

import org.springframework.stereotype.Component;

@Component
public class ItemTreeCellPresenter implements Initializable {
	public static class ItemTreeCellEvent extends Event {
		public static final EventType<ItemTreeCellEvent> APPEND_SIBLING_REQUESTED = new EventType<>("APPEND_SIBLING_REQUESTED");
		
		private ItemModel requestSource;
		
		public ItemTreeCellEvent(EventType<? extends Event> eventType, ItemModel requestSource) {
			super(eventType);
			this.requestSource = requestSource;
		}

		public ItemModel getRequestSource() {
			return requestSource;
		}
		
	}

	@FXML private TextField title;
	@FXML private TextField description;

	private Optional<ItemModel> model;
	
	private BindingUtils bu = new BindingUtils();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		title.setOnKeyPressed(this::appendSibling);
	}
	
	public void setModel(Optional<ItemModel> model) {
		bu.unbindAll();
		this.model = model;
		if (model.isPresent()) {
			ItemModel item = model.get();
			bu.bindBidirectional(title.textProperty(), item.titleProperty());
			bu.bindBidirectional(description.textProperty(), item.descriptionProperty());
		}
	}
	
	public void appendSibling(KeyEvent evt) {
		title.fireEvent(new ItemTreeCellEvent(ItemTreeCellEvent.APPEND_SIBLING_REQUESTED, model.get()));
	}

}
