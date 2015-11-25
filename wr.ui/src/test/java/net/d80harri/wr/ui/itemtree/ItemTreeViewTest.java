package net.d80harri.wr.ui.itemtree;

import static net.d80harri.wr.ui.util.TestUtilMethods.computeLater;
import static net.d80harri.wr.ui.util.TestUtilMethods.runLater;

import java.util.function.Supplier;

import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import net.d80harri.wr.ui.core.WrUiAppContext;
import net.d80harri.wr.ui.itemtree.cell.TreeItemCellEvent;
import net.d80harri.wr.ui.itemtree.cell.ItemCellPresenter;
import net.d80harri.wr.ui.itemtree.cell.ItemCellView;

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
		view = new ItemTreeView(presenter, () -> new ItemCellView(
				WrUiAppContext.get().getBean(ItemCellPresenter.class)));

		return view;
	}

	@Test
	public void shallCreateItemAt() {
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(0);
		TreeItem<ItemCellView> newCell = computeLater(() -> view
				.createRootNode());
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);

		TreeItem<ItemCellView> veryNewCell = computeLater(() -> view
				.createItemAt(view.getRootNode(), 1));
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(2)
				.contains(newCell, veryNewCell);
	}

	// TODO: reactivate the following testcases

	@Test
	public void shallCreateWithTitle() {
		TreeItem<ItemCellView> firstCell = computeLater(() -> {
			TreeItem<ItemCellView> result = view.createRootNode();
			result.getValue().getPresenter().setTitle("ASDF");
			return result;
		});

		TreeItemCellEvent event = new TreeItemCellEvent(
				TreeItemCellEvent.CREATE_AFTER);
		event.setTitle("JKLÖ");

		runLater(() -> firstCell.getValue().fireEvent(event));

		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(2);
		Assertions.assertThat(
				view.getRootNode().getChildren().get(0).getValue().getPresenter().getTitle())
				.isEqualTo("ASDF");
		Assertions.assertThat(
				view.getRootNode().getChildren().get(1).getValue().getPresenter().getTitle())
				.isEqualTo("JKLÖ");

	}

	@Test
	public void shallFocusFirstChildWhenCellRequestsExpand() {
		TreeItem<ItemCellView> childItem = computeLater(() -> {
			TreeItem<ItemCellView> root = view.createRootNode();
			root.getValue().getPresenter().setTitle("MyRoot");
			return view.createItemAt(root, 0);
		});

		runLater(() -> {
			view.setActiveCell(childItem.getParent().getValue());
			childItem
					.getParent()
					.getValue()
					.fireEvent(
							new TreeItemCellEvent(
									TreeItemCellEvent.TOGGLE_EXPAND));
		});

		Assertions.assertThat(childItem.getValue().getPresenter().isActivated()).isTrue();

		runLater(() -> childItem
				.getParent()
				.getValue()
				.fireEvent(
						new TreeItemCellEvent(TreeItemCellEvent.TOGGLE_EXPAND)));

		Assertions.assertThat(childItem.getValue().getPresenter().isActivated()).isFalse();
	}

	@Test
	public void expandWhenNoChildrenPresent() {
		TreeItem<ItemCellView> root = computeLater(new Supplier<TreeItem<ItemCellView>>() {

			@Override
			public TreeItem<ItemCellView> get() {
				TreeItem<ItemCellView> root = view.createRootNode();
				root.getValue().getPresenter().setTitle("MyRoot");
				return root;
			}
		});

		runLater(() -> root.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.TOGGLE_EXPAND)));

		Assertions.assertThat(root.getValue().getPresenter().isActivated()).isTrue();
	}

	@Test
	public void shallNotThrowExceptionWhenExpandWithNoChildren() {
		TreeItem<ItemCellView> leafItem = computeLater(new Supplier<TreeItem<ItemCellView>>() {

			@Override
			public TreeItem<ItemCellView> get() {
				return view.createRootNode();
			}
		});

		runLater(() -> leafItem.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.TOGGLE_EXPAND)));

		Assertions.assertThat(leafItem.isExpanded()).isFalse();
	}

	@Test
	public void shallFocusNextSibling() {
		TreeItem<ItemCellView> second = computeLater(new Supplier<TreeItem<ItemCellView>>() {

			@Override
			public TreeItem<ItemCellView> get() {
				TreeItem<ItemCellView> first = view.createRootNode();
				first.getValue().getPresenter().setTitle("MyRoot");
				return view.createRootNode();
			}
		});

		runLater(() -> second.previousSibling().getValue()
				.fireEvent(new TreeItemCellEvent(TreeItemCellEvent.GOTO_NEXT)));

		Assertions.assertThat(second.getValue().getPresenter().isActivated()).isTrue();
	}

	@Test
	public void shallFocusPreviousSibling() {
		TreeItem<ItemCellView> secondItem = computeLater(new Supplier<TreeItem<ItemCellView>>() {

			@Override
			public TreeItem<ItemCellView> get() {
				TreeItem<ItemCellView> first = view.createRootNode();
				first.getValue().getPresenter().setTitle("MyRoot");
				return view.createRootNode();
			}
		});

		runLater(() -> secondItem.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.GOTO_PREVIOUS)));

		Assertions.assertThat(
				secondItem.previousSibling().getValue().getPresenter().isActivated()).isTrue();
	}

	@Test
	public void shallDeleteItem() {
		TreeItem<ItemCellView> rootNode = computeLater(new Supplier<TreeItem<ItemCellView>>() {

			@Override
			public TreeItem<ItemCellView> get() {
				return view.createRootNode();
			}
		});

		runLater(() -> rootNode.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.DELETE)));

		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(0);
	}

	@Test
	public void shallIndentItem() {
		TreeItem<ItemCellView> rootNode1 = computeLater(() -> view
				.createRootNode());
		TreeItem<ItemCellView> rootNode2 = computeLater(() -> view
				.createRootNode());
		runLater(() -> rootNode2.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.INDENT)));

		Assertions.assertThat(rootNode1.getChildren()).hasSize(1);
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);
	}

	@Test
	public void indentFirstChild() {
		TreeItem<ItemCellView> rootNode1 = computeLater(() -> view
				.createRootNode());
		runLater(() -> rootNode1.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.INDENT)));

		Assertions.assertThat(rootNode1.getChildren()).hasSize(0);
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);
	}

	@Test
	public void shallMergeWithNext() {
		TreeItem<ItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<ItemCellView> result = view.createRootNode();
			result.getValue().getPresenter().setTitle("ASDF");
			return result;
		});
		TreeItem<ItemCellView> rootNode2 = computeLater(() -> {
			TreeItem<ItemCellView> result = view.createRootNode();
			result.getValue().getPresenter().setTitle("JKLÖ");
			return result;
		});
		runLater(() -> rootNode1.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MERGEWITH_NEXT)));

		Assertions.assertThat(rootNode1.getChildren()).hasSize(0);
		Assertions.assertThat(rootNode1.getValue().getPresenter().getTitle()).isEqualTo(
				"ASDFJKLÖ");
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);
	}

	@Test
	public void mergeWithNextWhenNoNext() {
		TreeItem<ItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<ItemCellView> result = view.createRootNode();
			result.getValue().getPresenter().setTitle("ASDF");
			return result;
		});
		runLater(() -> rootNode1.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MERGEWITH_NEXT)));

		Assertions.assertThat(rootNode1.getChildren()).hasSize(0);
		Assertions.assertThat(rootNode1.getValue().getPresenter().getTitle())
				.isEqualTo("ASDF");
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);
	}

	@Test
	public void shallMergeWithPrev() {
		TreeItem<ItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<ItemCellView> result = view.createRootNode();
			result.getValue().getPresenter().setTitle("ASDF");
			return result;
		});
		TreeItem<ItemCellView> rootNode2 = computeLater(() -> {
			TreeItem<ItemCellView> result = view.createRootNode();
			result.getValue().getPresenter().setTitle("JKLÖ");
			return result;
		});
		runLater(() -> rootNode2.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MERGEWITH_PREVIOUS)));

		Assertions.assertThat(rootNode1.getChildren()).hasSize(0);
		Assertions.assertThat(rootNode1.getValue().getPresenter().getTitle()).isEqualTo(
				"ASDFJKLÖ");
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);
	}

	@Test
	public void mergeWithPrevWhenNoPrev() {
		TreeItem<ItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<ItemCellView> result = view.createRootNode();
			result.getValue().getPresenter().setTitle("ASDF");
			return result;
		});
		runLater(() -> rootNode1.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MERGEWITH_PREVIOUS)));

		Assertions.assertThat(rootNode1.getChildren()).hasSize(0);
		Assertions.assertThat(rootNode1.getValue().getPresenter().getTitle())
				.isEqualTo("ASDF");
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);
	}

	@Test
	public void moveDown() {
		TreeItem<ItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<ItemCellView> result = view.createRootNode();
			result.getValue().getPresenter().setTitle("ASDF");
			return result;
		});
		TreeItem<ItemCellView> rootNode2 = computeLater(() -> {
			TreeItem<ItemCellView> result = view.createRootNode();
			result.getValue().getPresenter().setTitle("jklö");
			return result;
		});
		runLater(() -> rootNode1.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MOVE_DOWN)));

		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(2)
				.containsExactly(rootNode2, rootNode1);
	}

	@Test
	public void moveLastElementDown() {
		TreeItem<ItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<ItemCellView> result = view.createRootNode();
			result.getValue().getPresenter().setTitle("ASDF");
			return result;
		});

		runLater(() -> rootNode1.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MOVE_DOWN)));

		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1)
				.containsExactly(rootNode1);
	}

	@Test
	public void moveUp() {
		TreeItem<ItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<ItemCellView> result = view.createRootNode();
			result.getValue().getPresenter().setTitle("ASDF");
			return result;
		});
		TreeItem<ItemCellView> rootNode2 = computeLater(() -> {
			TreeItem<ItemCellView> result = view.createRootNode();
			result.getValue().getPresenter().setTitle("jklö");
			return result;
		});
		runLater(() -> rootNode2.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MOVE_UP)));

		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(2)
				.containsExactly(rootNode2, rootNode1);
	}

	@Test
	public void moveFirstItemUp() {
		TreeItem<ItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<ItemCellView> result = view.createRootNode();
			result.getValue().getPresenter().setTitle("ASDF");
			return result;
		});

		runLater(() -> rootNode1.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MOVE_UP)));

		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1)
				.containsExactly(rootNode1);
	}

	@Test
	public void shallOutdentItem() {
		TreeItem<ItemCellView> rootNode1 = computeLater(() -> view
				.createRootNode());
		TreeItem<ItemCellView> rootNode2 = computeLater(() -> view
				.createItemAt(rootNode1, 0));
		runLater(() -> rootNode2.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.OUTDENT)));

		Assertions.assertThat(rootNode1.getParent()).isEqualTo(
				view.getRootNode());
		Assertions.assertThat(rootNode2.getParent()).isEqualTo(
				view.getRootNode());
		Assertions.assertThat(rootNode1.getChildren()).hasSize(0);
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(2)
				.containsExactly(rootNode1, rootNode2);
	}

	/*
	 * TODO: reactivate this test
	 * 
	 * @Test public void activateOtherCell() { TreeItem<TreeItemCellView>
	 * rootNode1 = computeLater(() -> view .createRootNode());
	 * TreeItem<TreeItemCellView> rootNode2 = computeLater(() -> view
	 * .createRootNode());
	 * 
	 * click(rootNode1.getValue().getTxtTitle(), MouseButton.PRIMARY).type(
	 * KeyCode.SHIFT, KeyCode.ENTER);
	 * 
	 * Assertions.assertThat(rootNode1.getValue().getDetailPane().isVisible())
	 * .isTrue();
	 * Assertions.assertThat(rootNode2.getValue().getDetailPane().isVisible())
	 * .isFalse();
	 * 
	 * click(rootNode2.getValue().getTxtTitle(), MouseButton.PRIMARY);
	 * Assertions.assertThat(rootNode1.getValue().getDetailPane().isVisible())
	 * .isFalse();
	 * Assertions.assertThat(rootNode2.getValue().getDetailPane().isVisible())
	 * .isFalse(); }
	 */

	@Test
	public void activePropertyBinding() {
		TreeItem<ItemCellView> rootNode = computeLater(() -> view
				.createRootNode());

		runLater(() -> rootNode.getValue().getPresenter().setActivated(true));

		Assertions.assertThat(view.getActiveCell()).isEqualTo(
				rootNode.getValue());

		runLater(() -> rootNode.getValue().getPresenter().setActivated(false));

		Assertions.assertThat(view.getActiveCell()).isNull();
	}

}
