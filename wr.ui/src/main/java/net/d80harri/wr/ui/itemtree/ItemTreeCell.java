package net.d80harri.wr.ui.itemtree;

import java.util.Optional;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ItemTreeCell extends TreeCell<ItemModel> {

	private ItemTreeCellView view = null;

	@Override
	protected void updateItem(ItemModel item, boolean empty) {
		System.out.println(empty + " " + this.hashCode()
				+ " updateItem called " + item);
		super.updateItem(item, empty);
		if (empty || item == null) {
			setText(null);
			setGraphic(null);
		} else {
			setText(null);
			createView();

			setModel(item);
			setGraphic(view.getView());
		}
	}

	public void createView() {
		this.view = new ItemTreeCellView();
		this.view.getPresenter().title.setOnKeyPressed(this::keyPressed);
	}
	public void keyPressed(KeyEvent evt) {
		if (evt.getCode() == KeyCode.ENTER) {
			ItemModel newModel = new ItemModel();
			newModel.setTitle(""+Math.random());
			TreeItem<ItemModel> item = new TreeItem<ItemModel>(newModel);
			getTreeItem().getParent().getChildren().add(item);
			
//			getTreeView().getSelectionModel().select(item);
			
			getTreeView().layout();
			getTreeView().edit(item);
		}
	}

	private void setModel(ItemModel model) {
		((ItemTreeCellPresenter)view.getPresenter()).setModel(Optional.of(model));
	}

	@Override
	public void startEdit() {
		super.startEdit();
		view.getPresenter().requestFocus();
	}
}
