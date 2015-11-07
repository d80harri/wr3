package net.d80harri.wr.ui.core;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class PresenterBase<T, V extends IView<ME, V>, ME extends IPresenter<V, ME>> implements IPresenter<V, ME> {
	private ObjectProperty<T> model;
	private V view;
	
	public PresenterBase(T model) {
		setModel(model);
	}
	
	public PresenterBase(T model, V view) {
		setModel(model);
		this.view = view;
	}
	
	public ObjectProperty<T> modelProperty() {
		if (model == null) {
			model = new SimpleObjectProperty<>();
		}
		return model;
	}
	
	public T getModel() {
		return modelProperty().get();
	}
	
	public void setModel(T value) {
		modelProperty().set(value);
	}
	
	public V getView() {
		return view;
	}
	
	public void setView(V view) {
		this.view = view;
	}
	
}
