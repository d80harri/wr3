package net.d80harri.wr.ui.itemtree;

import java.util.Optional;

import javafx.scene.control.TreeCell;

public class ItemTreeCell extends TreeCell<ItemModel> {

	private ItemTreeCellView view = null;
	private ItemTreeCellPresenter presenter = null;

	@Override
	protected void updateItem(ItemModel item, boolean empty) {
		super.updateItem(item, empty);
		if (empty || item == null) {
			setText(null);
			setGraphic(null);
		} else {
			setText(null);
			createView();
			setGraphic(view.getView());
			getPresenter().setModel(Optional.of(item));
		}
	}

	public void createView() {
		this.view = new ItemTreeCellView();
	}

	public ItemTreeCellPresenter getPresenter() {
		return (ItemTreeCellPresenter)this.view.getPresenter();
	}

	@Override
	public void startEdit() {
		super.startEdit();
		getPresenter().requestFocus();
	}
}
