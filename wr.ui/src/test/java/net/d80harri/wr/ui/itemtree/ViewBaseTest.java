package net.d80harri.wr.ui.itemtree;

import org.junit.Test;

public class ViewBaseTest {
	
	public class PresenterForViewWithoutFxml {

	}
	public static class ViewWithoutFxml extends ViewBase<PresenterForViewWithoutFxml> {

	}
	@Test(expected=ViewBase.FxmlDoesNotExistException.class)
	public void shouldThrowExceptionIfFxmlDoesNotExist() {
		new ViewWithoutFxml();
	}
}
