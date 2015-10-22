package net.d80harri.wr.ui.itemtree2;

import java.util.function.Supplier;

import javafx.application.Platform;
import javafx.scene.Parent;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.loadui.testfx.GuiTest;
import org.mockito.Mock;
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
	public void createdRootNodeShouldBeSelected() throws InterruptedException {
		TreeItemCellView cellView = runLater(() -> view.createRootNode());
		
		Assertions.assertThat(cellView.getTxtTitle().isFocused()).isTrue();
	}
	
	@SuppressWarnings("unchecked")
	private <T> T runLater(final Supplier<T> supplier) {
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
}
