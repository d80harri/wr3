package net.d80harri.wr.ui.itemtree.cell;

import net.d80harri.wr.ui.core.IPresenter;

public interface ITreeItemCellPresenter extends IPresenter<ITreeItemCellView, ITreeItemCellPresenter>{

	void saveOrUpdate();

}
