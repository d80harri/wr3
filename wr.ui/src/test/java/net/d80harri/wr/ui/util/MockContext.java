package net.d80harri.wr.ui.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.mockito.stubbing.Answer;

public abstract class MockContext<T> {
	private Class<T> type;
	protected Map<Method, Answer<?>> answers = new HashMap<Method, Answer<?>>();
	
	public MockContext(Class<T> type) {
		this.type = type;
		try {
			installAnswers(type);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Class<T> getType() {
		return type;
	}
	
	public Answer<?> getAnswer(Method method) {
		return answers.get(method);
	}

	protected abstract void installAnswers(Class<T> type) throws NoSuchMethodException, SecurityException;
}
