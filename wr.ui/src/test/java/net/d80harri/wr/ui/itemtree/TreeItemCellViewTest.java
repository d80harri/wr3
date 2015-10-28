package net.d80harri.wr.ui.itemtree;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import net.d80harri.wr.ui.itemtree.TreeItemCellView.TreeItemCellEvent;

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
		shortCutHelper(TreeItemCellEvent.MOVE_UP,
				() -> click(view.getTxtTitle(), MouseButton.PRIMARY).type(".")
						.type(KeyCode.HOME).type(KeyCode.LEFT));
	}

	@Test
	public void userWantsToNavigateToPreviousUsingUpArrow() {
		shortCutHelper(TreeItemCellEvent.MOVE_UP, () -> click(view.getTxtTitle(), MouseButton.PRIMARY).type("...").type(KeyCode.LEFT)
				.type(KeyCode.UP));
	}
	
	@Test
	public void userWantsToNavigateToNextUsingRightArrow() {
		shortCutHelper(TreeItemCellEvent.MOVE_DOWN, KeyCode.RIGHT);
	}
	
	@Test
	public void userWantsToNavigateToNextUsingDownArrow() {
		shortCutHelper(TreeItemCellEvent.MOVE_DOWN, () -> click(view.getTxtTitle(), MouseButton.PRIMARY).type("...").type(KeyCode.LEFT)
				.type(KeyCode.DOWN));
	}

	@Test
	public void userWantsToMoveTheCursorToTheRight() {
		shortCutHelper(null, () -> click(view.getTxtTitle(), MouseButton.PRIMARY).type(".").type(KeyCode.HOME)
				.type(KeyCode.RIGHT));
	}
	
	@Test
	public void userWantsToMoveTheCursorToTheLeft() {
		shortCutHelper(null, () -> click(view.getTxtTitle(), MouseButton.PRIMARY).type(".").type(KeyCode.END)
				.type(KeyCode.LEFT));
	}


	@Test
	public void userWantsToNavigateToParent() {
		shortCutHelper(TreeItemCellEvent.MOVE_TO_PARENT, KeyCode.CONTROL, KeyCode.UP);
	}

	@Test
	public void userWantsToExpandNode() {
		shortCutHelper(TreeItemCellEvent.EXPAND, KeyCode.CONTROL, KeyCode.RIGHT);
	}
	
	@Test
	public void userWantsToCollapsedNode() {
		shortCutHelper(TreeItemCellEvent.COLLAPSE, KeyCode.CONTROL, KeyCode.LEFT);
	}

	@Test
	public void userWantsToCreateAChild() {
		shortCutHelper(TreeItemCellEvent.CREATE_CHILD, KeyCode.CONTROL,
				KeyCode.ENTER);
	}

	@Test
	public void shallViewDetail() {
		Assertions.assertThat(view.getDetailPane().isVisible()).isFalse();
		click(view.getTxtTitle(), MouseButton.PRIMARY).type(KeyCode.SHIFT,
				KeyCode.ENTER);
		Assertions.assertThat(view.getDetailPane().isVisible()).isTrue();
		Assertions.assertThat(view.getDescriptionArea().getTextArea().isFocused()).isTrue();
	}

	@Test
	public void userWantsToToggleDetailView() {
		Assertions.assertThat(view.getDetailPane().isVisible()).isFalse();
		Assertions.assertThat(view.getDetailPane().isManaged()).isFalse();

		click(view.getTxtTitle(), MouseButton.PRIMARY).type(KeyCode.SHIFT,
				KeyCode.ENTER);

		Assertions.assertThat(view.getDetailPane().isVisible()).isTrue();
		Assertions.assertThat(view.getDetailPane().isManaged()).isTrue();

		click(view.getTxtTitle(), MouseButton.PRIMARY).type(KeyCode.SHIFT,
				KeyCode.ENTER);

		Assertions.assertThat(view.getDetailPane().isVisible()).isFalse();
		Assertions.assertThat(view.getDetailPane().isManaged()).isFalse();
	}

	private void shortCutHelper(EventType<TreeItemCellEvent> event,
			Runnable additionalActions) {
		EventHandler<TreeItemCellEvent> mockedEvent = Mockito
				.mock(EventHandler.class);
		view.addEventHandler(TreeItemCellView.TreeItemCellEvent.BASE, mockedEvent);
		
		if (additionalActions != null)
			additionalActions.run();
		
		if (event == null) {
			Mockito.verify(mockedEvent, Mockito.times(0)).handle(Mockito.any());
		} else {
			ArgumentCaptor<TreeItemCellEvent> argument = ArgumentCaptor
					.forClass(TreeItemCellEvent.class);
			Mockito.verify(mockedEvent, Mockito.times(1)).handle(
					argument.capture());
			Assertions.assertThat(argument.getValue().getEventType()).isSameAs(
					event);
		}
	}

	private void shortCutHelper(EventType<TreeItemCellEvent> event,
			KeyCode... code) {
		shortCutHelper(event,
				() -> click(view.getTxtTitle(), MouseButton.PRIMARY).type(".")
						.type(code));
	}
}
