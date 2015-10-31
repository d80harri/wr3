package net.d80harri.wr.ui.itemtree;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import net.d80harri.wr.ui.itemtree.TreeItemCellView.TreeItemCellEvent;

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

	// TODO: this method should not be public
	// instead use a method like addItemAfterSelected(new TreeItemCellView());
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
		resultCell.visit();
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
		item.getValue().go();
		int rowOfItem = itemTree.getRow(item);

		if (event.getEventType() == TreeItemCellEvent.CREATE_AFTER) {
			createItemAt(item.getParent(), item.getParent().getChildren()
					.indexOf(item) + 1);
		} else if (event.getEventType() == TreeItemCellEvent.TOGGLE_EXPAND) {
			if (item.getChildren().size() > 0) {
				item.expandedProperty().set(!item.expandedProperty().get());
				itemTree.layout();
				item.getChildren().get(0).getValue().visit();
			}
		} else if (event.getEventType() == TreeItemCellEvent.GOTO_PREVIOUS) {
			itemTree.getTreeItem(rowOfItem - 1).getValue().visit();
		} else if (event.getEventType() == TreeItemCellEvent.GOTO_NEXT) {
			itemTree.getTreeItem(rowOfItem + 1).getValue().visit();
		} else if (event.getEventType() == TreeItemCellEvent.DELETE) {
			item.getParent().getChildren().remove(item);
		} else if (event.getEventType() == TreeItemCellEvent.INDENT) {
			int localIdx = item.getParent().getChildren().indexOf(item);
			TreeItem<TreeItemCellView> previous = item.getParent().getChildren().get(localIdx-1);
			item.getParent().getChildren().remove(item);
			previous.getChildren().add(item);
			previous.setExpanded(true);
		} else if (event.getEventType() == TreeItemCellEvent.MERGEWITH_NEXT) {
			int localIdx = item.getParent().getChildren().indexOf(item);
			if (localIdx+1 != item.getParent().getChildren().size()) {
				TreeItem<TreeItemCellView> next = item.getParent().getChildren().get(localIdx+1);
				item.getParent().getChildren().remove(next);
				item.getValue().getTxtTitle().appendText(next.getValue().getTxtTitle().getText());
			}
		} else if (event.getEventType() == TreeItemCellEvent.MERGEWITH_PREVIOUS) {
			int localIdx = item.getParent().getChildren().indexOf(item);
			if (localIdx != 0) {
				TreeItem<TreeItemCellView> prev = item.getParent().getChildren().get(localIdx-1);
				item.getParent().getChildren().remove(item);
				prev.getValue().getTxtTitle().appendText(item.getValue().getTxtTitle().getText());
			}
		} else if (event.getEventType() == TreeItemCellEvent.MOVE_DOWN) {

		} else if (event.getEventType() == TreeItemCellEvent.MOVE_UP) {

		} else if (event.getEventType() == TreeItemCellEvent.OUTDENT) {
			TreeItem<TreeItemCellView> parent = item.getParent();
			int localIdxOfParent = parent.getParent().getChildren().indexOf(parent);
			parent.getParent().getChildren().add(localIdxOfParent +1, item);
			parent.getChildren().remove(item);
		} else if (event.getEventType() == TreeItemCellEvent.SPLIT) {

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
