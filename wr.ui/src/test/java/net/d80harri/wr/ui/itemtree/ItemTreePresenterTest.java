package net.d80harri.wr.ui.itemtree;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import net.d80harri.wr.ui.itemtree.cell.TreeItemCellPresenter;

public class ItemTreePresenterTest {

	@Test
	public void addRootNode() {
		ItemTreePresenter tree = new ItemTreePresenter(null, null);
		TreeItemCellPresenter root1 = new TreeItemCellPresenter(null, null);
		
		tree.addRootNode(root1);
		
		Assertions.assertThat(tree.getRootItems()).hasSize(1).contains(root1);
		Assertions.assertThat(tree.getActiveItem()).isEqualTo(root1);
		Assertions.assertThat(root1.isActivated()).isTrue();
	}
	
	@Test
	public void addNodeAfterActive() {
		ItemTreePresenter tree = new ItemTreePresenter(null, null);
		TreeItemCellPresenter root1 = new TreeItemCellPresenter(null, null);
		TreeItemCellPresenter root2 = new TreeItemCellPresenter(null, null);
		
		tree.addRootNode(root1);
		tree.addNodeAfterActive(root2);
		
		Assertions.assertThat(tree.getRootItems()).hasSize(2).containsSequence(root1, root2);
		Assertions.assertThat(tree.getActiveItem()).isEqualTo(root2);
		Assertions.assertThat(root1.isActivated()).isFalse();
		Assertions.assertThat(root2.isActivated()).isTrue();
	}
	
	@Test
	public void setActiveItem() {
		ItemTreePresenter tree = new ItemTreePresenter(null, null);
		TreeItemCellPresenter root1 = new TreeItemCellPresenter(null, null);
		TreeItemCellPresenter root2 = new TreeItemCellPresenter(null, null);
		
		tree.addRootNode(root1);
		tree.addNodeAfterActive(root2);
		
		tree.setActiveItem(root1);
		
		Assertions.assertThat(tree.getRootItems()).hasSize(2).containsSequence(root1, root2);
		Assertions.assertThat(tree.getActiveItem()).isEqualTo(root1);
		Assertions.assertThat(root1.isActivated()).isTrue();
		Assertions.assertThat(root2.isActivated()).isFalse();
	}
	
	@Test
	public void setActiveItemNull() {
		ItemTreePresenter tree = new ItemTreePresenter(null, null);
		TreeItemCellPresenter root1 = new TreeItemCellPresenter(null, null);
		
		tree.addRootNode(root1);
		tree.setActiveItem(null);
		
		Assertions.assertThat(tree.getRootItems()).hasSize(1).contains(root1);
		Assertions.assertThat(tree.getActiveItem()).isNull();
		Assertions.assertThat(root1.isActivated()).isFalse();
	}
	
	@Test
	public void setActiveToItemThatDoesNotExist() {
		ItemTreePresenter tree = new ItemTreePresenter(null, null);
		TreeItemCellPresenter root1 = new TreeItemCellPresenter(null, null);
		TreeItemCellPresenter notExisting = new TreeItemCellPresenter(null, null);
		
		tree.addRootNode(root1);
		tree.setActiveItem(notExisting);
		
		Assertions.assertThat(tree.getRootItems()).hasSize(1).contains(root1);
		Assertions.assertThat(tree.getActiveItem()).isEqualTo(notExisting);
		Assertions.assertThat(root1.isActivated()).isFalse();
		Assertions.assertThat(notExisting.isActivated()).isTrue();
	}
}
