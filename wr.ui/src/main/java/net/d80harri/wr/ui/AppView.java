package net.d80harri.wr.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import net.d80harri.wr.ui.core.ViewBase;
import net.d80harri.wr.ui.core.WrUiAppContext;
import net.d80harri.wr.ui.itemtree.ItemTreeView;
import net.d80harri.wr.ui.itemtree.cell.ItemCellPresenter;
import net.d80harri.wr.ui.itemtree.cell.ItemCellView;

import org.fxmisc.easybind.EasyBind;

public class AppView extends ViewBase<AppPresenter> implements Initializable {

	public AppView(AppPresenter presenter) {
		super(presenter);
	}

	@FXML
	private ItemTreeView itemTreeView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		itemTreeView.presenterProperty().bind(EasyBind.select(presenterProperty()).selectObject(i -> i.itemTreeProperty()));
		itemTreeView.setTreeItemCellFactory(() -> new ItemCellView(WrUiAppContext.get().getBean(ItemCellPresenter.class)));
	}

	@FXML
	private void addEventHandler(ActionEvent event) {
		getPresenter().createRootItem();
	}

}
