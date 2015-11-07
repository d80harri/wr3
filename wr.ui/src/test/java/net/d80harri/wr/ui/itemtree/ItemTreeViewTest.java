package net.d80harri.wr.ui.itemtree;

import static net.d80harri.wr.ui.util.TestUtilMethods.computeLater;
import static net.d80harri.wr.ui.util.TestUtilMethods.runLater;

import java.util.function.Supplier;

import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import net.d80harri.wr.ui.core.WrUiAppContext;
import net.d80harri.wr.ui.itemtree.cell.ITreeItemCellPresenter;
import net.d80harri.wr.ui.itemtree.cell.ITreeItemCellView;
import net.d80harri.wr.ui.itemtree.cell.ITreeItemCellView.TreeItemCellEvent;
import net.d80harri.wr.ui.itemtree.cell.TreeItemCellView;

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
		view = new ItemTreeView(presenter, () -> new TreeItemCellView(WrUiAppContext.get().getBean(ITreeItemCellPresenter.class)));

		return view;
	}

	@Test
	public void shallCreateNewRootNode() {
		TreeItem<ITreeItemCellView> newCell = computeLater(() -> {
			TreeItem<ITreeItemCellView> result = view.createRootNode();
			result.getValue().setTitle("Root");
			return result;
		});

		Assertions.assertThat(view.getItemTree().getRoot().getChildren())
				.hasSize(1).contains(newCell);
		Assertions.assertThat(newCell.getChildren()).hasSize(0);
		Assertions.assertThat(newCell.getParent())
				.isEqualTo(view.getRootNode());
	}

	@Test
	public void shallCreateItemAt() {
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(0);
		TreeItem<ITreeItemCellView> newCell = computeLater(() -> view
				.createRootNode());
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);

		TreeItem<ITreeItemCellView> veryNewCell = computeLater(() -> view
				.createItemAt(view.getRootNode(), 1));
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(2)
				.contains(newCell, veryNewCell);
	}

	@Test
	public void createdRootNodeShouldBeSelected() throws InterruptedException {
		TreeItem<ITreeItemCellView> cellView = computeLater(() -> view
				.createRootNode());

		Assertions.assertThat(cellView.getValue().isActivated())
				.isTrue();
	}

	@Test
	public void shallAddNodeWhenCellRequestsNextSibling() {
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(0);
		TreeItem<ITreeItemCellView> firstCell = computeLater(() -> view
				.createRootNode());
		
		TreeItem<ITreeItemCellView> thirdCell = computeLater(() -> view
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

	// TODO: reactivate the following testcases
	
	@Test
	public void shallCreateWithTitle() {
		TreeItem<ITreeItemCellView> firstCell = computeLater(() -> {
			TreeItem<ITreeItemCellView> result = view.createRootNode();
			result.getValue().setTitle("ASDF");
			return result;
		});

		TreeItemCellEvent event = new TreeItemCellEvent(
				TreeItemCellEvent.CREATE_AFTER);
		event.setTitle("JKLÖ");

		runLater(() -> firstCell.getValue().fireEvent(event));

		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(2);
		Assertions.assertThat(
				view.getRootNode().getChildren().get(0).getValue()
						.getTitle()).isEqualTo("ASDF");
		Assertions.assertThat(
				view.getRootNode().getChildren().get(1).getValue()
						.getTitle()).isEqualTo("JKLÖ");

	}

	@Test
	public void shallFocusFirstChildWhenCellRequestsExpand() {
		TreeItem<ITreeItemCellView> childItem = computeLater(() -> {
			TreeItem<ITreeItemCellView> root = view.createRootNode();
			root.getValue().setTitle("MyRoot");
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

		Assertions.assertThat(childItem.getValue().isActivated())
				.isTrue();

		runLater(() -> childItem
				.getParent()
				.getValue()
				.fireEvent(
						new TreeItemCellEvent(TreeItemCellEvent.TOGGLE_EXPAND)));

		Assertions.assertThat(childItem.getValue().isActivated())
				.isFalse();
	}

	@Test
	public void expandWhenNoChildrenPresent() {
		TreeItem<ITreeItemCellView> root = computeLater(new Supplier<TreeItem<ITreeItemCellView>>() {

			@Override
			public TreeItem<ITreeItemCellView> get() {
				TreeItem<ITreeItemCellView> root = view.createRootNode();
				root.getValue().setTitle("MyRoot");
				return root;
			}
		});

		runLater(() -> root.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.TOGGLE_EXPAND)));

		Assertions.assertThat(root.getValue().isActivated())
				.isTrue();
	}

	@Test
	public void shallNotThrowExceptionWhenExpandWithNoChildren() {
		TreeItem<ITreeItemCellView> leafItem = computeLater(new Supplier<TreeItem<ITreeItemCellView>>() {

			@Override
			public TreeItem<ITreeItemCellView> get() {
				return view.createRootNode();
			}
		});

		runLater(() -> leafItem.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.TOGGLE_EXPAND)));

		Assertions.assertThat(leafItem.getValue().isActivated())
				.isTrue();
	}

	@Test
	public void shallFocusNextSibling() {
		TreeItem<ITreeItemCellView> second = computeLater(new Supplier<TreeItem<ITreeItemCellView>>() {

			@Override
			public TreeItem<ITreeItemCellView> get() {
				TreeItem<ITreeItemCellView> first = view.createRootNode();
				first.getValue().setTitle("MyRoot");
				return view.createRootNode();
			}
		});

		runLater(() -> second.previousSibling().getValue()
				.fireEvent(new TreeItemCellEvent(TreeItemCellEvent.GOTO_NEXT)));

		Assertions.assertThat(second.getValue().isActivated())
				.isTrue();
	}

	@Test
	public void shallFocusPreviousSibling() {
		TreeItem<ITreeItemCellView> secondItem = computeLater(new Supplier<TreeItem<ITreeItemCellView>>() {

			@Override
			public TreeItem<ITreeItemCellView> get() {
				TreeItem<ITreeItemCellView> first = view.createRootNode();
				first.getValue().setTitle("MyRoot");
				return view.createRootNode();
			}
		});

		runLater(() -> secondItem.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.GOTO_PREVIOUS)));

		Assertions.assertThat(
				secondItem.previousSibling().getValue().isActivated()).isTrue();
	}

	@Test
	public void shallDeleteItem() {
		TreeItem<ITreeItemCellView> rootNode = computeLater(new Supplier<TreeItem<ITreeItemCellView>>() {

			@Override
			public TreeItem<ITreeItemCellView> get() {
				return view.createRootNode();
			}
		});

		runLater(() -> rootNode.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.DELETE)));

		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(0);
	}

	@Test
	public void shallIndentItem() {
		TreeItem<ITreeItemCellView> rootNode1 = computeLater(() -> view
				.createRootNode());
		TreeItem<ITreeItemCellView> rootNode2 = computeLater(() -> view
				.createRootNode());
		runLater(() -> rootNode2.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.INDENT)));

		Assertions.assertThat(rootNode1.getChildren()).hasSize(1);
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);
	}
	
	@Test
	public void indentFirstChild() {
		TreeItem<ITreeItemCellView> rootNode1 = computeLater(() -> view
				.createRootNode());
		runLater(() -> rootNode1.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.INDENT)));

		Assertions.assertThat(rootNode1.getChildren()).hasSize(0);
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);
	}

	@Test
	public void shallMergeWithNext() {
		TreeItem<ITreeItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<ITreeItemCellView> result = view.createRootNode();
			result.getValue().setTitle("ASDF");
			return result;
		});
		TreeItem<ITreeItemCellView> rootNode2 = computeLater(() -> {
			TreeItem<ITreeItemCellView> result = view.createRootNode();
			result.getValue().setTitle("JKLÖ");
			return result;
		});
		runLater(() -> rootNode1.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MERGEWITH_NEXT)));

		Assertions.assertThat(rootNode1.getChildren()).hasSize(0);
		Assertions.assertThat(rootNode1.getValue().getTitle())
				.isEqualTo("ASDFJKLÖ");
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);
	}

	@Test
	public void mergeWithNextWhenNoNext() {
		TreeItem<ITreeItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<ITreeItemCellView> result = view.createRootNode();
			result.getValue().setTitle("ASDF");
			return result;
		});
		runLater(() -> rootNode1.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MERGEWITH_NEXT)));

		Assertions.assertThat(rootNode1.getChildren()).hasSize(0);
		Assertions.assertThat(rootNode1.getValue().getTitle())
				.isEqualTo("ASDF");
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);
	}

	@Test
	public void shallMergeWithPrev() {
		TreeItem<ITreeItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<ITreeItemCellView> result = view.createRootNode();
			result.getValue().setTitle("ASDF");
			return result;
		});
		TreeItem<ITreeItemCellView> rootNode2 = computeLater(() -> {
			TreeItem<ITreeItemCellView> result = view.createRootNode();
			result.getValue().setTitle("JKLÖ");
			return result;
		});
		runLater(() -> rootNode2.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MERGEWITH_PREVIOUS)));

		Assertions.assertThat(rootNode1.getChildren()).hasSize(0);
		Assertions.assertThat(rootNode1.getValue().getTitle())
				.isEqualTo("ASDFJKLÖ");
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);
	}

	@Test
	public void mergeWithPrevWhenNoPrev() {
		TreeItem<ITreeItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<ITreeItemCellView> result = view.createRootNode();
			result.getValue().setTitle("ASDF");
			return result;
		});
		runLater(() -> rootNode1.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MERGEWITH_PREVIOUS)));

		Assertions.assertThat(rootNode1.getChildren()).hasSize(0);
		Assertions.assertThat(rootNode1.getValue().getTitle())
				.isEqualTo("ASDF");
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);
	}

	@Test
	public void moveDown() {
		TreeItem<ITreeItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<ITreeItemCellView> result = view.createRootNode();
			result.getValue().setTitle("ASDF");
			return result;
		});
		TreeItem<ITreeItemCellView> rootNode2 = computeLater(() -> {
			TreeItem<ITreeItemCellView> result = view.createRootNode();
			result.getValue().setTitle("jklö");
			return result;
		});
		runLater(() -> rootNode1.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MOVE_DOWN)));

		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(2)
				.containsExactly(rootNode2, rootNode1);
	}

	@Test
	public void moveLastElementDown() {
		TreeItem<ITreeItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<ITreeItemCellView> result = view.createRootNode();
			result.getValue().setTitle("ASDF");
			return result;
		});

		runLater(() -> rootNode1.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MOVE_DOWN)));

		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1)
				.containsExactly(rootNode1);
	}

	@Test
	public void moveUp() {
		TreeItem<ITreeItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<ITreeItemCellView> result = view.createRootNode();
			result.getValue().setTitle("ASDF");
			return result;
		});
		TreeItem<ITreeItemCellView> rootNode2 = computeLater(() -> {
			TreeItem<ITreeItemCellView> result = view.createRootNode();
			result.getValue().setTitle("jklö");
			return result;
		});
		runLater(() -> rootNode2.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MOVE_UP)));

		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(2)
				.containsExactly(rootNode2, rootNode1);
	}

	@Test
	public void moveFirstItemUp() {
		TreeItem<ITreeItemCellView> rootNode1 = computeLater(() -> {
			TreeItem<ITreeItemCellView> result = view.createRootNode();
			result.getValue().setTitle("ASDF");
			return result;
		});

		runLater(() -> rootNode1.getValue().fireEvent(
				new TreeItemCellEvent(TreeItemCellEvent.MOVE_UP)));

		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1)
				.containsExactly(rootNode1);
	}

	@Test
	public void shallOutdentItem() {
		TreeItem<ITreeItemCellView> rootNode1 = computeLater(() -> view
				.createRootNode());
		TreeItem<ITreeItemCellView> rootNode2 = computeLater(() -> view
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
/*TODO: reactivate this test
	@Test
	public void activateOtherCell() {
		TreeItem<ITreeItemCellView> rootNode1 = computeLater(() -> view
				.createRootNode());
		TreeItem<ITreeItemCellView> rootNode2 = computeLater(() -> view
				.createRootNode());

		click(rootNode1.getValue().getTxtTitle(), MouseButton.PRIMARY).type(
				KeyCode.SHIFT, KeyCode.ENTER);

		Assertions.assertThat(rootNode1.getValue().getDetailPane().isVisible())
				.isTrue();
		Assertions.assertThat(rootNode2.getValue().getDetailPane().isVisible())
				.isFalse();

		click(rootNode2.getValue().getTxtTitle(), MouseButton.PRIMARY);
		Assertions.assertThat(rootNode1.getValue().getDetailPane().isVisible())
				.isFalse();
		Assertions.assertThat(rootNode2.getValue().getDetailPane().isVisible())
				.isFalse();
	}
	*/
	
	@Test
	public void activePropertyBinding() {
		TreeItem<ITreeItemCellView> rootNode = computeLater(() -> view
				.createRootNode());
		
		runLater(() -> rootNode.getValue().setActivated(true));
		
		Assertions.assertThat(view.getActiveCell()).isEqualTo(rootNode.getValue());
		
		runLater(() -> rootNode.getValue().setActivated(false));
		
		Assertions.assertThat(view.getActiveCell()).isNull();
	}
	
}
