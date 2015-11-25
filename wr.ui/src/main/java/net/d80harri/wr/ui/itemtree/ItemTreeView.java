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
import net.d80harri.wr.ui.itemtree.cell.TreeItemCellPresenter;
import net.d80harri.wr.ui.itemtree.cell.TreeItemCellView;

import org.fxmisc.easybind.EasyBind;

public class ItemTreeView extends ViewBase<ItemTreePresenter> implements
		Initializable {

	@FXML
	private TreeView<TreeItemCellView> itemTree;
	private TreeItem<TreeItemCellView> rootNode;
	private Supplier<TreeItemCellView> treeItemCellFactory;

	private ObservableList<TreeItem<TreeItemCellView>> mappedList;

	public ItemTreeView() {
		// TODO Auto-generated constructor stub
	}

	public ItemTreeView(ItemTreePresenter presenter,
			Supplier<TreeItemCellView> treeItemCellView) {
		super(presenter);
		this.treeItemCellFactory = treeItemCellView;
	}

	public Supplier<TreeItemCellView> getTreeItemCellFactory() {
		return treeItemCellFactory;
	}

	public void setTreeItemCellFactory(
			Supplier<TreeItemCellView> treeItemCellFactory) {
		this.treeItemCellFactory = treeItemCellFactory;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rootNode = new TreeItem<TreeItemCellView>();

		rootNode.setExpanded(true);
		itemTree.setRoot(rootNode);

		EasyBind.subscribe(presenterProperty(), this::presenterChanged);
	}

	private void presenterChanged(ItemTreePresenter presenter) {
		if (presenter == null) {
			this.mappedList = null;
		} else {
			this.mappedList = EasyBind.map(presenter.getRootItems(),
					i -> this.createTreeItemFromPresenter(i));
			EasyBind.listBind(rootNode.getChildren(), mappedList);
		}
	}

	private TreeItem<TreeItemCellView> createTreeItemFromPresenter(
			TreeItemCellPresenter presenter) {
		TreeItem<TreeItemCellView> result = new TreeItem<TreeItemCellView>(
				createCellViewFromPresenter(presenter)) {
			ObservableList<TreeItem<TreeItemCellView>> children;

			@Override
			public boolean isLeaf() {
				return getChildren().size() == 0;
			}

			@Override
			public ObservableList<TreeItem<TreeItemCellView>> getChildren() {
				if (children == null) {
					children = EasyBind.map(presenter.getChildren(),
							i -> createTreeItemFromPresenter(i));
					presenter.loadChildren();
				}
				return this.children;
			}
		};

		result.expandedProperty().bindBidirectional(
				presenter.expandedProperty());
		return result;
	}

	private TreeItemCellView createCellViewFromPresenter(
			TreeItemCellPresenter presenter) {
		TreeItemCellView result = new TreeItemCellView(presenter);
		return result;
	}

	public TreeView<TreeItemCellView> getItemTree() {
		return itemTree;
	}

	public TreeItem<TreeItemCellView> getRootNode() {
		return rootNode;
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
