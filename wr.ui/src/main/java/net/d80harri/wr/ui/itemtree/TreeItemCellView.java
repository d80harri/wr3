package net.d80harri.wr.ui.itemtree;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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

		private String title;

		public TreeItemCellEvent(
				EventType<? extends TreeItemCellEvent> eventType) {
			super(eventType);
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getTitle() {
			return title;
		}

	}

	@FXML
	private TextField txtTitle;
	@FXML
	private Pane detailPane;
	@FXML
	private FittingHeightTextArea descriptionArea;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txtTitle.setOnKeyPressed(this::txtTitle_KeyPressed);
		detailPane.addEventFilter(KeyEvent.KEY_PRESSED, this::detail_KeyPressed);
		detailPane.managedProperty().bind(detailVisibleProperty());
		detailPane.visibleProperty().bind(detailVisibleProperty());

		this.addEventFilter(MouseEvent.MOUSE_CLICKED,
				new EventHandler<MouseEvent>() {
					public void handle(MouseEvent evt) {
						setActivated(true);
					};
				});
	}

	@Override
	protected void layoutChildren() {
		super.layoutChildren();

		txtTitle.resize(getWidth(), getHeight());
	}

	public TextField getTxtTitle() {
		return txtTitle;
	}

	private void detail_KeyPressed(KeyEvent evt) {
		if (evt.isShiftDown()) {
			if (evt.getCode() == KeyCode.ENTER) {
				txtTitle.requestFocus();
				setDetailVisible(false);
			}
		}
	}
	
	private void txtTitle_KeyPressed(KeyEvent evt) {
		if (evt.isControlDown()) {
			if (evt.getCode() == KeyCode.SPACE) {
				this.fireEvent(new TreeItemCellEvent(
						TreeItemCellEvent.TOGGLE_EXPAND));
				evt.consume();
			} else if (evt.getCode() == KeyCode.D) {
				this.fireEvent(new TreeItemCellEvent(
						TreeItemCellEvent.DELETE));
			}
		} else if (evt.isShiftDown()) {
			if (evt.getCode() == KeyCode.TAB) {
				this.fireEvent(new TreeItemCellEvent(TreeItemCellEvent.OUTDENT));
				evt.consume();
			} else if (evt.getCode() == KeyCode.ENTER) {
				setDetailVisible(!isDetailVisible());
				descriptionArea.requestFocus();
				evt.consume();
			}
		} else if (evt.isAltDown()) {
			if (evt.getCode() == KeyCode.UP) {
				this.fireEvent(new TreeItemCellEvent(TreeItemCellEvent.MOVE_UP));
				evt.consume();
			} else if (evt.getCode() == KeyCode.DOWN) {
				this.fireEvent(new TreeItemCellEvent(
						TreeItemCellEvent.MOVE_DOWN));
				evt.consume();
			} else if (evt.getCode() == KeyCode.RIGHT) {
				this.fireEvent(new TreeItemCellEvent(TreeItemCellEvent.INDENT));
				evt.consume();
			} else if (evt.getCode() == KeyCode.LEFT) {
				this.fireEvent(new TreeItemCellEvent(TreeItemCellEvent.OUTDENT));
				evt.consume();
			}
		} else {
			if (evt.getCode() == KeyCode.ENTER) {
				if (txtTitle.getCaretPosition() == txtTitle.getText().length()) {
					this.fireEvent(new TreeItemCellEvent(
							TreeItemCellEvent.CREATE_AFTER));
					evt.consume();
				} else {
					int caretPosition = getTxtTitle().getCaretPosition();
					String newText = getTxtTitle().getText().substring(
							caretPosition);
					getTxtTitle().deleteText(caretPosition,
							getTxtTitle().getLength());
					TreeItemCellEvent cellEvent = new TreeItemCellEvent(
							TreeItemCellEvent.CREATE_AFTER);
					cellEvent.setTitle(newText);
					this.fireEvent(cellEvent);
					evt.consume();
				}
			} else if (evt.getCode() == KeyCode.UP) {
				this.fireEvent(new TreeItemCellEvent(
						TreeItemCellEvent.GOTO_PREVIOUS));
				evt.consume();
			} else if (evt.getCode() == KeyCode.DOWN) {
				this.fireEvent(new TreeItemCellEvent(
						TreeItemCellEvent.GOTO_NEXT));
				evt.consume();
			} else if (evt.getCode() == KeyCode.TAB) {
				this.fireEvent(new TreeItemCellEvent(TreeItemCellEvent.INDENT));
				evt.consume();
			} else if (evt.getCode() == KeyCode.BACK_SPACE) {
				if (txtTitle.getText().isEmpty()) {
					this.fireEvent(new TreeItemCellEvent(
							TreeItemCellEvent.DELETE));
					evt.consume();
				} else if (txtTitle.getCaretPosition() == 0) {
					this.fireEvent(new TreeItemCellEvent(
							TreeItemCellEvent.MERGEWITH_PREVIOUS));
					evt.consume();
				}
			} else if (evt.getCode() == KeyCode.DELETE) {
				if (txtTitle.getText().isEmpty()) {
					this.fireEvent(new TreeItemCellEvent(
							TreeItemCellEvent.DELETE));
					evt.consume();
				} else if (txtTitle.getCaretPosition() == txtTitle.getText()
						.length()) {
					this.fireEvent(new TreeItemCellEvent(
							TreeItemCellEvent.MERGEWITH_NEXT));
					evt.consume();
				}
			} else if (evt.getCode() == KeyCode.LEFT) {
				if (txtTitle.getCaretPosition() == 0) {
					this.fireEvent(new TreeItemCellEvent(
							TreeItemCellEvent.GOTO_PREVIOUS));
					evt.consume();
				}
			} else if (evt.getCode() == KeyCode.RIGHT) {
				if (txtTitle.getCaretPosition() == txtTitle.getText().length()) {
					this.fireEvent(new TreeItemCellEvent(
							TreeItemCellEvent.GOTO_NEXT));
					evt.consume();
				}
			}
		}
	}

	public Pane getDetailPane() {
		return detailPane;
	}

	public FittingHeightTextArea getDescriptionArea() {
		return descriptionArea;
	}

	/*
	 * =======================================================================
	 * == PROPERTIES ==
	 * =======================================================================
	 */

	private BooleanProperty detailVisible;
	
	public final BooleanProperty detailVisibleProperty() {
		if (detailVisible == null) {
			detailVisible = new SimpleBooleanProperty(false);
		}
		return this.detailVisible;
	}

	public final boolean isDetailVisible() {
		return this.detailVisibleProperty().get();
	}

	public final void setDetailVisible(final boolean detailVisible) {
		this.detailVisibleProperty().set(detailVisible);
	}

	private BooleanProperty activated;
	
	public final BooleanProperty activatedProperty() {
		if (activated == null) {
			activated = new SimpleBooleanProperty(false);
			activated.addListener((obs, o, n) -> {
				if (n) {
					txtTitle.requestFocus();
					txtTitle.positionCaret(0);
				} else {
					setDetailVisible(false);
				}
			});
		}
		return this.activated;
	}

	public final boolean isActivated() {
		return this.activatedProperty().get();
	}

	public final void setActivated(final boolean activated) {
		this.activatedProperty().set(activated);
	}

}
