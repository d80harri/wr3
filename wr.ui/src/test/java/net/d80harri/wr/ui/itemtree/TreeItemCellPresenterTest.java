package net.d80harri.wr.ui.itemtree;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import net.d80harri.wr.service.Service;
import net.d80harri.wr.service.model.ItemDto;
import net.d80harri.wr.service.util.SpringAwareBeanMapper;
import net.d80harri.wr.ui.core.WrUiAppContext;
import net.d80harri.wr.ui.itemtree.cell.TreeItemCellPresenter;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class TreeItemCellPresenterTest {

	@Test
	public void loadChildrenTest() {
		Service mockedService = mock(Service.class);
		when(mockedService.getItemsByParentId(eq(1))).thenReturn(
				Arrays.asList(new ItemDto(), new ItemDto()));

		TreeItemCellPresenter presenter = new TreeItemCellPresenter(
				mockedService, WrUiAppContext.get().getBean(
						SpringAwareBeanMapper.class));
		presenter.setId(1);
		presenter.loadChildren();

		Assertions.assertThat(presenter.getChildren()).hasSize(2);
	}
	
	@Test
	public void childrenToParentMapping() {
		TreeItemCellPresenter parent = new TreeItemCellPresenter();
		TreeItemCellPresenter child = new TreeItemCellPresenter();
		
		parent.getChildren().add(child);
		
		Assertions.assertThat(child.getParent()).isSameAs(parent);
		
		parent.getChildren().remove(child);
		
		Assertions.assertThat(child.getParent()).isNull();
	}
	
	@Test
	public void parentToChildrenMapping() {
		TreeItemCellPresenter parent = new TreeItemCellPresenter();
		TreeItemCellPresenter child = new TreeItemCellPresenter();
		
		child.setParent(parent);
		
		Assertions.assertThat(parent.getChildren()).hasSize(1).contains(child);
		
		child.setParent(null);
		
		Assertions.assertThat(parent.getChildren()).isEmpty();
	}
	
	@Test
	public void gotoNextAndPrevious() {
		Service mockedService = mock(Service.class);
		
		TreeItemCellPresenter parent = new TreeItemCellPresenter(mockedService, null);
		TreeItemCellPresenter child1 = new TreeItemCellPresenter(mockedService, null);
		TreeItemCellPresenter child2 = new TreeItemCellPresenter(mockedService, null);
		
		child1.setParent(parent);
		child2.setParent(parent);
		
		child1.setActivated(true);
		
		Assertions.assertThat(child1.isActivated()).isTrue();
		Assertions.assertThat(child2.isActivated()).isFalse();
		
		child1.gotoPreviousSibling();
		
		Assertions.assertThat(child1.isActivated()).isTrue();
		Assertions.assertThat(child2.isActivated()).isFalse();
		
		child1.gotoNextSibling();
		
		Assertions.assertThat(child1.isActivated()).isFalse();
		Assertions.assertThat(child2.isActivated()).isTrue();
		
		child1.gotoNextSibling();
		
		Assertions.assertThat(child1.isActivated()).isFalse();
		Assertions.assertThat(child2.isActivated()).isTrue();
	}
}
