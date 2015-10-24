package net.d80harri.wr.ui.itemtree2;

import javafx.scene.control.TreeItem;

public interface IItemTreeView {

	public abstract TreeItem<TreeItemCellView> createItemAt(TreeItem<TreeItemCellView> parent, int indexOfItem);

	public abstract TreeItem<TreeItemCellView> createRootNode();


}
