package net.d80harri.wr.ui.itemtree2;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class TreeItemCellView extends ViewBase<TreeItemCellPresenter> {
	@FXML
	private TextField txtTitle;

	@Override
	protected void registerHandlers() {
		txtTitle.setOnKeyPressed(this::txtTitle_KeyPressed);
	}

	public TextField getTxtTitle() {
		return txtTitle;
	}
	
	private void txtTitle_KeyPressed(KeyEvent evt) {
		presenter.createSibling();
	}
	
	@Override
	public void requestFocus() {
		super.requestFocus();
		txtTitle.selectAll();
		txtTitle.requestFocus();
	}
}
