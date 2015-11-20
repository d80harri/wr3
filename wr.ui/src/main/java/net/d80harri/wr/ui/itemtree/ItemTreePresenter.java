package net.d80harri.wr.ui.itemtree;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.d80harri.wr.ui.itemtree.cell.TreeItemCellPresenter;

public class ItemTreePresenter {
	
	public ItemTreePresenter() {
		// TODO Auto-generated constructor stub
	}
	
	public void addRootNode(TreeItemCellPresenter rootNode) {
		getRootItems().add(rootNode);
	}
	
	private ObservableList<TreeItemCellPresenter> rootItems = FXCollections.observableArrayList();
	
	public ObservableList<TreeItemCellPresenter> getRootItems() {
		return this.rootItems;
	}

	private ObjectProperty<TreeItemCellPresenter> activeItem;

	public final ObjectProperty<TreeItemCellPresenter> activeItemProperty() {
		if (activeItem == null) {
			activeItem = new SimpleObjectProperty<>(this, "activeItem");

			activeItem.addListener((obs, o, n) -> {
				if (o != null) {
					o.setActivated(false);
				}
				if (n != null) {
					n.setActivated(true);
				}
			});
		}
		return this.activeItem;
	}

	public final TreeItemCellPresenter getActiveItem() {
		return this.activeItemProperty().get();
	}

	public final void setActiveItem(final TreeItemCellPresenter activeItem) {
		this.activeItemProperty().set(activeItem);
	}

	public void addNodeToActive(TreeItemCellPresenter newPresenter) {
		getActiveItem().addChildItem(newPresenter);
	}	
}
