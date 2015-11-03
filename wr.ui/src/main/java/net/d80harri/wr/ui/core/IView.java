package net.d80harri.wr.ui.core;

public interface IView<P> {

	public abstract P getPresenter();

	public abstract void setPresenter(P presenter);

}
