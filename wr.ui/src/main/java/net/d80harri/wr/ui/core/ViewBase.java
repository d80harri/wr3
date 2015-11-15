package net.d80harri.wr.ui.core;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Harald
 *
 * @param <P>
 *            the type of the presenter
 */
public abstract class ViewBase<P extends IPresenter<ME, P>, ME extends IView<P, ME>> extends AnchorPane implements IView<P, ME> {

	public static class FxmlDoesNotExistException extends RuntimeException {
		private static final long serialVersionUID = -7287130651184888316L;

		public FxmlDoesNotExistException(String string) {
			super(string);
		}

	}

	protected P presenter;

	public ViewBase() {
		loadFxml();
	}
	
	public ViewBase(P presenter) {
		loadFxml();
		this.presenter = presenter;
		this.presenter.setView((ME)this);
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

	public P getPresenter() {
		return presenter;
	}
	
}