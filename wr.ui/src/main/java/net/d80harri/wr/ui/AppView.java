package net.d80harri.wr.ui;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import net.d80harri.wr.ui.itemtree.IItemTreeView;
import net.d80harri.wr.ui.itemtree.ViewBase;

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
