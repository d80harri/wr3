package net.d80harri.wr.ui.core;

import net.d80harri.wr.ui.util.MockContext;

public abstract class MockContextIView<T extends IView> extends MockContext<T> {

	public MockContextIView(Class<T> type) {
		super(type);
	}

	@Override
	protected void installAnswers(Class<T> type) throws NoSuchMethodException,
			SecurityException {
		
	}

}
