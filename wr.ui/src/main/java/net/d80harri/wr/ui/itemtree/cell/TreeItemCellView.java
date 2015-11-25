package net.d80harri.wr.ui.itemtree.cell;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import net.d80harri.wr.ui.components.FittingHeightTextArea;
import net.d80harri.wr.ui.core.ViewBase;

import org.fxmisc.easybind.EasyBind;

public class TreeItemCellView extends ViewBase<TreeItemCellPresenter> implements
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
		detailPane
				.addEventFilter(KeyEvent.KEY_PRESSED, this::detail_KeyPressed);
		detailPane.managedProperty().bind(detailVisibleProperty());
		detailPane.visibleProperty().bind(detailVisibleProperty());

		this.addEventFilter(MouseEvent.MOUSE_CLICKED,
				new EventHandler<MouseEvent>() {
					public void handle(MouseEvent evt) {
						Optional.ofNullable(getPresenter()).ifPresent(
								i -> i.setActivated(true));
					};
				});

		EasyBind.subscribe(presenterProperty(), this::presenterChanged);

		txtTitle.textProperty().addListener(
				(obs, o, n) -> Optional.of(this.getPresenter()).ifPresent(
						i -> i.setTitle(n)));
	}

	private void presenterChanged(TreeItemCellPresenter presenter) {
		if (presenter != null) {
			EasyBind.subscribe(presenter.titleProperty(),
					n -> txtTitle.setText(n));
			presenter.activatedProperty().addListener((obs, o, n) ->{
				if (n) {
					txtTitle.requestFocus();
					txtTitle.positionCaret(0);
				} else {
					setDetailVisible(false);
					getPresenter().saveOrUpdate();
				}
			});
			presenter.titleCaretPositionProperty().bind(txtTitle.caretPositionProperty());
		}
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
				getPresenter().toggleExpand();
				evt.consume();
			} else if (evt.getCode() == KeyCode.D) {
				getPresenter().delete();
			}
		} else if (evt.isShiftDown()) {
			if (evt.getCode() == KeyCode.TAB) {
				getPresenter().outdent();
				evt.consume();
			} else if (evt.getCode() == KeyCode.ENTER) {
				setDetailVisible(!isDetailVisible());
				descriptionArea.requestFocus();
				evt.consume();
			}
		} else if (evt.isAltDown()) {
			if (evt.getCode() == KeyCode.UP) {
				getPresenter().switchWithPrev();
				evt.consume();
			} else if (evt.getCode() == KeyCode.DOWN) {
				getPresenter().switchWithNext();
				evt.consume();
			} else if (evt.getCode() == KeyCode.RIGHT) {
				getPresenter().indent();
				evt.consume();
			} else if (evt.getCode() == KeyCode.LEFT) {
				getPresenter().outdent();
				evt.consume();
			}
		} else {
			if (evt.getCode() == KeyCode.ENTER) {
				getPresenter().splitItem();
				evt.consume();
			} else if (evt.getCode() == KeyCode.UP) {
				getPresenter().gotoPreviousSibling();
				evt.consume();
			} else if (evt.getCode() == KeyCode.DOWN) {
				getPresenter().gotoNextSibling();
				evt.consume();
			} else if (evt.getCode() == KeyCode.TAB) {
				getPresenter().indent();
				evt.consume();
			} else if (evt.getCode() == KeyCode.BACK_SPACE) {
				if (txtTitle.getText().isEmpty()) {
					getPresenter().delete();
					evt.consume();
				} else if (txtTitle.getCaretPosition() == 0) {
					getPresenter().mergePreviousInto();
					evt.consume();
				}
			} else if (evt.getCode() == KeyCode.DELETE) {
				if (txtTitle.getText().isEmpty()) {
					getPresenter().delete();
					evt.consume();
				} else if (txtTitle.getCaretPosition() == txtTitle.getText()
						.length()) {
					getPresenter().mergeNextInto();
					evt.consume();
				}
			} else if (evt.getCode() == KeyCode.LEFT) {
				if (txtTitle.getCaretPosition() == 0) {
					getPresenter().gotoPreviousSibling();
					evt.consume();
				}
			} else if (evt.getCode() == KeyCode.RIGHT) {
				if (txtTitle.getCaretPosition() == txtTitle.getText().length()) {
					getPresenter().gotoNextSibling();
					evt.consume();
				}
			}
		}
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

}
