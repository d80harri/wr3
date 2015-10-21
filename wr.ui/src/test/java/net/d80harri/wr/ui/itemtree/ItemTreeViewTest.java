package net.d80harri.wr.ui.itemtree;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;

import javax.inject.Inject;

import net.d80harri.wr.ui.core.ServiceProxy;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.loadui.testfx.GuiTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/net/d80harri/wr/ui/test-application-context.xml" })
public class ItemTreeViewTest extends GuiTest {
	private ItemTreeView treeView;

	@Inject
	private ServiceProxy serviceProxy;

	@Override
	protected Parent getRootNode() {
		this.treeView = new ItemTreeView();
		return treeView.getView();
	}

	@Test
	public void dataInitializedOnStartup() {
		ItemModel rootNode = new ItemModel();

		when(serviceProxy.getRootItems()).thenReturn(Arrays.asList(rootNode));

		treeView.getPresenter().reload();
		Optional<TreeItem<ItemModel>> root = treeView.getPresenter().getModel();
		assertThat(root).isNotNull();
	}

	@Test
	public void addElementsToTree() {
		ItemModel rootNode = new ItemModel(i -> i.setId(1));
		ItemModel childNode = new ItemModel();

		when(serviceProxy.getRootItems()).thenReturn(Arrays.asList(rootNode));
		when(serviceProxy.getChildItemsOf(rootNode.getId())).thenReturn(
				Arrays.asList(childNode));

		click("root node text box").type(KeyCode.ENTER).type("My new Item");
		Node newNode = find("newly created node");
		// assert new node has text
		// assert new node is child of

		click("child node text box").type(KeyCode.ENTER).type(
				"My new second Item");
		newNode = find("newly created node");
		// assert new node has text
		// assert new node is child of

		/*
		 * Remove Scope annotation from ItemTreeCellPresenter (make it
		 * singleton) to provoke an error
		 */
		Assertions.fail("Not yet fully implemented");
	}

}
