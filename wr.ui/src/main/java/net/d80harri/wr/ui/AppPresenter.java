package net.d80harri.wr.ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import net.d80harri.wr.ui.itemtree.ItemTreePresenter;
import net.d80harri.wr.ui.itemtree.cell.TreeItemCellPresenter;


public class AppPresenter {

	public AppPresenter() {
		setItemTree(new ItemTreePresenter());
	}
	
	private ObjectProperty<ItemTreePresenter> itemTree;

	public final ObjectProperty<ItemTreePresenter> itemTreeProperty() {
		if (itemTree == null) {
			itemTree = new SimpleObjectProperty<>(this, "itemTree");
		}
		return this.itemTree;
	}

	public final net.d80harri.wr.ui.itemtree.ItemTreePresenter getItemTree() {
		return this.itemTreeProperty().get();
	}

	public final void setItemTree(
			final net.d80harri.wr.ui.itemtree.ItemTreePresenter itemTree) {
		this.itemTreeProperty().set(itemTree);
	}

	public void createRootItem() {
		getItemTree().addRootNode(new TreeItemCellPresenter());
	}

}
