package net.d80harri.wr.ui.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;

import net.d80harri.wr.ui.core.IPresenter;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javafx.application.Platform;

public class TestUtilMethods {
	
	public static <T> T createDefaultMock(final MockContext<T> context) {
		T result = Mockito.mock(context.getType(), new Answer<Object>() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Answer<?> answer = context.getAnswer(invocation.getMethod());
				if (answer == null) {
					throw new NoSuchMethodError("Method " + invocation + " not mocked");
				}
				return answer.answer(invocation);
			}
		});
		return result;
	}
	
	public static <T> T computeLater(final Supplier<T> supplier) {
		final FutureTask<T> query = new FutureTask<>(new Callable<T>() {
			@Override
			public T call() throws Exception {
				return supplier.get();
			}
		});

		Platform.runLater(query);

		try {
			return query.get();
		} catch (InterruptedException | ExecutionException e) {
			throw new Error(e.getMessage(), e);
		}
	}

	public static void runLater(final Runnable callback) {
		computeLater(() -> {
			callback.run();
			return null;
		});
	}
}
