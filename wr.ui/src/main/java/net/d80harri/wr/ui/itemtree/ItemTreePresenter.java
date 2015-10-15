package net.d80harri.wr.ui.itemtree;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import net.d80harri.wr.ui.core.ServiceProxy;
import net.d80harri.wr.ui.itemtree.ItemTreeCellPresenter.ItemTreeCellEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemTreePresenter implements Initializable {
	@Autowired
	private ServiceProxy serviceProxy;
	
	@Autowired
	private ItemTreePresenterLogic logic;
	
	@FXML
	private TreeView<ItemModel> itemTree;

	private Optional<TreeItem<ItemModel>> rootItem = Optional.empty();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rootItem = Optional.of(createNode(null));

		itemTree.setRoot(rootItem.get());
		itemTree.setShowRoot(false);
		itemTree.setCellFactory(this::createTreeItemCell);
		itemTree.setEditable(true);
	}

	private TreeItem<ItemModel> createNode(ItemModel item) {
		return new TreeItem<ItemModel>(item) {
			private boolean isLeaf;
			private boolean isFirstTimeChildren = true;
			private boolean isFirstTimeLeaf = true;

			@Override
			public ObservableList<TreeItem<ItemModel>> getChildren() {
				if (isFirstTimeChildren) {
					isFirstTimeChildren = false;

					// First getChildren() call, so we actually go off and
					// determine the children of the File contained in this
					// TreeItem.
					super.getChildren().setAll(buildChildren(this));
				}
				return super.getChildren();
			}

			@Override
			public boolean isLeaf() {
				if (isFirstTimeLeaf) {
					isFirstTimeLeaf = false;
					isLeaf = item != null && logic.getChildItemsOf(item.getId()).size() == 0; // TODO: Performance
				}

				return isLeaf;
			}

			private ObservableList<TreeItem<ItemModel>> buildChildren(
					TreeItem<ItemModel> TreeItem) {
				ItemModel f = TreeItem.getValue();

				ObservableList<TreeItem<ItemModel>> children = FXCollections
						.observableArrayList();
				List<ItemModel> childrenModels;

				if (f == null) { // this is root
					childrenModels = logic.getRootItems();
				} else {
					childrenModels = logic.getChildItemsOf(f.getId());
				}
				children.addAll(childrenModels.stream().map(i -> createNode(i))
						.collect(Collectors.toList()));
				return children;
			}
		};
	}

	private TreeCell<ItemModel> createTreeItemCell(TreeView<ItemModel> param) {
		ItemTreeCell cell = new ItemTreeCell();
		cell.addEventHandler(ItemTreeCellPresenter.ItemTreeCellEvent.APPEND_SIBLING_REQUESTED, this::appendSibling);
		return cell;
	}
	
	private void appendSibling(ItemTreeCellEvent event) {
		System.out.println("Appending sibling requested for " + event.getRequestSource().getTitle());
		ItemModel newItem = new ItemModel();
		TreeItem<ItemModel> item = new TreeItem<ItemModel>(newItem);
		
		ItemTreeCell cell = (ItemTreeCell) event.getSource();
		cell.getTreeItem().getParent().getChildren().add(item);
		itemTree.getSelectionModel().select(item);
		
		itemTree.layout();
		itemTree.edit(itemTree.getFocusModel().getFocusedItem());
	}
}
