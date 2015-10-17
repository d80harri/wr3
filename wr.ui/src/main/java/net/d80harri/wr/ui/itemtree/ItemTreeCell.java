package net.d80harri.wr.ui.itemtree;

import java.util.Optional;

import net.d80harri.wr.ui.itemtree.ItemTreeCellPresenter.ItemTreeCellEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ItemTreeCell extends TreeCell<ItemModel> {

	private ItemTreeCellView view = null;

	@Override
	protected synchronized void updateItem(ItemModel item, boolean empty) {
		System.out.println(empty + " " + this.hashCode()
				+ " updateItem called " + item);
		super.updateItem(item, empty);
		if (empty || item == null) {
			setText(null);
			setGraphic(null);
		} else {
			setText(null);
			createView();

			setModel(item);
			setGraphic(view.getView());
		}
	}

	public void createView() {
		this.view = new ItemTreeCellView();
	}

	private void setModel(ItemModel model) {
		((ItemTreeCellPresenter)view.getPresenter()).setModel(Optional.of(model));
	}

	// @Override
	// public void startEdit() {
	// super.startEdit();
	// if (this.view == null) {
	// createView();
	// }
	// setText(null);
	// setGraphic(view.getView());
	// getPresenter().requestFocus();
	// }
}
