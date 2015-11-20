package net.d80harri.wr.ui.itemtree;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import net.d80harri.wr.ui.core.ViewBase;
import net.d80harri.wr.ui.itemtree.cell.TreeItemCellEvent;
import net.d80harri.wr.ui.itemtree.cell.TreeItemCellPresenter;
import net.d80harri.wr.ui.itemtree.cell.TreeItemCellView;

import org.fxmisc.easybind.EasyBind;

public class ItemTreeView extends ViewBase<ItemTreePresenter> implements
		Initializable {

	@FXML
	private TreeView<TreeItemCellView> itemTree;
	private TreeItem<TreeItemCellView> rootNode;
	private Supplier<TreeItemCellView> treeItemCellFactory;

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

		presenterProperty()
				.addListener(
						(obs, o, n) -> {
							ObservableList<TreeItem<TreeItemCellView>> mappedList = EasyBind
									.map(n.getRootItems(), i -> new TreeItem<>(
											new TreeItemCellView(i)));
							n.getRootItems()
									.addListener(
											new ListChangeListener<TreeItemCellPresenter>() {

												@Override
												public void onChanged(
														javafx.collections.ListChangeListener.Change<? extends TreeItemCellPresenter> c) {
													System.out.println("?"
															+ n.getRootItems()
																	.size());
												}

											});
							EasyBind.listBind(rootNode.getChildren(),
									mappedList);
						});
		rootNode.getChildren().addListener(
				new ListChangeListener<TreeItem<TreeItemCellView>>() {

					@Override
					public void onChanged(
							javafx.collections.ListChangeListener.Change<? extends TreeItem<TreeItemCellView>> c) {
						System.out.println("!" + rootNode.getChildren().size());
					}

				});
	}

	// TODO: this method should not be public
	// instead use a method like addItemAfterSelected(new TreeItemCellView());
	public TreeItem<TreeItemCellView> createItemAt(
			TreeItem<TreeItemCellView> parent, int indexOfItem) {
		TreeItemCellView resultCell = treeItemCellFactory.get();
		TreeItem<TreeItemCellView> resultTreeItem = new TreeItem<TreeItemCellView>(
				resultCell);
		resultCell.addEventHandler(TreeItemCellEvent.BASE,
				this::handleTreeCellEvent);

		parent.getChildren().add(indexOfItem, resultTreeItem);
		itemTree.layout();
		EasyBind.select(resultCell.presenterProperty())
				.selectObject(i -> i.activatedProperty())
				.addListener(
						(obs, o, n) -> {
							if (n) {
								getPresenter().setActiveItem(
										resultCell.getPresenter());
							} else {
								getPresenter().setActiveItem(null);
							}
						});

		resultCell.getPresenter().setActivated(true);

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
		item.getValue().getPresenter().setActivated(false);
		int rowOfItem = itemTree.getRow(item);

		if (event.getEventType() == TreeItemCellEvent.CREATE_AFTER) {
			TreeItemCellPresenter newPresenter = new TreeItemCellPresenter();
			newPresenter.setTitle(event.getTitle());
			getPresenter().addNodeToActive(newPresenter);
		} else if (event.getEventType() == TreeItemCellEvent.TOGGLE_EXPAND) {
			if (item.getChildren().size() > 0) {
				boolean expanded = item.expandedProperty().get();
				item.expandedProperty().set(!expanded);
				itemTree.layout();
				if (expanded) {
					item.getValue().getPresenter().setActivated(true);
				} else {
					item.getChildren().get(0).getValue().getPresenter()
							.setActivated(true);
				}
			}
		} else if (event.getEventType() == TreeItemCellEvent.GOTO_PREVIOUS) {
			itemTree.getTreeItem(rowOfItem - 1).getValue().getPresenter()
					.setActivated(true);
		} else if (event.getEventType() == TreeItemCellEvent.GOTO_NEXT) {
			itemTree.getTreeItem(rowOfItem + 1).getValue().getPresenter()
					.setActivated(true);
		} else if (event.getEventType() == TreeItemCellEvent.DELETE) {
			item.getParent().getChildren().remove(item);
		} else if (event.getEventType() == TreeItemCellEvent.INDENT) {
			int localIdx = item.getParent().getChildren().indexOf(item);
			if (localIdx > 0) {
				TreeItem<TreeItemCellView> previous = item.getParent()
						.getChildren().get(localIdx - 1);
				item.getParent().getChildren().remove(item);
				previous.getChildren().add(item);
				previous.setExpanded(true);
			}
		} else if (event.getEventType() == TreeItemCellEvent.MERGEWITH_NEXT) {
			int localIdx = item.getParent().getChildren().indexOf(item);
			if (localIdx + 1 != item.getParent().getChildren().size()) {
				TreeItem<TreeItemCellView> next = item.getParent()
						.getChildren().get(localIdx + 1);
				item.getParent().getChildren().remove(next);
				item.getValue().appendToTitle(
						next.getValue().getPresenter().getTitle());
			}
		} else if (event.getEventType() == TreeItemCellEvent.MERGEWITH_PREVIOUS) {
			int localIdx = item.getParent().getChildren().indexOf(item);
			if (localIdx != 0) {
				TreeItem<TreeItemCellView> prev = item.getParent()
						.getChildren().get(localIdx - 1);
				item.getParent().getChildren().remove(item);
				prev.getValue().appendToTitle(
						item.getValue().getPresenter().getTitle());
			}
		} else if (event.getEventType() == TreeItemCellEvent.MOVE_DOWN) {
			int localIdx = item.getParent().getChildren().indexOf(item);
			if (localIdx + 1 != item.getParent().getChildren().size()) {
				TreeItem<TreeItemCellView> parent = item.getParent();
				parent.getChildren().remove(item);
				parent.getChildren().add(localIdx + 1, item);
			}
		} else if (event.getEventType() == TreeItemCellEvent.MOVE_UP) {
			int localIdx = item.getParent().getChildren().indexOf(item);
			if (localIdx >= 1) {
				TreeItem<TreeItemCellView> parent = item.getParent();
				parent.getChildren().remove(item);
				parent.getChildren().add(localIdx - 1, item);
			}
		} else if (event.getEventType() == TreeItemCellEvent.OUTDENT) {
			TreeItem<TreeItemCellView> parent = item.getParent();
			int localIdxOfParent = parent.getParent().getChildren()
					.indexOf(parent);
			parent.getChildren().remove(item);
			parent.getParent().getChildren().add(localIdxOfParent + 1, item);
		} else {
			// nothing to do
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
