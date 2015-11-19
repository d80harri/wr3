package net.d80harri.wr.ui.core;

import javafx.beans.property.ObjectProperty;

public interface IView<P> {

	public abstract P getPresenter();
	public abstract void setPresenter(P presenter);
	public abstract ObjectProperty<P> presenterProperty();
	
}
