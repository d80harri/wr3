package net.d80harri.wr.ui.itemtree;

import static net.d80harri.wr.ui.util.TestUtilMethods.runLater;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import net.d80harri.wr.ui.itemtree.cell.TreeItemCellEvent;
import net.d80harri.wr.ui.itemtree.cell.TreeItemCellPresenter;
import net.d80harri.wr.ui.itemtree.cell.TreeItemCellView;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class TreeItemCellViewTest extends GuiTest {
	private TreeItemCellView view;

	@Mock
	private TreeItemCellPresenter presenter;

	@Override
	protected Parent getRootNode() {
		MockitoAnnotations.initMocks(this);
		view = new TreeItemCellView(presenter);

		return view;
	}

	@Test
	public void userWantsToMoveTheCursorToTheRight() {
		shortCutHelper(null,
				() -> click(view.getTxtTitle(), MouseButton.PRIMARY).type(".")
						.type(KeyCode.HOME).type(KeyCode.RIGHT));
	}

	@Test
	public void userWantsToMoveTheCursorToTheLeft() {
		shortCutHelper(null,
				() -> click(view.getTxtTitle(), MouseButton.PRIMARY).type(".")
						.type(KeyCode.END).type(KeyCode.LEFT));
	}

	@Test
	public void userWantsDeleteACharWithBackspace() {
		shortCutHelper(null,
				() -> click(view.getTxtTitle(), MouseButton.PRIMARY)
						.type("ABC").type(KeyCode.LEFT)
						.type(KeyCode.BACK_SPACE));
		Assertions.assertThat(view.getTxtTitle().getText()).isEqualTo("AC");
	}

	@Test
	public void userWantsDeleteACharWithDEL() {
		shortCutHelper(null,
				() -> click(view.getTxtTitle(), MouseButton.PRIMARY)
						.type("ABC").type(KeyCode.LEFT).type(KeyCode.LEFT)
						.type(KeyCode.DELETE));
		Assertions.assertThat(view.getTxtTitle().getText()).isEqualTo("AC");
	}

	@Test
	public void shallViewDetail() {
		Assertions.assertThat(view.getDetailPane().isVisible()).isFalse();
		click(view.getTxtTitle(), MouseButton.PRIMARY).type(KeyCode.SHIFT,
				KeyCode.ENTER);
		Assertions.assertThat(view.getDetailPane().isVisible()).isTrue();
		Assertions.assertThat(
				view.getDescriptionArea().getTextArea().isFocused()).isTrue();
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

	@Test
	public void shallHideDetailWhenInDetail() {
		runLater(() -> {
			view.setDetailVisible(true);
			view.getDescriptionArea().getTextArea().requestFocus();
		});
		type(KeyCode.SHIFT, KeyCode.ENTER);
		Assertions.assertThat(view.getDetailPane().isVisible()).isFalse();
		Assertions.assertThat(view.getTxtTitle().isFocused()).isTrue();
	}

	@Test
	public void detailVisiblePropertyBinding() {
		runLater(() -> view.setDetailVisible(true));

		Assertions.assertThat(view.isDetailVisible()).isTrue();
		Assertions.assertThat(view.getDetailPane().isVisible()).isTrue();

		runLater(() -> view.setDetailVisible(false));

		Assertions.assertThat(view.isDetailVisible()).isFalse();
		Assertions.assertThat(view.getDetailPane().isVisible()).isFalse();
	}

	private void shortCutHelper(EventType<TreeItemCellEvent> event,
			Runnable additionalActions) {
		EventHandler<TreeItemCellEvent> mockedEvent = Mockito
				.mock(EventHandler.class);
		view.addEventHandler(TreeItemCellEvent.BASE,
				mockedEvent);

		if (additionalActions != null)
			additionalActions.run();

		if (event == null) {
			Mockito.verify(mockedEvent, Mockito.never()).handle(Mockito.any());
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
