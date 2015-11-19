package net.d80harri.wr.ui.core;

import java.io.IOException;
import java.net.URL;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Harald
 *
 * @param <P>
 *            the type of the presenter
 */
public abstract class ViewBase<P> extends AnchorPane implements IView<P> {

	public static class FxmlDoesNotExistException extends RuntimeException {
		private static final long serialVersionUID = -7287130651184888316L;

		public FxmlDoesNotExistException(String string) {
			super(string);
		}

	}

	protected ObjectProperty<P> presenter;

	public ViewBase() {
		loadFxml();
	}
	
	public ViewBase(P presenter) {
		loadFxml();
		setPresenter(presenter);
	}

	protected void loadFxml() {
		String location = getClass().getSimpleName() + ".fxml";
		URL locationUrl = getClass().getResource(location);
		if (locationUrl == null)
			throw new FxmlDoesNotExistException("Location " + location
					+ " not found");
		FXMLLoader fxmlLoader = new FXMLLoader(locationUrl);
		fxmlLoader.setController(this);
		fxmlLoader.setRoot(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	@Override
	public ObjectProperty<P> presenterProperty() {
		if (this.presenter == null) {
			this.presenter = new SimpleObjectProperty<>(this, "presenter");
		}
		return this.presenter;
	}
	
	public P getPresenter() {
		return presenterProperty().get();
	}
	
	@Override
	public void setPresenter(P presenter) {
		presenterProperty().set(presenter);
	}
	
}