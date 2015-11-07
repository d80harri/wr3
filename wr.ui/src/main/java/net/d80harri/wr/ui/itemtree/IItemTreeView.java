package net.d80harri.wr.ui.itemtree;

import javafx.scene.control.TreeItem;
import net.d80harri.wr.ui.core.IView;
import net.d80harri.wr.ui.itemtree.cell.ITreeItemCellView;

public interface IItemTreeView extends IView<ItemTreePresenter, IItemTreeView> {

	public abstract TreeItem<ITreeItemCellView> createItemAt(TreeItem<ITreeItemCellView> parent, int indexOfItem);

	public abstract TreeItem<ITreeItemCellView> createRootNode();


}
