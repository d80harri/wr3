package net.d80harri.wr.ui.itemtree;

import java.util.Optional;

import javafx.scene.control.TreeCell;

public class ItemTreeCell extends TreeCell<ItemModel>{

	private ItemTreeCellView view;
	private ItemTreeCellPresenter presenter;
	
	public ItemTreeCell() {
		view = new ItemTreeCellView();
		presenter = (ItemTreeCellPresenter) view.getPresenter();
		
		this.setGraphic(view.getView());
	}
	
	@Override
	protected void updateItem(ItemModel item, boolean empty) {
		super.updateItem(item, empty);
		if (empty || item == null) {
			setText(null);
			setGraphic(null);
		} else {
			setText(item.getTitle());
			setGraphic(view.getView());
			presenter.setModel(Optional.of(item));			
		}
	}
}
