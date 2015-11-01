package net.d80harri.wr.ui.itemtree;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
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

		TreeItem<TreeItemCellView> veryNewCell = computeLater(() -> view
				.createItemAt(view.getRootNode(), 1));
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(2)
				.contains(newCell, veryNewCell);
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
		Assertions.assertThat(view.getRootNode().getChildren().get(0))
				.isEqualTo(firstCell);
		Assertions.assertThat(view.getRootNode().getChildren().get(2))
				.isEqualTo(thirdCell);
		// TODO: register listener that fires when node is created
		// use this listener to retrieve the reference of the newly created item
	}

	@Test
	public void shallCreateWithTitle() {
		TreeItem<TreeItemCellView> firstCell = computeLater(() -> {
			TreeItem<TreeItemCellView> result = view.createRootNode();
			result.getValue().getTxtTitle().setText("ASDF");
			return result;
		});

		TreeItemCellEvent event = new TreeItemCellEvent(
				TreeItemCellEvent.CREATE_AFTER);
		event.setTitle("JKLÖ");

		runLater(() -> firstCell.getValue().fireEvent(event));

		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(2);
		Assertions.assertThat(
				view.getRootNode().getChildren().get(0).getValue()
						.getTxtTitle().getText()).isEqualTo("ASDF");
		Assertions.assertThat(
				view.getRootNode().getChildren().get(1).getValue()
						.getTxtTitle().getText()).isEqualTo("JKLÖ");

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

		runLater(() -> childItem
				.getParent()
				.getValue()
				.fireEvent(
						new TreeItemCellEvent(TreeItemCellEvent.TOGGLE_EXPAND)));

		Assertions.assertThat(childItem.getValue().getTxtTitle().isFocused())
				.isTrue();

		runLater(() -> childItem
				.getParent()
				.getValue()
				.fireEvent(
						new TreeItemCellEvent(TreeItemCellEvent.TOGGLE_EXPAND)));

		Assertions.assertThat(childItem.getValue().getTxtTitle().isFocused())
				.isFalse();
	}

	@Test
	public void expandWhenNoChildrenPresent() {
		TreeItem<TreeItemCellView> root = computeLater(new Supplier<TreeItem<TreeItemCellView>>() {

			@Override
			public TreeItem<TreeItemCellView> get() {
				TreeItem<TreeItemCellView> root = view.createRootNode();
				root.getValue().getTxtTitle().setText("MyRoot");
				return root;
			}
		});

		runLater(() -> root.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.TOGGLE_EXPAND)));

		Assertions.assertThat(root.getValue().getTxtTitle().isFocused())
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

		runLater(() -> leafItem.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.TOGGLE_EXPAND)));

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
				.fireEvent(new TreeItemCellEvent(TreeItemCellEvent.GOTO_NEXT)));

		Assertions.assertThat(second.getValue().getTxtTitle().isFocused())
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

		runLater(() -> secondItem.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.GOTO_PREVIOUS)));

		Assertions.assertThat(
				secondItem.previousSibling().getValue().getTxtTitle()
						.isFocused()).isTrue();
	}

	@Test
	public void shallDeleteItem() {
		TreeItem<TreeItemCellView> rootNode = computeLater(new Supplier<TreeItem<TreeItemCellView>>() {

			@Override
			public TreeItem<TreeItemCellView> get() {
				return view.createRootNode();
			}
		});

		runLater(() -> rootNode.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.DELETE)));

		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(0);
	}

	@Test
	public void shallIndentItem() {
		TreeItem<TreeItemCellView> rootNode1 = computeLater(() -> view
				.createRootNode());
		TreeItem<TreeItemCellView> rootNode2 = computeLater(() -> view
				.createRootNode());
		runLater(() -> rootNode2.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.INDENT)));

		Assertions.assertThat(rootNode1.getChildren()).hasSize(1);
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);
	}

	@Test
	public void shallMergeWithNext() {
		TreeItem<TreeItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<TreeItemCellView> result = view.createRootNode();
			result.getValue().getTxtTitle().setText("ASDF");
			return result;
		});
		TreeItem<TreeItemCellView> rootNode2 = computeLater(() -> {
			TreeItem<TreeItemCellView> result = view.createRootNode();
			result.getValue().getTxtTitle().setText("JKLÖ");
			return result;
		});
		runLater(() -> rootNode1.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MERGEWITH_NEXT)));

		Assertions.assertThat(rootNode1.getChildren()).hasSize(0);
		Assertions.assertThat(rootNode1.getValue().getTxtTitle().getText())
				.isEqualTo("ASDFJKLÖ");
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);
	}

	@Test
	public void mergeWithNextWhenNoNext() {
		TreeItem<TreeItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<TreeItemCellView> result = view.createRootNode();
			result.getValue().getTxtTitle().setText("ASDF");
			return result;
		});
		runLater(() -> rootNode1.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MERGEWITH_NEXT)));

		Assertions.assertThat(rootNode1.getChildren()).hasSize(0);
		Assertions.assertThat(rootNode1.getValue().getTxtTitle().getText())
				.isEqualTo("ASDF");
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);
	}

	@Test
	public void shallMergeWithPrev() {
		TreeItem<TreeItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<TreeItemCellView> result = view.createRootNode();
			result.getValue().getTxtTitle().setText("ASDF");
			return result;
		});
		TreeItem<TreeItemCellView> rootNode2 = computeLater(() -> {
			TreeItem<TreeItemCellView> result = view.createRootNode();
			result.getValue().getTxtTitle().setText("JKLÖ");
			return result;
		});
		runLater(() -> rootNode2.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MERGEWITH_PREVIOUS)));

		Assertions.assertThat(rootNode1.getChildren()).hasSize(0);
		Assertions.assertThat(rootNode1.getValue().getTxtTitle().getText())
				.isEqualTo("ASDFJKLÖ");
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);
	}

	@Test
	public void mergeWithPrevWhenNoPrev() {
		TreeItem<TreeItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<TreeItemCellView> result = view.createRootNode();
			result.getValue().getTxtTitle().setText("ASDF");
			return result;
		});
		runLater(() -> rootNode1.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MERGEWITH_PREVIOUS)));

		Assertions.assertThat(rootNode1.getChildren()).hasSize(0);
		Assertions.assertThat(rootNode1.getValue().getTxtTitle().getText())
				.isEqualTo("ASDF");
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);
	}

	@Test
	public void moveDown() {
		TreeItem<TreeItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<TreeItemCellView> result = view.createRootNode();
			result.getValue().getTxtTitle().setText("ASDF");
			return result;
		});
		TreeItem<TreeItemCellView> rootNode2 = computeLater(() -> {
			TreeItem<TreeItemCellView> result = view.createRootNode();
			result.getValue().getTxtTitle().setText("jklö");
			return result;
		});
		runLater(() -> rootNode1.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MOVE_DOWN)));

		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(2)
				.containsExactly(rootNode2, rootNode1);
	}

	@Test
	public void moveLastElementDown() {
		TreeItem<TreeItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<TreeItemCellView> result = view.createRootNode();
			result.getValue().getTxtTitle().setText("ASDF");
			return result;
		});

		runLater(() -> rootNode1.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MOVE_DOWN)));

		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1)
				.containsExactly(rootNode1);
	}

	@Test
	public void moveUp() {
		TreeItem<TreeItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<TreeItemCellView> result = view.createRootNode();
			result.getValue().getTxtTitle().setText("ASDF");
			return result;
		});
		TreeItem<TreeItemCellView> rootNode2 = computeLater(() -> {
			TreeItem<TreeItemCellView> result = view.createRootNode();
			result.getValue().getTxtTitle().setText("jklö");
			return result;
		});
		runLater(() -> rootNode2.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MOVE_UP)));

		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(2)
				.containsExactly(rootNode2, rootNode1);
	}

	@Test
	public void moveFirstItemUp() {
		TreeItem<TreeItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<TreeItemCellView> result = view.createRootNode();
			result.getValue().getTxtTitle().setText("ASDF");
			return result;
		});

		runLater(() -> rootNode1.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MOVE_UP)));

		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1)
				.containsExactly(rootNode1);
	}

	@Test
	public void shallOutdentItem() {
		TreeItem<TreeItemCellView> rootNode1 = computeLater(() -> view
				.createRootNode());
		TreeItem<TreeItemCellView> rootNode2 = computeLater(() -> view
				.createItemAt(rootNode1, 0));
		runLater(() -> rootNode2.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.OUTDENT)));

		Assertions.assertThat(rootNode1.getChildren()).hasSize(0);
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(2);
	}

	private <T> T computeLater(final Supplier<T> supplier) {
		final FutureTask<T> query = new FutureTask<>(new Callable<T>() {
			@Override
			public T call() throws Exception {
				return supplier.get();
			}
		});

		Platform.runLater(query);

		try {
			return query.get();
		} catch (InterruptedException | ExecutionException e) {
			throw new Error(e.getMessage(), e);
		}
	}

	private void runLater(final Runnable callback) {
		computeLater(() -> {
			callback.run();
			return null;
		});
	}
}
