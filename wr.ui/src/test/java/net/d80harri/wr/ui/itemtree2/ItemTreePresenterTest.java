package net.d80harri.wr.ui.itemtree2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/net/d80harri/wr/ui/test-application-context.xml" })
public class ItemTreePresenterTest {
	@Mock
	private ItemTreeView view;

	private ItemTreePresenter presenter;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
		presenter = new ItemTreePresenter();
		presenter.setView(view);
	}
	
	@Test
	public void shallCreateNewRootNode() {
		presenter.createRootNode();
		
		Mockito.verify(view).createRootNode();
	}
}