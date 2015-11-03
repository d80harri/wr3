package net.d80harri.wr.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import net.d80harri.wr.ui.itemtree.IItemTreeView;
import net.d80harri.wr.ui.itemtree.ViewBase;

public class AppView extends ViewBase<AppPresenter> implements Initializable {

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
