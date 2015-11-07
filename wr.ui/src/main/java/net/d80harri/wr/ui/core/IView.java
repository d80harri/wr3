package net.d80harri.wr.ui.core;

import javafx.event.Event;

public interface IView<P extends IPresenter<ME, P>, ME extends IView<P, ME>> {

	public abstract P getPresenter();
	public void fireEvent(Event event);
}
