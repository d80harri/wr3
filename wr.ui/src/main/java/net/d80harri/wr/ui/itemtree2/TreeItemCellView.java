package net.d80harri.wr.ui.itemtree2;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class TreeItemCellView extends ViewBase<TreeItemCellPresenter> {
	@FXML
	private TextField txtTitle;

	@Override
	protected void registerHandlers() {
		txtTitle.setOnKeyPressed(this::txtTitle_KeyPressed);
	}

	public TextField getTxtTitle() {
		return txtTitle;
	}

	private void txtTitle_KeyPressed(KeyEvent evt) {
		if (evt.getCode() == KeyCode.ENTER) {
			this.fireEvent(new NewItemRequestedEvent(NewItemRequestedEvent.NEXT));
		}
	}

	@Override
	public void requestFocus() {
		super.requestFocus();
		txtTitle.selectAll();
		txtTitle.requestFocus();
	}

	public static class NewItemRequestedEvent extends Event {
		private static final long serialVersionUID = 3699285621625278032L;

		public static final EventType<NewItemRequestedEvent> BASE = new EventType<TreeItemCellView.NewItemRequestedEvent>(
				"BASE");
		public static final EventType<NewItemRequestedEvent> PARENT = new EventType<TreeItemCellView.NewItemRequestedEvent>(BASE, 
				"PARENT");
		public static final EventType<NewItemRequestedEvent> NEXT = new EventType<TreeItemCellView.NewItemRequestedEvent>(BASE, 
				"NEXT");
		public static final EventType<NewItemRequestedEvent> PREV = new EventType<TreeItemCellView.NewItemRequestedEvent>(BASE, 
				"PREV");
		public static final EventType<NewItemRequestedEvent> CHILD = new EventType<TreeItemCellView.NewItemRequestedEvent>(BASE, 
				"CHILD");

		public NewItemRequestedEvent(EventType<? extends Event> eventType) {
			super(eventType);
		}

	}

}
