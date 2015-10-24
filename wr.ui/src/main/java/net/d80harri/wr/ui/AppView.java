package net.d80harri.wr.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import net.d80harri.wr.ui.itemtree2.IItemTreeView;
import net.d80harri.wr.ui.itemtree2.ViewBase;

public class AppView extends ViewBase<AppPresenter> {

	@FXML
	private IItemTreeView itemTreeView;
	
	@Override
	protected void registerHandlers() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private void addEventHandler(ActionEvent event) {
		itemTreeView.createRootNode();
	}
	
}
