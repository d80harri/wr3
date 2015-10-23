package net.d80harri.wr.ui.itemtree2;

import java.util.function.Supplier;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import net.d80harri.wr.ui.itemtree2.TreeItemCellView.TreeItemCellEvent;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.loadui.testfx.GuiTest;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/net/d80harri/wr/ui/test-application-context.xml" })
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
		TreeItem<TreeItemCellView> newCell = computeLater(() -> view.createRootNode());

		Assertions.assertThat(view.getItemTree().getRoot().getChildren())
					.hasSize(1)
					.contains(newCell);
	}

	@Test
	public void createdRootNodeShouldBeSelected() throws InterruptedException {
		TreeItem<TreeItemCellView> cellView = computeLater(() -> view.createRootNode());

		Assertions.assertThat(cellView.getValue().getTxtTitle().isFocused()).isTrue();
	}

	@Test
	public void shallAddNodeWhenCellRequestsNextSibling() {
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(0);
		TreeItem<TreeItemCellView> newCell = computeLater(() -> view.createRootNode());
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);

		runLater(() -> newCell.getValue().fireEvent(new TreeItemCellEvent(
				TreeItemCellEvent.CREATE_AFTER)));
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(2);
	}
	
	@Test
	public void shallAddNodeWhenCellRequestsChild() {
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(0);
		TreeItem<TreeItemCellView> newCell = computeLater(() -> view.createRootNode());
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);

		runLater(() -> newCell.getValue().fireEvent(new TreeItemCellEvent(
				TreeItemCellEvent.CREATE_CHILD)));
		Assertions.assertThat(newCell.getChildren()).hasSize(2);
	}
	
	@Test
	public void shallFocusFirstChildWhenCellRequestsExpand() {
		Assertions.fail("NYI");
	}
	
	@Test
	public void shallFocusNextSibling() {
		Assertions.fail("NYI");
	}
	
	@Test
	public void shallFocusParent() {
		Assertions.fail("NYI");
	}
	
	@Test
	public void shallFocusPreviousSibling() {
		Assertions.fail("NYI");
	}

	@SuppressWarnings("unchecked")
	private <T> T computeLater(final Supplier<T> supplier) {
		Object[] cell = new Object[1];
		Runnable runable = new Runnable() {

			@Override
			public void run() {
				synchronized (this) {
					cell[0] = supplier.get();
					notify();
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

	@SuppressWarnings("unchecked")
	private void runLater(final Runnable callback) {
		Runnable runable = new Runnable() {

			@Override
			public void run() {
				synchronized (this) {
					callback.run();
					notify();
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
