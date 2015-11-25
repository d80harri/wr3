package net.d80harri.wr.ui.itemtree;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import net.d80harri.wr.service.Service;
import net.d80harri.wr.service.model.ItemDto;
import net.d80harri.wr.service.util.SpringAwareBeanMapper;
import net.d80harri.wr.ui.core.WrUiAppContext;
import net.d80harri.wr.ui.itemtree.cell.ItemCellPresenter;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ItemCellPresenterTest {

	@Test
	public void loadChildrenTest() {
		Service mockedService = mock(Service.class);
		when(mockedService.getItemsByParentId(eq(1))).thenReturn(
				Arrays.asList(new ItemDto(), new ItemDto()));

		ItemCellPresenter presenter = new ItemCellPresenter(
				mockedService, WrUiAppContext.get().getBean(
						SpringAwareBeanMapper.class));
		presenter.setId(1);
		presenter.loadChildren();

		Assertions.assertThat(presenter.getChildren()).hasSize(2);
	}

	@Test
	public void childrenToParentMapping() {
		ItemCellPresenter parent = new ItemCellPresenter();
		ItemCellPresenter child = new ItemCellPresenter();

		parent.getChildren().add(child);

		Assertions.assertThat(child.getParent()).isSameAs(parent);

		parent.getChildren().remove(child);

		Assertions.assertThat(child.getParent()).isNull();
	}

	@Test
	public void addingChildToItsOwnParent() {
		ItemCellPresenter parent = new ItemCellPresenter();
		ItemCellPresenter child1 = new ItemCellPresenter();
		ItemCellPresenter child2 = new ItemCellPresenter();

		child1.setParent(parent);
		child2.setParent(parent);

		parent.getChildren().add(child1);

		Assertions.assertThat(parent.getChildren()).hasSize(3)
				.containsSequence(child1, child2, child1);
	}

	@Test
	public void parentToChildrenMapping() {
		ItemCellPresenter parent = new ItemCellPresenter();
		ItemCellPresenter child = new ItemCellPresenter();

		child.setParent(parent);

		Assertions.assertThat(parent.getChildren()).hasSize(1).contains(child);

		child.setParent(null);

		Assertions.assertThat(parent.getChildren()).isEmpty();
	}

	@Test
	public void gotoNextAndPrevious() {
		Service mockedService = mock(Service.class);

		ItemCellPresenter parent = new ItemCellPresenter(mockedService,
				null);
		ItemCellPresenter child1 = new ItemCellPresenter(mockedService,
				null);
		ItemCellPresenter child2 = new ItemCellPresenter(mockedService,
				null);

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

	@Test
	public void mergeWithNext() {
		Service mockedService = mock(Service.class);

		ItemCellPresenter parent = new ItemCellPresenter(mockedService,
				null);
		ItemCellPresenter child1 = new ItemCellPresenter(mockedService,
				null);
		ItemCellPresenter child2 = new ItemCellPresenter(mockedService,
				null);

		child1.setTitle("1");
		child1.setParent(parent);
		child2.setTitle("2");
		child2.setParent(parent);

		child1.mergeNextInto();

		Assertions.assertThat(parent.getChildren()).hasSize(1);
		Assertions.assertThat(child2.getParent()).isNull();
		Assertions.assertThat(child1.getTitle()).isEqualTo("12");
	}

	@Test
	public void mergeWithPrev() {
		Service mockedService = mock(Service.class);

		ItemCellPresenter parent = new ItemCellPresenter(mockedService,
				null);
		ItemCellPresenter child1 = new ItemCellPresenter(mockedService,
				null);
		ItemCellPresenter child2 = new ItemCellPresenter(mockedService,
				null);

		child1.setTitle("1");
		child1.setParent(parent);
		child2.setTitle("2");
		child2.setParent(parent);

		child2.mergePreviousInto();

		Assertions.assertThat(parent.getChildren()).hasSize(1);
		Assertions.assertThat(child2.getParent()).isNull();
		Assertions.assertThat(child1.getTitle()).isEqualTo("12");
	}

	@Test
	public void switchWithNext() {
		Service mockedService = mock(Service.class);

		ItemCellPresenter parent = new ItemCellPresenter(mockedService,
				null);
		ItemCellPresenter child1 = new ItemCellPresenter(mockedService,
				null);
		ItemCellPresenter child2 = new ItemCellPresenter(mockedService,
				null);

		child1.setTitle("1");
		child1.setParent(parent);
		child2.setTitle("2");
		child2.setParent(parent);

		child1.switchWithNext();

		Assertions.assertThat(parent.getChildren()).hasSize(2)
				.containsSequence(child2, child1);
		Assertions.assertThat(child1.getParent()).isSameAs(parent);
		Assertions.assertThat(child1.getChildIndex()).isEqualTo(1);
		Assertions.assertThat(child2.getParent()).isSameAs(parent);
		Assertions.assertThat(child2.getChildIndex()).isEqualTo(0);
	}

	@Test
	public void switchWithPrevious() {
		Service mockedService = mock(Service.class);

		ItemCellPresenter parent = new ItemCellPresenter(mockedService,
				null);
		ItemCellPresenter child1 = new ItemCellPresenter(mockedService,
				null);
		ItemCellPresenter child2 = new ItemCellPresenter(mockedService,
				null);

		child1.setParent(parent);
		child2.setParent(parent);

		child2.switchWithPrev();

		Assertions.assertThat(parent.getChildren()).hasSize(2)
				.containsSequence(child2, child1);
		Assertions.assertThat(child1.getParent()).isSameAs(parent);
		Assertions.assertThat(child1.getChildIndex()).isEqualTo(1);
		Assertions.assertThat(child2.getParent()).isSameAs(parent);
		Assertions.assertThat(child2.getChildIndex()).isEqualTo(0);
	}

	@Test
	public void indent() {
		Service mockedService = mock(Service.class);

		ItemCellPresenter parent = new ItemCellPresenter(mockedService,
				null);
		ItemCellPresenter child1 = new ItemCellPresenter(mockedService,
				null);
		ItemCellPresenter child2 = new ItemCellPresenter(mockedService,
				null);

		child1.setParent(parent);
		child2.setParent(parent);

		child2.indent();

		Assertions.assertThat(parent.getChildren()).hasSize(1)
				.containsSequence(child1);

		Assertions.assertThat(child1.getParent()).isSameAs(parent);
		Assertions.assertThat(child1.getChildren()).hasSize(1)
				.containsSequence(child2);
		Assertions.assertThat(child1.getChildIndex()).isEqualTo(0);

		Assertions.assertThat(child2.getParent()).isSameAs(child1);
		Assertions.assertThat(child2.getChildren()).isEmpty();
		Assertions.assertThat(child2.getChildIndex()).isEqualTo(0);
	}

	@Test
	public void outdent() {
		Service mockedService = mock(Service.class);

		ItemCellPresenter parent = new ItemCellPresenter(mockedService,
				null);
		ItemCellPresenter child1 = new ItemCellPresenter(mockedService,
				null);
		ItemCellPresenter child2 = new ItemCellPresenter(mockedService,
				null);
		ItemCellPresenter child3 = new ItemCellPresenter(mockedService,
				null);

		child1.setParent(parent);
		child2.setParent(child1);
		child3.setParent(parent);

		child2.outdent();

		Assertions.assertThat(parent.getChildren()).hasSize(3)
				.containsSequence(child1, child2, child3);

		Assertions.assertThat(child1.getParent()).isSameAs(parent);
		Assertions.assertThat(child1.getChildren()).isEmpty();
		Assertions.assertThat(child1.getChildIndex()).isEqualTo(0);

		Assertions.assertThat(child2.getParent()).isSameAs(parent);
		Assertions.assertThat(child2.getChildren()).isEmpty();
		Assertions.assertThat(child2.getChildIndex()).isEqualTo(1);

		Assertions.assertThat(child3.getParent()).isSameAs(parent);
		Assertions.assertThat(child3.getChildren()).isEmpty();
		Assertions.assertThat(child3.getChildIndex()).isEqualTo(2);
	}

	@Test
	public void splitItem() {
		Service mockedService = mock(Service.class);

		ItemCellPresenter parent = new ItemCellPresenter(mockedService,
				null);
		ItemCellPresenter child1 = new ItemCellPresenter(mockedService,
				null);
		ItemCellPresenter child2;

		child1.setParent(parent);
		child1.setTitle("1234");
		child1.setTitleCaretPosition(2);
		child2 = child1.splitItem();

		Assertions.assertThat(parent.getChildren()).hasSize(2).contains(child1, child2);

		Assertions.assertThat(child1.getParent()).isSameAs(parent);
		Assertions.assertThat(child1.getChildren()).isEmpty();
		Assertions.assertThat(child1.getChildIndex()).isEqualTo(0);
		Assertions.assertThat(child1.getTitle()).isEqualTo("12");

		Assertions.assertThat(child2.getParent()).isSameAs(parent);
		Assertions.assertThat(child2.getChildren()).isEmpty();
		Assertions.assertThat(child2.getChildIndex()).isEqualTo(1);
		Assertions.assertThat(child2.getTitle()).isEqualTo("34");
	}
	
	@Test
	public void toggleExpand() {
		Service mockedService = mock(Service.class);

		ItemCellPresenter parent = new ItemCellPresenter(mockedService,
				null);
		ItemCellPresenter child1 = new ItemCellPresenter(mockedService,
				null);

		child1.setParent(parent);
		
		parent.toggleExpand();
		
		Assertions.assertThat(parent.getChildren()).hasSize(1).contains(child1);
		Assertions.assertThat(parent.isExpanded()).isTrue();
		
		Assertions.assertThat(child1.getParent()).isSameAs(parent);
		Assertions.assertThat(child1.getChildren()).isEmpty();
		Assertions.assertThat(child1.getChildIndex()).isEqualTo(0);
		Assertions.assertThat(child1.isExpanded()).isFalse();
		Assertions.assertThat(child1.isActivated()).isTrue();
	}
}
