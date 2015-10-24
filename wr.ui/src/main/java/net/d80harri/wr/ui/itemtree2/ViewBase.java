package net.d80harri.wr.ui.itemtree2;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * 
 * @author Harald
 *
 * @param <P> the type of the presenter 
 */
public abstract class ViewBase<P> extends AnchorPane {

	public static class FxmlDoesNotExistException extends RuntimeException {
		private static final long serialVersionUID = -7287130651184888316L;

		public FxmlDoesNotExistException(String string) {
			super(string);
		}
		
	}
	
	protected P presenter;

	public ViewBase() {
		loadFxml();
		registerHandlers();
	}

	protected abstract void registerHandlers();

	protected void loadFxml() {
		String location = getClass().getSimpleName() + ".fxml";
		URL locationUrl = getClass().getResource(location);
		if (locationUrl == null)
			throw new FxmlDoesNotExistException("Location " + location + " not found");
		FXMLLoader fxmlLoader = new FXMLLoader(locationUrl);
		fxmlLoader.setController(this);
		fxmlLoader.setRoot(this);
	
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public void setPresenter(P presenter) {
		this.presenter = presenter;
	}

}