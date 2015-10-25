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

	@Override
	public TreeItem<TreeItemCellView> createRootNode() {
		return createItemAt(this.rootNode, rootNode.getChildren().size());
	}

	@Override
	public TreeItem<TreeItemCellView> createItemAt(
			TreeItem<TreeItemCellView> parent, int indexOfItem) {
		TreeItemCellView resultCell = new TreeItemCellView();
		TreeItem<TreeItemCellView> resultTreeItem = new TreeItem<TreeItemCellView>(
				resultCell);
		resultCell.addEventHandler(TreeItemCellView.TreeItemCellEvent.BASE,
				this::handleTreeCellEvent);
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

	private void handleTreeCellEvent(TreeItemCellEvent event) {
		TreeItem<TreeItemCellView> item = findItem((TreeItemCellView) event
				.getSource());
		int rowOfItem = itemTree.getRow(item);

		if (event.getEventType() == TreeItemCellEvent.CREATE_AFTER) {
			createItemAt(item.getParent(), rowOfItem + 1);
		} else if (event.getEventType() == TreeItemCellEvent.CREATE_CHILD) {
			TreeItem<TreeItemCellView> createdItem = createItemAt(item, 0);
			item.expandedProperty().set(true);
			itemTree.layout();
			createdItem.getValue().requestFocus();
		} else if (event.getEventType() == TreeItemCellEvent.EXPAND) {
			if (item.getChildren().size() > 0) {
				item.expandedProperty().set(true);
				itemTree.layout();
				item.getChildren().get(0).getValue().requestFocus();
			}
		} else if (event.getEventType() == TreeItemCellEvent.MOVE_UP) {
			itemTree.getTreeItem(rowOfItem - 1).getValue().requestFocus();
		} else if (event.getEventType() == TreeItemCellEvent.MOVE_DOWN) {
			itemTree.getTreeItem(rowOfItem + 1).getValue().requestFocus();
		} else if (event.getEventType() == TreeItemCellEvent.MOVE_TO_PARENT) {
			item.getParent().getValue().requestFocus();
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
