package net.d80harri.wr.ui.itemtree2;

public class ItemTreePresenter {

	private IItemTreeView view;
	
	public void setView(IItemTreeView view) {
		this.view = view;
	}

	public TreeItemCellView createRootNode() {
		return view.createRootNode();
	}

}
