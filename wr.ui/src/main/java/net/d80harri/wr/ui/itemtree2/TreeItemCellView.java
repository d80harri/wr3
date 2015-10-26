package net.d80harri.wr.ui.itemtree2;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class TreeItemCellView extends ViewBase<TreeItemCellPresenter> {

	public static class TreeItemCellEvent extends Event {
		private static final long serialVersionUID = 3699285621625278032L;

		public static final EventType<TreeItemCellEvent> BASE = new EventType<TreeItemCellView.TreeItemCellEvent>(
				"BASE");
		public static final EventType<TreeItemCellEvent> CREATE_AFTER = new EventType<TreeItemCellView.TreeItemCellEvent>(
				BASE, "NEXT");
		public static final EventType<TreeItemCellEvent> CREATE_CHILD = new EventType<TreeItemCellView.TreeItemCellEvent>(
				BASE, "CHILD");

		public static final EventType<TreeItemCellEvent> MOVE_UP = new EventType<TreeItemCellView.TreeItemCellEvent>(
				BASE, "MOVE_UP");

		public static final EventType<TreeItemCellEvent> MOVE_DOWN = new EventType<TreeItemCellView.TreeItemCellEvent>(
				BASE, "MOVE_DOWN");

		public static final EventType<TreeItemCellEvent> EXPAND = new EventType<TreeItemCellView.TreeItemCellEvent>(
				BASE, "EXPAND");

		public static final EventType<TreeItemCellEvent> MOVE_TO_PARENT = new EventType<TreeItemCellView.TreeItemCellEvent>(
				BASE, "MOVE_TO_PARENT");

		public TreeItemCellEvent(EventType<? extends TreeItemCellEvent> eventType) {
			super(eventType);
		}

	}

	@FXML
	private TextField txtTitle;
	@FXML
	private Pane detailPane;

	@Override
	protected void registerHandlers() {
		txtTitle.setOnKeyPressed(this::txtTitle_KeyPressed);
	}

	@Override
	protected void layoutChildren() {
		super.layoutChildren();
		txtTitle.resize(getWidth(), getHeight());
	}
	
	public TextField getTxtTitle() {
		return txtTitle;
	}

	private void txtTitle_KeyPressed(KeyEvent evt) {
		if (evt.getCode() == KeyCode.ENTER) {
			if (evt.isShiftDown()) {
				this.detailPane.setVisible(!this.detailPane.isVisible());
				this.detailPane.setManaged(!this.detailPane.isManaged());
			} else if (evt.isControlDown()) {
				this.fireEvent(new TreeItemCellEvent(TreeItemCellEvent.CREATE_CHILD));
			} else {
				this.fireEvent(new TreeItemCellEvent(TreeItemCellEvent.CREATE_AFTER));
			}
		} else if (evt.getCode() == KeyCode.UP) {
			this.fireEvent(new TreeItemCellEvent(TreeItemCellEvent.MOVE_TO_PARENT));
		} else if (evt.getCode() == KeyCode.DOWN) {
			this.fireEvent(new TreeItemCellEvent(TreeItemCellEvent.EXPAND));
		} else if (evt.getCode() == KeyCode.RIGHT && caretIsAtLastPositionInTitle()) {
			this.fireEvent(new TreeItemCellEvent(TreeItemCellEvent.MOVE_DOWN));
		} else if (evt.getCode() == KeyCode.LEFT && caretIsAtFirstPositionInTitle()) {
			this.fireEvent(new TreeItemCellEvent(TreeItemCellEvent.MOVE_UP));
		}
	}
	
	private boolean caretIsAtLastPositionInTitle() {
		return txtTitle.getCaretPosition() == txtTitle.getText().length();
	}
	
	private boolean caretIsAtFirstPositionInTitle() {
		return txtTitle.getCaretPosition() == 0;
	}

	@Override
	public void requestFocus() {
		super.requestFocus();
		txtTitle.requestFocus();
		txtTitle.selectAll();
	}

	public Pane getDetailPane() {
		return detailPane;
	}
}
