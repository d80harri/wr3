package net.d80harri.wr.ui.itemtree2;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import net.d80harri.wr.ui.itemtree2.TreeItemCellView.TreeItemCellEvent;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.loadui.testfx.GuiTest;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/net/d80harri/wr/ui/test-application-context.xml" })
public class TreeItemCellViewTest extends GuiTest {
	private TreeItemCellView view;

	@Mock
	private TreeItemCellPresenter presenter;

	@Override
	protected Parent getRootNode() {
		MockitoAnnotations.initMocks(this);
		view = new TreeItemCellView();
		view.setPresenter(presenter);

		return view;
	}

	@Test
	public void userWantsToAddASiblingItemUsingTheKeyboard() {
		shortCutHelper(TreeItemCellEvent.CREATE_AFTER, KeyCode.ENTER);
	}

	@Test
	public void userWantsToNavigateToPrevious() {
		shortCutHelper(TreeItemCellEvent.MOVE_UP, KeyCode.UP);
	}

	@Test
	public void userWantsToNavigateToNext() {
		shortCutHelper(TreeItemCellEvent.MOVE_DOWN, KeyCode.DOWN);
	}

	@Test
	public void userWantsToNavigateToParent() {
		shortCutHelper(TreeItemCellEvent.MOVE_TO_PARENT, KeyCode.LEFT);
	}

	@Test
	public void userWantsToExpandNode() {
		shortCutHelper(TreeItemCellEvent.EXPAND, KeyCode.RIGHT);
	}

	@Test
	public void userWantsToCreateAChild() {
		shortCutHelper(TreeItemCellEvent.CREATE_CHILD, KeyCode.CONTROL,
				KeyCode.ENTER);
	}

	@Test
	public void userWantsToToggleDetailView() {
		Assertions.assertThat(view.isDetailVisible()).isFalse();
		click(view.getTxtTitle(), MouseButton.PRIMARY).type(KeyCode.SHIFT,
				KeyCode.ENTER);
		Assertions.assertThat(view.isDetailVisible()).isTrue();
		click(view.getTxtTitle(), MouseButton.PRIMARY).type(KeyCode.SHIFT,
				KeyCode.ENTER);
		Assertions.assertThat(view.isDetailVisible()).isFalse();
	}

	private void shortCutHelper(EventType<TreeItemCellEvent> event,
			KeyCode... code) {
		EventHandler<TreeItemCellEvent> mockedEvent = Mockito
				.mock(EventHandler.class);
		view.addEventHandler(event, mockedEvent);
		click(view.getTxtTitle(), MouseButton.PRIMARY).type(".").type(code);

		ArgumentCaptor<TreeItemCellEvent> argument = ArgumentCaptor
				.forClass(TreeItemCellEvent.class);
		Mockito.verify(mockedEvent, Mockito.times(1))
				.handle(argument.capture());
		Assertions.assertThat(argument.getValue().getEventType()).isSameAs(
				event);
	}
}
