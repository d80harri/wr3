package net.d80harri.wr.ui.itemtree2;

import java.util.function.Supplier;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.util.Callback;
import net.d80harri.wr.ui.itemtree2.TreeItemCellView.NewItemRequestedEvent;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.loadui.testfx.GuiTest;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.answers.ThrowsException;
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
	public void createdRootNodeShouldBeSelected() throws InterruptedException {
		TreeItemCellView cellView = computeLater(() -> view.createRootNode());
		
		Assertions.assertThat(cellView.getTxtTitle().isFocused()).isTrue();
	}
	
	@Test
	public void shallAddNodeWhenCellRequestsNextSibling() {
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(0);
		TreeItemCellView newCell = computeLater(() -> view.createRootNode());
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(1);
		
		runLater(() -> newCell.fireEvent(new NewItemRequestedEvent(NewItemRequestedEvent.NEXT)));
		Assertions.assertThat(view.getRootNode().getChildren()).hasSize(2);
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
		return (T)cell[0];
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
