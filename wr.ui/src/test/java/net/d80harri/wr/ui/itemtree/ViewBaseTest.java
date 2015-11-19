package net.d80harri.wr.ui.itemtree;

import net.d80harri.wr.ui.core.ViewBase;

import org.junit.Test;

public class ViewBaseTest {
	
	public class PresenterForViewWithoutFxml {

	}
	public static class ViewWithoutFxml extends ViewBase<PresenterForViewWithoutFxml> {

		public ViewWithoutFxml(PresenterForViewWithoutFxml presenter) {
			super(presenter);
		}

	}
	@Test(expected=ViewBase.FxmlDoesNotExistException.class)
	public void shouldThrowExceptionIfFxmlDoesNotExist() {
		new ViewWithoutFxml(new PresenterForViewWithoutFxml());
	}
}
