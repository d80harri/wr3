package net.d80harri.wr.ui.itemtree;

import java.util.Optional;

import javafx.scene.control.TreeCell;

public class ItemTreeCell extends TreeCell<ItemModel> {

	@Override
	protected void updateItem(ItemModel item, boolean empty) {
		super.updateItem(item, empty);
		if (empty || item == null) {
			setText(null);
			setGraphic(null);
		} else {
			ItemTreeCellView view = new ItemTreeCellView();
			ItemTreeCellPresenter presenter = (ItemTreeCellPresenter) view
					.getPresenter();
			setText(null);
			setGraphic(view.getView());
			presenter.setModel(Optional.of(item));
		}
	}
	
	@Override
	public void startEdit() {
		super.startEdit();		
	}
}
