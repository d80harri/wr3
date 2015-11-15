package net.d80harri.wr.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import net.d80harri.wr.ui.core.ViewBase;
import net.d80harri.wr.ui.core.WrUiAppContext;
import net.d80harri.wr.ui.itemtree.ItemTreeView;
import net.d80harri.wr.ui.itemtree.cell.TreeItemCellPresenter;
import net.d80harri.wr.ui.itemtree.cell.TreeItemCellView;

public class AppView extends ViewBase<AppPresenter, AppView> implements Initializable {

	public AppView(AppPresenter presenter) {
		super(presenter);
	}

	@FXML
	private ItemTreeView itemTreeView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		itemTreeView.setTreeItemCellFactory(() -> new TreeItemCellView(WrUiAppContext.get().getBean(TreeItemCellPresenter.class)));
	}

	@FXML
	private void addEventHandler(ActionEvent event) {
		itemTreeView.createRootNode();
	}

}
