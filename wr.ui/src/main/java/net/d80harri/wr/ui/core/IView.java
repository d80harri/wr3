package net.d80harri.wr.ui.core;

public interface IView<P extends IPresenter<ME, P>, ME extends IView<P, ME>> {

	public abstract P getPresenter();

}
