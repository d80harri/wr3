package net.d80harri.wr.ui.itemtree;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Optional;

import javafx.scene.Parent;
import javafx.scene.control.TreeItem;

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
	
	@Test
	public void addElementsToTree() {
		/*
		 * 1. create a root nodes with one child node
		 * 2. select the root node and click enter
		 * 3. a new root node has to be created
		 * 
		 * 4. select the child node and click enter
		 * 5. a new child node has to be created
		 */
		
		/* Remove Scope annotation from ItemTreeCellPresenter (make it singleton)
		 * to provoke an error
		 */
		fail("Not yet fully implemented");
	}

}
