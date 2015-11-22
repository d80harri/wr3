package net.d80harri.wr.ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import net.d80harri.wr.service.Service;
import net.d80harri.wr.service.util.SpringAwareBeanMapper;
import net.d80harri.wr.ui.itemtree.ItemTreePresenter;
import net.d80harri.wr.ui.itemtree.cell.TreeItemCellPresenter;


public class AppPresenter {
	private Service service;
	
	private SpringAwareBeanMapper mapper;
	
	public AppPresenter(Service service, SpringAwareBeanMapper mapper) {
		this.service = service;
		this.mapper = mapper;				
		setItemTree(new ItemTreePresenter(service, mapper));
		getItemTree().load();
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
		getItemTree().addRootNode(new TreeItemCellPresenter(service, mapper));
	}

}
