package net.d80harri.wr.ui.itemtree;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import net.d80harri.wr.ui.components.FittingHeightTextArea;

public class TreeItemCellView extends ViewBase<TreeItemCellPresenter> implements
		Initializable {

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

		public static final EventType<TreeItemCellEvent> COLLAPSE = new EventType<TreeItemCellView.TreeItemCellEvent>(
				BASE, "COLLAPSE");;

		public static final EventType<TreeItemCellEvent> MOVE_TO_PARENT = new EventType<TreeItemCellView.TreeItemCellEvent>(
				BASE, "MOVE_TO_PARENT");

		public TreeItemCellEvent(
				EventType<? extends TreeItemCellEvent> eventType) {
			super(eventType);
		}

	}

	@FXML
	private TextField txtTitle;
	@FXML
	private Pane detailPane;
	@FXML
	private FittingHeightTextArea descriptionArea;

	private BooleanProperty detailVisible;

	@Override
	protected void registerHandlers() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		detailVisible = new SimpleBooleanProperty(false);
		txtTitle.setOnKeyPressed(this::txtTitle_KeyPressed);
		detailPane.managedProperty().bind(detailVisible);
		detailPane.visibleProperty().bind(detailVisible);
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
				detailVisible.set(!detailVisible.get());
				descriptionArea.requestFocus();
			} else if (evt.isControlDown()) {
				this.fireEvent(new TreeItemCellEvent(
						TreeItemCellEvent.CREATE_CHILD));
			} else {
				this.fireEvent(new TreeItemCellEvent(
						TreeItemCellEvent.CREATE_AFTER));
			}
		} else if (evt.getCode() == KeyCode.UP) {
			if (evt.isControlDown()) {
				this.fireEvent(new TreeItemCellEvent(
						TreeItemCellEvent.MOVE_TO_PARENT));
			} else {
				this.fireEvent(new TreeItemCellEvent(TreeItemCellEvent.MOVE_UP));
			}
		} else if (evt.getCode() == KeyCode.DOWN) {
			this.fireEvent(new TreeItemCellEvent(TreeItemCellEvent.MOVE_DOWN));
		} else if (evt.getCode() == KeyCode.RIGHT) {
			if (evt.isControlDown()) {
				this.fireEvent(new TreeItemCellEvent(TreeItemCellEvent.EXPAND));
			} else {
				if (caretIsAtLastPositionInTitle()) {
					this.fireEvent(new TreeItemCellEvent(
							TreeItemCellEvent.MOVE_DOWN));
				}
			}
		} else if (evt.getCode() == KeyCode.LEFT) {
			if (evt.isControlDown()) {
				this.fireEvent(new TreeItemCellEvent(TreeItemCellEvent.COLLAPSE));
			} else {
				if (caretIsAtFirstPositionInTitle()) {
					this.fireEvent(new TreeItemCellEvent(
							TreeItemCellEvent.MOVE_UP));
				}
			}
		}
	}

	private boolean caretIsAtLastPositionInTitle() {
		return txtTitle.getCaretPosition() == txtTitle.getText().length();
	}

	private boolean caretIsAtFirstPositionInTitle() {
		return txtTitle.getCaretPosition() == 0;
	}

	public void visit() {
		txtTitle.requestFocus();
		txtTitle.positionCaret(0);
	}

	public void go() {
		this.detailVisible.set(false);
	}

	public Pane getDetailPane() {
		return detailPane;
	}
	
	public FittingHeightTextArea getDescriptionArea() {
		return descriptionArea;
	}

}
