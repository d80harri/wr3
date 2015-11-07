package net.d80harri.wr.ui.core;

public interface IPresenter<V extends IView<ME, V>, ME extends IPresenter<V, ME>> {
	public void setView(V view);
}
