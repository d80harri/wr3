package net.d80harri.wr.ui.itemtree;

import javafx.scene.Parent;
import javafx.scene.control.TreeView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.loadui.testfx.GuiTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/net/d80harri/wr/ui/test-application-context.xml" })
public class ItemTreeViewUiTest extends GuiTest {
	@Mock
	private ItemTreePresenterLogic logic;
	
	@Override
	protected Parent getRootNode() {
		MockitoAnnotations.initMocks(this);
		ItemTreeView treeView = new ItemTreeView();
		treeView.getPresenter().setLogic(logic);
		return treeView.getView();
	}

	
	@Test
	public void aNewNodeShouldAppearWhenEnterIsHitInTextBox() {
		System.out.println();
	}
}
