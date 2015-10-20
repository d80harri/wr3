package net.d80harri.wr.ui.itemtree;

import java.io.Serializable;
import java.util.Arrays;

import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.objenesis.ObjenesisHelper;

public class ThrowsUnmockedException implements Answer<Object>, Serializable {

	private static final long serialVersionUID = 1128820328555183980L;
	private final ConditionalStackTraceFilter filter = new ConditionalStackTraceFilter();

	public Object answer(InvocationOnMock invocation) throws Throwable {
		Throwable throwable = new RuntimeException("Not mocked "
				+ invocation.getMethod() + "("
				+ Arrays.toString(invocation.getArguments()) + ")");
		// Throwable throwable = new RuntimeException();
		throwable.fillInStackTrace();
		filter.filter(throwable);
		throw throwable;
	}

}
