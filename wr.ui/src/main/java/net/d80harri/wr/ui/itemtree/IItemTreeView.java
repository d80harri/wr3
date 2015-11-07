package net.d80harri.wr.ui.itemtree;

import net.d80harri.wr.ui.core.IView;
import net.d80harri.wr.ui.itemtree.cell.TreeItemCellView;
import javafx.scene.control.TreeItem;

public interface IItemTreeView extends IView<ItemTreePresenter, IItemTreeView> {

	public abstract TreeItem<TreeItemCellView> createItemAt(TreeItem<TreeItemCellView> parent, int indexOfItem);

	public abstract TreeItem<TreeItemCellView> createRootNode();


}
