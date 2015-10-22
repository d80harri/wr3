package net.d80harri.wr.ui.itemtree2;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import net.d80harri.wr.ui.itemtree2.TreeItemCellView.NewItemRequestedEvent;

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
	public TreeItemCellView createRootNode() {
		return createItemNextTo(this.rootNode, rootNode.getChildren().size());
	}

	public TreeItemCellView createItemNextTo(TreeItem<TreeItemCellView> parent,
			int indexOfItem) {
		TreeItemCellView result = new TreeItemCellView();
		result.addEventHandler(TreeItemCellView.NewItemRequestedEvent.BASE,
				this::addNode);
		parent.getChildren().add(indexOfItem,
				new TreeItem<TreeItemCellView>(result));
		itemTree.layout();
		result.requestFocus();
		return result;
	}

	public TreeView<TreeItemCellView> getItemTree() {
		return itemTree;
	}

	public TreeItem<TreeItemCellView> getRootNode() {
		return rootNode;
	}

	public void addNode(NewItemRequestedEvent event) {
		TreeItem<TreeItemCellView> item = findItem((TreeItemCellView) event
				.getSource());
		int indexOfItem = item.getParent().getChildren().indexOf(item);

		if (event.getEventType() == NewItemRequestedEvent.NEXT) {
			createItemNextTo(item.getParent(), indexOfItem);
		}
	}

	private TreeItem<TreeItemCellView> findItem(TreeItemCellView source) {
		return findItem(this.rootNode, source);
	}

	private TreeItem<TreeItemCellView> findItem(TreeItem<TreeItemCellView> it,
			TreeItemCellView item) {
		if (it.getValue() == item){
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
