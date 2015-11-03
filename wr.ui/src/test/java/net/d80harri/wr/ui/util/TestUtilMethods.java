package net.d80harri.wr.ui.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;

import javafx.application.Platform;

public class TestUtilMethods {

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
