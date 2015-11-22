package net.d80harri.wr.ui.itemtree.cell;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.d80harri.wr.service.Service;
import net.d80harri.wr.service.model.ItemDto;
import net.d80harri.wr.service.util.SpringAwareBeanMapper;

public class TreeItemCellPresenter {

	private Service service;
	
	private SpringAwareBeanMapper mapper;

	public TreeItemCellPresenter() {}
	
	public TreeItemCellPresenter(Service service, SpringAwareBeanMapper mapper) {
		this.service = service;
		this.mapper = mapper;				
	}

	public void saveOrUpdate() {
		ItemDto dto = mapper.map(this, ItemDto.class);
		ItemDto persisted = service.saveOrUpdate(dto);
		mapper.map(persisted, this);
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}
	
	public SpringAwareBeanMapper getMapper() {
		return mapper;
	}
	public void setMapper(SpringAwareBeanMapper mapper) {
		this.mapper = mapper;
	}

	private ObservableList<TreeItemCellPresenter> children = FXCollections.observableArrayList();
	
	public ObservableList<TreeItemCellPresenter> getChildren() {
		return children;
	}
	
	private StringProperty title;
	public final StringProperty titleProperty() {
		if (title == null) {
			title = new SimpleStringProperty(this, "title");
		}
		return this.title;
	}

	public final java.lang.String getTitle() {
		return this.titleProperty().get();
	}

	public final void setTitle(final java.lang.String title) {
		this.titleProperty().set(title);
	}

	private ObjectProperty<Integer> id;
	public final ObjectProperty<Integer> idProperty() {
		if (id == null) {
			id = new SimpleObjectProperty<>(this, "id");
		}
		return this.id;
	}

	public final java.lang.Integer getId() {
		return this.idProperty().get();
	}

	public final void setId(final java.lang.Integer id) {
		this.idProperty().set(id);
	}
	

	private BooleanProperty activated;
	
	public final BooleanProperty activatedProperty() {
		if (activated == null) {
			activated = new SimpleBooleanProperty(false);
		}
		return this.activated;
	}

	public final boolean isActivated() {
		return this.activatedProperty().get();
	}

	public final void setActivated(final boolean activated) {
		this.activatedProperty().set(activated);
	}

	public void addChildItem(TreeItemCellPresenter newPresenter) {
		getChildren().add(newPresenter);
	}

}
