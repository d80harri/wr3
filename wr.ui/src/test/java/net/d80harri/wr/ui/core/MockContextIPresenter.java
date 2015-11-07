package net.d80harri.wr.ui.core;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import net.d80harri.wr.ui.core.IPresenter;
import net.d80harri.wr.ui.core.IView;
import net.d80harri.wr.ui.util.MockContext;

public abstract class MockContextIPresenter<T extends IPresenter> extends MockContext<T>{

	public MockContextIPresenter(Class<T> type) {
		super(type);
	}
	
	@Override
	protected void installAnswers(Class<T> type) throws NoSuchMethodException, SecurityException {
		answers.put(type.getMethod("setView", IView.class), new Answer<Object>() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return invocation.callRealMethod();
			}
		});
	}

}
