package net.d80harri.wr.ui.itemtree;

import net.d80harri.wr.ui.core.IPresenter;

public class ItemTreePresenter implements IPresenter<IItemTreeView, ItemTreePresenter> {

	private IItemTreeView view;
	
	public void setView(IItemTreeView view) {
		this.view = view;
	}

}
