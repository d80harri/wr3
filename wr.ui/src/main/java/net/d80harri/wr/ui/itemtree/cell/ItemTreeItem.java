package net.d80harri.wr.ui.itemtree.cell;

import org.fxmisc.easybind.EasyBind;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

public class ItemTreeItem extends TreeItem<ItemCellView>{
	private ItemCellPresenter presenter;
	private ObservableList<TreeItem<ItemCellView>> children;

	public ItemTreeItem(ItemCellPresenter presenter) {
		this.presenter = presenter;
		setValue(new ItemCellView(presenter));
		this.expandedProperty().bindBidirectional(
				presenter.expandedProperty());
	}
	
	@Override
	public boolean isLeaf() {
		return getChildren().size() == 0;
	}

	@Override
	public ObservableList<TreeItem<ItemCellView>> getChildren() {
		if (children == null) {
			children = EasyBind.map(presenter.getChildren(),
					i -> new ItemTreeItem(i));
			presenter.loadChildren();
		}
		return this.children;
	}
}
