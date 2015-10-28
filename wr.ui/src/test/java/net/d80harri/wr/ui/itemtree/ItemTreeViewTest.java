package net.d80harri.wr.ui.itemtree;

import java.util.function.Supplier;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import net.d80harri.wr.ui.itemtree.TreeItemCellView.TreeItemCellEvent;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ItemTreeViewTest extends GuiTest {
	private ItemTreeView view;

	@Mock
	private ItemTreePresenter presenter;

	@Override
	protected Parent getRootNode() {
		MockitoAnnotations.initMocks(this);
		view = new ItemTreeView();
		view.setPresenter(presenter);

		return view;
	}

	@Test
	public void shallCreateNewRootNode() {
		TreeItem<TreeItemCellView> newCell = computeLater(() -> view
				.createRootNode());

		Assertions.assertThat(view.getItemTree().getRoot().getChildren())
				.hasSize(1).contains(newCell);
	}
	
	@Test
	public void shallCreateItemAt() {
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(0);
		TreeItem<TreeItemCellView> newCell = computeLater(() -> view
				.createRootNode());
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);

		TreeItem<TreeItemCellView> veryNewCell = computeLater(() -> view.createItemAt(view.getRootNode(), 1));
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(2).contains(newCell, veryNewCell);
	}

	@Test
	public void createdRootNodeShouldBeSelected() throws InterruptedException {
		TreeItem<TreeItemCellView> cellView = computeLater(() -> view
				.createRootNode());

		Assertions.assertThat(cellView.getValue().getTxtTitle().isFocused())
				.isTrue();
	}

	@Test
	public void shallAddNodeWhenCellRequestsNextSibling() {
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(0);
		TreeItem<TreeItemCellView> firstCell = computeLater(() -> view
				.createRootNode());
		TreeItem<TreeItemCellView> thirdCell = computeLater(() -> view
				.createRootNode());
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(2);

		runLater(() -> firstCell.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.CREATE_AFTER)));
		Assertions.assertThat(view.getRootNode().getChildren().get(0)).isEqualTo(firstCell);
		Assertions.assertThat(view.getRootNode().getChildren().get(2)).isEqualTo(thirdCell);
		// TODO: register listener that fires when node is created
		// use this listener to retrieve the reference of the newly created item
	}

	@Test
	public void shallAddNodeWhenCellRequestsChild() {
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(0);
		TreeItem<TreeItemCellView> newCell = computeLater(() -> view
				.createRootNode());
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);

		Assertions.assertThat(newCell.getChildren()).hasSize(0);
		runLater(() -> newCell.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.CREATE_CHILD)));
		Assertions.assertThat(newCell.getChildren()).hasSize(1);
	}

	@Test
	public void shallFocusFirstChildWhenCellRequestsExpand() {
		TreeItem<TreeItemCellView> childItem = computeLater(new Supplier<TreeItem<TreeItemCellView>>() {

			@Override
			public TreeItem<TreeItemCellView> get() {
				TreeItem<TreeItemCellView> root = view.createRootNode();
				root.getValue().getTxtTitle().setText("MyRoot");
				return view.createItemAt(root, 0);
			}
		});

		runLater(() -> childItem.getParent().getValue()
				.fireEvent(new TreeItemCellEvent(TreeItemCellEvent.EXPAND)));

		Assertions.assertThat(childItem.getValue().getTxtTitle().isFocused())
				.isTrue();
	}
	
	@Test
	public void shallNotThrowExceptionWhenExpandWithNoChildren() {
		TreeItem<TreeItemCellView> leafItem = computeLater(new Supplier<TreeItem<TreeItemCellView>>() {

			@Override
			public TreeItem<TreeItemCellView> get() {
				return view.createRootNode();
			}
		});

		runLater(() -> leafItem.getValue()
				.fireEvent(new TreeItemCellEvent(TreeItemCellEvent.EXPAND)));

		Assertions.assertThat(leafItem.getValue().getTxtTitle().isFocused())
				.isTrue();
	}

	@Test
	public void shallFocusNextSibling() {
		TreeItem<TreeItemCellView> second = computeLater(new Supplier<TreeItem<TreeItemCellView>>() {

			@Override
			public TreeItem<TreeItemCellView> get() {
				TreeItem<TreeItemCellView> first = view.createRootNode();
				first.getValue().getTxtTitle().setText("MyRoot");
				return view.createRootNode();
			}
		});

		runLater(() -> second.previousSibling().getValue()
				.fireEvent(new TreeItemCellEvent(TreeItemCellEvent.MOVE_DOWN)));

		Assertions.assertThat(second.getValue().getTxtTitle().isFocused())
				.isTrue();
	}

	@Test
	public void shallFocusParent() {
		TreeItem<TreeItemCellView> childItem = computeLater(new Supplier<TreeItem<TreeItemCellView>>() {

			@Override
			public TreeItem<TreeItemCellView> get() {
				TreeItem<TreeItemCellView> root = view.createRootNode();
				root.getValue().getTxtTitle().setText("MyRoot");
				return view.createItemAt(root, 0);
			}
		});

		runLater(() -> childItem.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MOVE_TO_PARENT)));

		Assertions.assertThat(
				childItem.getParent().getValue().getTxtTitle().isFocused())
				.isTrue();
	}
	
	@Test
	public void shallNotThrowExceptionWhenMoveToParentFromRootNode() {
		TreeItem<TreeItemCellView> root = computeLater(new Supplier<TreeItem<TreeItemCellView>>() {

			@Override
			public TreeItem<TreeItemCellView> get() {
				return view.createRootNode();
			}
		});

		runLater(() -> root.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MOVE_TO_PARENT)));

		Assertions.assertThat(
				root.getValue().getTxtTitle().isFocused())
				.isTrue();
	}

	@Test
	public void shallFocusPreviousSibling() {
		TreeItem<TreeItemCellView> secondItem = computeLater(new Supplier<TreeItem<TreeItemCellView>>() {

			@Override
			public TreeItem<TreeItemCellView> get() {
				TreeItem<TreeItemCellView> first = view.createRootNode();
				first.getValue().getTxtTitle().setText("MyRoot");
				return view.createRootNode();
			}
		});

		runLater(() -> secondItem.getValue()
				.fireEvent(new TreeItemCellEvent(TreeItemCellEvent.MOVE_UP)));

		Assertions.assertThat(secondItem.previousSibling().getValue().getTxtTitle().isFocused())
				.isTrue();
	}

	@SuppressWarnings("unchecked")
	private <T> T computeLater(final Supplier<T> supplier) {
		Object[] cell = new Object[1];
		Runnable runable = new Runnable() {

			@Override
			public void run() {
				synchronized (this) {
					try {
						cell[0] = supplier.get();
					} finally {
						notify();
					}
				}
			}
		};

		Platform.runLater(runable);
		synchronized (runable) {
			try {
				runable.wait();
			} catch (InterruptedException e) {
				throw new Error(e);
			}
		}
		return (T) cell[0];
	}

	private void runLater(final Runnable callback) {
		Runnable runable = new Runnable() {

			@Override
			public void run() {
				synchronized (this) {
					try {
						callback.run();
					} finally {
						notify();
					}
				}
			}
		};

		Platform.runLater(runable);
		synchronized (runable) {
			try {
				runable.wait();
			} catch (InterruptedException e) {
				throw new Error(e);
			}
		}
	}
}
