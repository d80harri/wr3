package net.d80harri.wr.ui.itemtree.cell;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
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
import net.d80harri.wr.ui.core.ViewBase;

public class TreeItemCellView extends ViewBase<TreeItemCellPresenter, TreeItemCellView> implements
		Initializable {

	@FXML
	private TextField txtTitle;
	@FXML
	private Pane detailPane;
	@FXML
	private FittingHeightTextArea descriptionArea;

	public TreeItemCellView(TreeItemCellPresenter presenter) {
		super(presenter);
	}
	
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
					getPresenter().saveOrUpdate();
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

	public StringProperty titleProperty() {
		return txtTitle.textProperty();
	}

	public String getTitle() {
		return titleProperty().get();
	}
	
	public void setTitle(String title) {
		titleProperty().set(title);
	}
	
	public void appendToTitle(String title) {
		txtTitle.appendText(title);
	}

}
