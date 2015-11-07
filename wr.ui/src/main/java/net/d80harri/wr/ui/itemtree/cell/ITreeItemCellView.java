package net.d80harri.wr.ui.itemtree.cell;

import javafx.beans.property.StringProperty;
import net.d80harri.wr.ui.core.IView;

public interface ITreeItemCellView extends IView<ITreeItemCellPresenter, ITreeItemCellView> {

	StringProperty titleProperty();

}
