package net.d80harri.wr.ui.core;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.net.URL;

import net.d80harri.wr.ui.itemtree.ItemTreeView;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Harald
 *
 * @param <P>
 *            the type of the presenter
 */
public abstract class ViewBase<P extends IPresenter<ME>, ME extends IView<P>> extends AnchorPane implements IView<P> {

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

	public void setPresenter(P presenter) {
		this.presenter = presenter;
	}

	public P getPresenter() {
		if (presenter == null) {
			Class clazz = (Class)
			   ((ParameterizedType)getClass().getGenericSuperclass())
			      .getActualTypeArguments()[0];
			presenter = (P)WrUiAppContext.get().getBean(clazz, this);
		}
		return presenter;
	}
	
}