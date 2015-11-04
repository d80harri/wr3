package net.d80harri.wr.ui.itemtree;

import net.d80harri.wr.ui.core.PresenterBase;
import net.d80harri.wr.ui.core.ViewBase;

import org.junit.Test;

public class ViewBaseTest {
	
	public class PresenterForViewWithoutFxml extends PresenterBase<Void, ViewWithoutFxml>{

		public PresenterForViewWithoutFxml() {
			super(null);
		}

	}
	public static class ViewWithoutFxml extends ViewBase<PresenterForViewWithoutFxml, ViewWithoutFxml> {

	}
	@Test(expected=ViewBase.FxmlDoesNotExistException.class)
	public void shouldThrowExceptionIfFxmlDoesNotExist() {
		new ViewWithoutFxml();
	}
}
