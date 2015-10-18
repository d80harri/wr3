package net.d80harri.wr.ui.itemtree;

import java.util.Optional;

import javafx.scene.Parent;
import javafx.scene.control.TreeItem;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.loadui.testfx.GuiTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/net/d80harri/wr/ui/test-application-context.xml" })
public class ItemTreeViewTest extends GuiTest {
	private ItemTreeView treeView;

	@Override
	protected Parent getRootNode() {
		this.treeView = new ItemTreeView();
		return treeView.getView();
	}

	@Test
	public void dataInitializedOnStartup() {
		Optional<TreeItem<ItemModel>> root = treeView.getPresenter().getModel();
		assertThat(root).isNotNull();
		
		fail("Not yet fully implemented");
	}

}
