package net.d80harri.wr.ui.itemtree2;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import net.d80harri.wr.ui.itemtree2.TreeItemCellView.NewItemRequestedEvent;

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
		EventHandler<NewItemRequestedEvent> mockedEvent = Mockito.mock(EventHandler.class);
		view.addEventHandler(NewItemRequestedEvent.NEXT, mockedEvent);
		click(view.getTxtTitle(), MouseButton.PRIMARY).type("Some text").type(KeyCode.ENTER);
		
		ArgumentCaptor<NewItemRequestedEvent> argument = ArgumentCaptor.forClass(NewItemRequestedEvent.class);
		Mockito.verify(mockedEvent, Mockito.times(1)).handle(argument.capture());
		Assertions.assertThat(argument.getValue().getEventType()).isSameAs(NewItemRequestedEvent.NEXT);
	}
}
