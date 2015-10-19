package net.d80harri.wr.ui.itemtree;

import java.util.Optional;

import javafx.scene.control.TreeCell;

public class ItemTreeCell extends TreeCell<ItemModel> {

	private ItemTreeCellView view = null;

	@Override
	protected void updateItem(ItemModel item, boolean empty) {
		System.out.println(empty + " " + this.hashCode()
				+ " updateItem called " + item);
		super.updateItem(item, empty);
		if (empty || item == null) {
			setText(null);
			setGraphic(null);
		} else {
			setText(null);
			if (view == null)
				createView();

			setModel(item);
			setGraphic(view.getView());
		}
	}

	public void createView() {
		this.view = new ItemTreeCellView();
		this.view.getPresenter().setCell(this);
	}

	private void setModel(ItemModel model) {
		((ItemTreeCellPresenter)view.getPresenter()).setModel(Optional.of(model));
	}

	@Override
	public void startEdit() {
		super.startEdit();
		view.getPresenter().requestFocus();
	}
}
