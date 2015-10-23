package net.d80harri.wr.ui.itemtree2;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import net.d80harri.wr.ui.itemtree2.TreeItemCellView.TreeItemCellEvent;

public class ItemTreeView extends ViewBase<ItemTreePresenter> implements
		IItemTreeView {

	@FXML
	private TreeView<TreeItemCellView> itemTree;
	private TreeItem<TreeItemCellView> rootNode;

	@Override
	protected void registerHandlers() {
		rootNode = new TreeItem<TreeItemCellView>();
		rootNode.setExpanded(true);
		itemTree.setRoot(rootNode);
	}

	public TreeItem<TreeItemCellView> createRootNode() {
		return createItemNextTo(this.rootNode, rootNode.getChildren().size());
	}

	public TreeItem<TreeItemCellView> createItemNextTo(
			TreeItem<TreeItemCellView> parent, int indexOfItem) {
		TreeItemCellView resultCell = new TreeItemCellView();
		TreeItem<TreeItemCellView> resultTreeItem = new TreeItem<TreeItemCellView>(
				resultCell);
		resultCell.addEventHandler(TreeItemCellView.TreeItemCellEvent.BASE,
				this::addNode);
		parent.getChildren().add(indexOfItem, resultTreeItem);
		itemTree.layout();
		resultCell.requestFocus();
		return resultTreeItem;
	}

	public TreeView<TreeItemCellView> getItemTree() {
		return itemTree;
	}

	public TreeItem<TreeItemCellView> getRootNode() {
		return rootNode;
	}

	private void addNode(TreeItemCellEvent event) {
		TreeItem<TreeItemCellView> item = findItem((TreeItemCellView) event
				.getSource());
		int indexOfItem = item.getParent().getChildren().indexOf(item);

		if (event.getEventType() == TreeItemCellEvent.CREATE_AFTER) {
			createItemNextTo(item.getParent(), indexOfItem);
		} else { //if (event.getEventType() == NewItemRequestedEvent.CHILD)
			createItemNextTo(item, 0);
		}
	}

	private TreeItem<TreeItemCellView> findItem(TreeItemCellView source) {
		return findItem(this.rootNode, source);
	}

	private TreeItem<TreeItemCellView> findItem(TreeItem<TreeItemCellView> it,
			TreeItemCellView item) {
		if (it.getValue() == item) {
			return it;
		} else {
			for (TreeItem<TreeItemCellView> child : it.getChildren()) {
				TreeItem<TreeItemCellView> result = findItem(child, item);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}

}
