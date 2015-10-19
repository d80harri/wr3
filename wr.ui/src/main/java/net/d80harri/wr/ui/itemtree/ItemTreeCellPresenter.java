package net.d80harri.wr.ui.itemtree;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import net.d80harri.wr.ui.core.BindingUtils;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ItemTreeCellPresenter implements Initializable {
	
	@FXML
	private BooleanProperty detailVisible = new SimpleBooleanProperty(false);

	@FXML 
	private TextField title;
	@FXML
	private TextField description;
	
	private Optional<ItemModel> model;

	private BindingUtils bu = new BindingUtils();
	
	private ItemTreeCell cell;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		title.setOnKeyPressed(this::keyPressed);
	}

	public void setModel(Optional<ItemModel> model) {
		bu.unbindAll();
		this.model = model;
		if (model.isPresent()) {
			ItemModel item = model.get();
			bu.bindBidirectional(title.textProperty(), item.titleProperty());
			bu.bindBidirectional(description.textProperty(),
					item.descriptionProperty());
		}
	}

	public void requestFocus() {
		title.setText(title.getText() + " -----");
		title.selectAll();
		title.requestFocus();
	}

	public final BooleanProperty detailVisibleProperty() {
		return this.detailVisible;
	}

	public final boolean isDetailVisible() {
		return this.detailVisibleProperty().get();
	}

	public final void setDetailVisible(final boolean detailVisible) {
		this.detailVisibleProperty().set(detailVisible);
	}

	private void keyPressed(KeyEvent evt) {
		if (evt.getCode() == KeyCode.ENTER) {
			ItemModel newModel = new ItemModel();
			newModel.setTitle(""+Math.random());
			TreeItem<ItemModel> item = new TreeItem<ItemModel>(newModel);
			cell.getTreeItem().getParent().getChildren().add(item);
			
//			getTreeView().getSelectionModel().select(item);
			
			cell.getTreeView().layout();
			cell.getTreeView().edit(item);
		}
	}

	public void setCell(ItemTreeCell itemTreeCell) {
		this.cell = itemTreeCell;
	}
}
