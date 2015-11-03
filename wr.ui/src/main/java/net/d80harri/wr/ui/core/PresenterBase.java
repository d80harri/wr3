package net.d80harri.wr.ui.core;

public class PresenterBase<T, V extends IView> implements IPresenter<T, V> {
	private final T model;
	private final V view;
	
	public PresenterBase(T model, V view) {
		this.model = model;
		this.view = view;
		view.setPresenter(this);
	}
	
	public T getModel() {
		return model;
	}
	
	public V getView() {
		return view;
	}
	
}
