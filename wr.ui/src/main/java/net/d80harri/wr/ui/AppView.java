package net.d80harri.wr.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import net.d80harri.wr.ui.core.ViewBase;
import net.d80harri.wr.ui.itemtree.IItemTreeView;

public class AppView extends ViewBase<AppPresenter, AppView> implements Initializable {

	public AppView(AppPresenter presenter) {
		super(presenter);
	}

	@FXML
	private IItemTreeView itemTreeView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@FXML
	private void addEventHandler(ActionEvent event) {
		itemTreeView.createRootNode();
	}

}
