package net.d80harri.wr.ui.itemtree2;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class ItemTreeView extends ViewBase<ItemTreePresenter> implements IItemTreeView {

	@FXML
	private TreeView<TreeItemCellView> itemTree;
	private TreeItem<TreeItemCellView> rootNode;
	
	@Override
	protected void registerHandlers() {
		rootNode = new TreeItem<TreeItemCellView>();
		rootNode.setExpanded(true);
		itemTree.setRoot(rootNode);
	}

	@Override
	public TreeItemCellView createRootNode() {
		TreeItemCellView result = new TreeItemCellView();
		rootNode.getChildren().add(new TreeItem<TreeItemCellView>(result));
		itemTree.layout();
		result.requestFocus();
		return result;
	}

	public TreeView<TreeItemCellView> getItemTree() {
		return itemTree;
	}

}
