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
		
		public static final EventType<TreeItemCellEvent> INDENT = new EventType<TreeItemCellView.TreeItemCellEvent>(
				BASE, "MOVETO_CHILD_OF_PREVIOUS");

		public static final EventType<TreeItemCellEvent> DELETE = new EventType<TreeItemCellView.TreeItemCellEvent>(
				BASE, "DELETE");

		public static final EventType<TreeItemCellEvent> MERGEWITH_PREVIOUS = new EventType<TreeItemCellView.TreeItemCellEvent>(
				BASE, "MERGEWITH_PREVIOUS");

		public static final EventType<TreeItemCellEvent> MERGEWITH_NEXT = new EventType<TreeItemCellView.TreeItemCellEvent>(
				BASE, "MERGEWITH_NEXT");

		public static final EventType<TreeItemCellEvent> SPLIT = new EventType<TreeItemCellView.TreeItemCellEvent>(
				BASE, "SPLIT");

		public static final EventType<TreeItemCellEvent> OUTDENT = new EventType<TreeItemCellView.TreeItemCellEvent>(
				BASE, "MOVETO_PARENT");

		public static final EventType<TreeItemCellEvent> MOVE_DOWN = new EventType<TreeItemCellView.TreeItemCellEvent>(
				BASE, "MOVETO_NEXT");

		public static final EventType<TreeItemCellEvent> MOVE_UP = new EventType<TreeItemCellView.TreeItemCellEvent>(
				BASE, "MOVETO_PREVIOUS");

		public static final EventType<TreeItemCellEvent> CREATE_AFTER = new EventType<TreeItemCellView.TreeItemCellEvent>(
				BASE, "CREATE_AFTER");

		public static final EventType<TreeItemCellEvent> GOTO_PREVIOUS = new EventType<TreeItemCellView.TreeItemCellEvent>(
				BASE, "GOTO_PREVIOUS");

		public static final EventType<TreeItemCellEvent> GOTO_NEXT = new EventType<TreeItemCellView.TreeItemCellEvent>(
				BASE, "GOTO_NEXT");

		public static final EventType<TreeItemCellEvent> TOGGLE_EXPAND = new EventType<TreeItemCellView.TreeItemCellEvent>(
				BASE, "TOGGLE_EXPAND");

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
		if (evt.isControlDown()) {
			if (evt.getCode() == KeyCode.SPACE) {
				this.fireEvent(new TreeItemCellEvent(
						TreeItemCellEvent.TOGGLE_EXPAND));
			}
		} else if (evt.isShiftDown()) {
			if (evt.getCode() == KeyCode.TAB) {
				this.fireEvent(new TreeItemCellEvent(
						TreeItemCellEvent.OUTDENT));
			} else if (evt.getCode() == KeyCode.ENTER) {
				detailVisible.set(!detailVisible.get());
				descriptionArea.requestFocus();
			}
		} else if (evt.isAltDown()) {
			if (evt.getCode() == KeyCode.UP) {
				this.fireEvent(new TreeItemCellEvent(
						TreeItemCellEvent.MOVE_UP));
			} else if (evt.getCode() == KeyCode.DOWN) {
				this.fireEvent(new TreeItemCellEvent(
						TreeItemCellEvent.MOVE_DOWN));
			} else if (evt.getCode() == KeyCode.RIGHT) {
				this.fireEvent(new TreeItemCellEvent(
						TreeItemCellEvent.INDENT));
			} else if (evt.getCode() == KeyCode.LEFT) {
				this.fireEvent(new TreeItemCellEvent(
						TreeItemCellEvent.OUTDENT));
			}
		} else {
			if (evt.getCode() == KeyCode.ENTER) {
				if (txtTitle.getCaretPosition() == txtTitle.getText().length()) {
					this.fireEvent(new TreeItemCellEvent(
							TreeItemCellEvent.CREATE_AFTER));
				} else {
					this.fireEvent(new TreeItemCellEvent(
							TreeItemCellEvent.SPLIT));
				}
			} else if (evt.getCode() == KeyCode.UP) {
				this.fireEvent(new TreeItemCellEvent(
						TreeItemCellEvent.GOTO_PREVIOUS));
			} else if (evt.getCode() == KeyCode.DOWN) {
				this.fireEvent(new TreeItemCellEvent(
						TreeItemCellEvent.GOTO_NEXT));
			} else if (evt.getCode() == KeyCode.TAB) {
				this.fireEvent(new TreeItemCellEvent(
						TreeItemCellEvent.INDENT));
			} else if (evt.getCode() == KeyCode.BACK_SPACE) {
				if (txtTitle.getText().isEmpty()) {
					this.fireEvent(new TreeItemCellEvent(
							TreeItemCellEvent.DELETE));
				} else if (txtTitle.getCaretPosition() == 0) {
					this.fireEvent(new TreeItemCellEvent(
							TreeItemCellEvent.MERGEWITH_PREVIOUS));
				}
			} else if (evt.getCode() == KeyCode.DELETE) {
				if (txtTitle.getText().isEmpty()) {
					this.fireEvent(new TreeItemCellEvent(
							TreeItemCellEvent.DELETE));
				} else if (txtTitle.getCaretPosition() == txtTitle.getText()
						.length()) {
					this.fireEvent(new TreeItemCellEvent(
							TreeItemCellEvent.MERGEWITH_NEXT));
				}
			} else if (evt.getCode() == KeyCode.LEFT) {
				if (txtTitle.getCaretPosition() == 0) {
					this.fireEvent(new TreeItemCellEvent(
							TreeItemCellEvent.GOTO_PREVIOUS));
				}
			} else if (evt.getCode() == KeyCode.RIGHT) {
				if (txtTitle.getCaretPosition() == txtTitle.getText().length()) {
					this.fireEvent(new TreeItemCellEvent(
							TreeItemCellEvent.GOTO_NEXT));
				}
			}
		}
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
