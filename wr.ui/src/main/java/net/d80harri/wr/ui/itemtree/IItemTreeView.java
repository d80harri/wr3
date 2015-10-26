package net.d80harri.wr.ui.itemtree;

import javafx.scene.control.TreeItem;

public interface IItemTreeView {

	public abstract TreeItem<TreeItemCellView> createItemAt(TreeItem<TreeItemCellView> parent, int indexOfItem);

	public abstract TreeItem<TreeItemCellView> createRootNode();


}
