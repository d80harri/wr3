package net.d80harri.wr.ui.itemtree;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import net.d80harri.wr.ui.core.ViewBase;
import net.d80harri.wr.ui.itemtree.cell.ITreeItemCellView;
import net.d80harri.wr.ui.itemtree.cell.ITreeItemCellView.TreeItemCellEvent;
import net.d80harri.wr.ui.itemtree.cell.TreeItemCellView;

public class ItemTreeView extends ViewBase<ItemTreePresenter, IItemTreeView> implements
		IItemTreeView, Initializable {

	@FXML
	private TreeView<ITreeItemCellView> itemTree;
	private TreeItem<ITreeItemCellView> rootNode;
	private Supplier<ITreeItemCellView> treeItemCellView;

	public ItemTreeView(ItemTreePresenter presenter, Supplier<ITreeItemCellView> treeItemCellView) {
		super(presenter);
		this.treeItemCellView = treeItemCellView;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rootNode = new TreeItem<ITreeItemCellView>();
		
		rootNode.setExpanded(true);
		itemTree.setRoot(rootNode);
	}

	@Override
	public TreeItem<ITreeItemCellView> createRootNode() {
		return createItemAt(this.rootNode, rootNode.getChildren().size());
	}

	// TODO: this method should not be public
	// instead use a method like addItemAfterSelected(new TreeItemCellView());
	@Override
	public TreeItem<ITreeItemCellView> createItemAt(
			TreeItem<ITreeItemCellView> parent, int indexOfItem) {
		ITreeItemCellView resultCell = treeItemCellView.get();
		TreeItem<ITreeItemCellView> resultTreeItem = new TreeItem<ITreeItemCellView>(
				resultCell);
		resultCell.addEventHandler(ITreeItemCellView.TreeItemCellEvent.BASE,
				this::handleTreeCellEvent);

		parent.getChildren().add(indexOfItem, resultTreeItem);
		itemTree.layout();
		resultCell.activatedProperty().addListener((obs, o, n) -> {
			if (n) {
				activeCellProperty().set(resultCell);
			} else {
				activeCellProperty().set(null);
			}
		});
		resultCell.setActivated(true);

		return resultTreeItem;
	}

	public TreeView<ITreeItemCellView> getItemTree() {
		return itemTree;
	}

	public TreeItem<ITreeItemCellView> getRootNode() {
		return rootNode;
	}

	private void handleTreeCellEvent(TreeItemCellEvent event) {
		TreeItem<ITreeItemCellView> item = findItem((ITreeItemCellView) event
				.getSource());
		item.getValue().setActivated(false);
		int rowOfItem = itemTree.getRow(item);

		if (event.getEventType() == TreeItemCellEvent.CREATE_AFTER) {
			TreeItem<ITreeItemCellView> newItem = createItemAt(item.getParent(),
					item.getParent().getChildren().indexOf(item) + 1);
			newItem.getValue().setTitle(event.getTitle());
		} else if (event.getEventType() == TreeItemCellEvent.TOGGLE_EXPAND) {
			if (item.getChildren().size() > 0) {
				boolean expanded = item.expandedProperty().get();
				item.expandedProperty().set(!expanded);
				itemTree.layout();
				if (expanded) {
					item.getValue().setActivated(true);
				} else {
					item.getChildren().get(0).getValue().setActivated(true);
				}
			}
		} else if (event.getEventType() == TreeItemCellEvent.GOTO_PREVIOUS) {
			itemTree.getTreeItem(rowOfItem - 1).getValue().setActivated(true);
		} else if (event.getEventType() == TreeItemCellEvent.GOTO_NEXT) {
			itemTree.getTreeItem(rowOfItem + 1).getValue().setActivated(true);
		} else if (event.getEventType() == TreeItemCellEvent.DELETE) {
			item.getParent().getChildren().remove(item);
		} else if (event.getEventType() == TreeItemCellEvent.INDENT) {
			int localIdx = item.getParent().getChildren().indexOf(item);
			if (localIdx > 0) {
				TreeItem<ITreeItemCellView> previous = item.getParent()
						.getChildren().get(localIdx - 1);
				item.getParent().getChildren().remove(item);
				previous.getChildren().add(item);
				previous.setExpanded(true);
			}
		} else if (event.getEventType() == TreeItemCellEvent.MERGEWITH_NEXT) {
			int localIdx = item.getParent().getChildren().indexOf(item);
			if (localIdx + 1 != item.getParent().getChildren().size()) {
				TreeItem<ITreeItemCellView> next = item.getParent()
						.getChildren().get(localIdx + 1);
				item.getParent().getChildren().remove(next);
				item.getValue().appendToTitle(next.getValue().getTitle());
			}
		} else if (event.getEventType() == TreeItemCellEvent.MERGEWITH_PREVIOUS) {
			int localIdx = item.getParent().getChildren().indexOf(item);
			if (localIdx != 0) {
				TreeItem<ITreeItemCellView> prev = item.getParent()
						.getChildren().get(localIdx - 1);
				item.getParent().getChildren().remove(item);
				prev.getValue().appendToTitle(item.getValue().getTitle());
			}
		} else if (event.getEventType() == TreeItemCellEvent.MOVE_DOWN) {
			int localIdx = item.getParent().getChildren().indexOf(item);
			if (localIdx + 1 != item.getParent().getChildren().size()) {
				TreeItem<ITreeItemCellView> parent = item.getParent();
				parent.getChildren().remove(item);
				parent.getChildren().add(localIdx + 1, item);
			}
		} else if (event.getEventType() == TreeItemCellEvent.MOVE_UP) {
			int localIdx = item.getParent().getChildren().indexOf(item);
			if (localIdx >= 1) {
				TreeItem<ITreeItemCellView> parent = item.getParent();
				parent.getChildren().remove(item);
				parent.getChildren().add(localIdx - 1, item);
			}
		} else if (event.getEventType() == TreeItemCellEvent.OUTDENT) {
			TreeItem<ITreeItemCellView> parent = item.getParent();
			int localIdxOfParent = parent.getParent().getChildren()
					.indexOf(parent);
			parent.getChildren().remove(item);
			parent.getParent().getChildren().add(localIdxOfParent + 1, item);
		} else {
			// nothing to do
		}
	}

	private TreeItem<ITreeItemCellView> findItem(ITreeItemCellView source) {
		return findItem(this.rootNode, source);
	}

	private TreeItem<ITreeItemCellView> findItem(TreeItem<ITreeItemCellView> it,
			ITreeItemCellView item) {
		if (it.getValue() == item) {
			return it;
		} else {
			for (TreeItem<ITreeItemCellView> child : it.getChildren()) {
				TreeItem<ITreeItemCellView> result = findItem(child, item);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}

	/*
	 * =======================================================================
	 * == PROPERTIES ==
	 * =======================================================================
	 */

	private ObjectProperty<ITreeItemCellView> activeCell;

	public final ObjectProperty<ITreeItemCellView> activeCellProperty() {
		if (activeCell == null) {
			activeCell = new SimpleObjectProperty<>(null);

			activeCell.addListener((obs, o, n) -> {
				if (o != null) {
					o.setActivated(false);
				}
				if (n != null) {
					n.setActivated(true);
				}
			});
		}
		return this.activeCell;
	}

	public final ITreeItemCellView getActiveCell() {
		return this.activeCellProperty().get();
	}

	public final void setActiveCell(
			final ITreeItemCellView activeCell) {
		this.activeCellProperty().set(activeCell);
	}

}
