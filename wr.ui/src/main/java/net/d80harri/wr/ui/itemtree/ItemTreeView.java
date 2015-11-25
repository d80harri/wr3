package net.d80harri.wr.ui.itemtree;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import net.d80harri.wr.ui.core.ViewBase;
import net.d80harri.wr.ui.itemtree.cell.ItemCellPresenter;
import net.d80harri.wr.ui.itemtree.cell.ItemCellView;
import net.d80harri.wr.ui.itemtree.cell.ItemTreeItem;

import org.fxmisc.easybind.EasyBind;

public class ItemTreeView extends ViewBase<ItemTreePresenter> implements
		Initializable {

	@FXML
	private TreeView<ItemCellView> itemTree;
	private TreeItem<ItemCellView> rootNode;
	private Supplier<ItemCellView> treeItemCellFactory;

	private ObservableList<ItemTreeItem> mappedList;

	public ItemTreeView() {
		// TODO Auto-generated constructor stub
	}

	public ItemTreeView(ItemTreePresenter presenter,
			Supplier<ItemCellView> treeItemCellView) {
		super(presenter);
		this.treeItemCellFactory = treeItemCellView;
	}

	public Supplier<ItemCellView> getTreeItemCellFactory() {
		return treeItemCellFactory;
	}

	public void setTreeItemCellFactory(
			Supplier<ItemCellView> treeItemCellFactory) {
		this.treeItemCellFactory = treeItemCellFactory;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rootNode = new TreeItem<ItemCellView>();

		rootNode.setExpanded(true);
		itemTree.setRoot(rootNode);

		EasyBind.subscribe(presenterProperty(), this::presenterChanged);
	}

	private void presenterChanged(ItemTreePresenter presenter) {
		if (presenter == null) {
			this.mappedList = null;
		} else {
			this.mappedList = EasyBind.map(presenter.getRootItems(),
					i -> new ItemTreeItem(i));
			EasyBind.listBind(rootNode.getChildren(), mappedList);
		}
	}

	public TreeView<ItemCellView> getItemTree() {
		return itemTree;
	}

	public TreeItem<ItemCellView> getRootNode() {
		return rootNode;
	}

}
