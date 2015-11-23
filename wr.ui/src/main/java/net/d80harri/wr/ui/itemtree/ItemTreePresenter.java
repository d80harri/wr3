package net.d80harri.wr.ui.itemtree;

import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import net.d80harri.wr.service.Service;
import net.d80harri.wr.service.model.ItemDto;
import net.d80harri.wr.service.util.SpringAwareBeanMapper;
import net.d80harri.wr.ui.itemtree.cell.TreeItemCellPresenter;

import org.fxmisc.easybind.EasyBind;

public class ItemTreePresenter {
	private Service service;
	private SpringAwareBeanMapper mapper;

	private TreeItemCellPresenter invisibleRoot;

	public ItemTreePresenter(Service service, SpringAwareBeanMapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}

	public TreeItemCellPresenter getInvisibleRoot() {
		if (invisibleRoot == null) {
			invisibleRoot = new TreeItemCellPresenter(service, mapper);
		}
		return invisibleRoot;
	}

	public ObservableList<TreeItemCellPresenter> getRootItems() {
		return getInvisibleRoot().getChildren();
	}

	private ObjectProperty<TreeItemCellPresenter> activeItem;

	public final ObjectProperty<TreeItemCellPresenter> activeItemProperty() {
		if (activeItem == null) {
			activeItem = new SimpleObjectProperty<>(this, "activeItem");

			activeItem.addListener((obs, o, n) -> {
				if (o != null) {
					o.setActivated(false);
				}
				if (n != null) {
					n.setActivated(true);
				}
			});
		}
		return this.activeItem;
	}

	public final TreeItemCellPresenter getActiveItem() {
		return this.activeItemProperty().get();
	}

	public final void setActiveItem(final TreeItemCellPresenter activeItem) {
		this.activeItemProperty().set(activeItem);
	}

	// ======================================================================
	// OPERATIONS
	// ======================================================================

	public void addRootNode(TreeItemCellPresenter rootNode) {
		registerCellPresenter(rootNode);
		getRootItems().add(rootNode);
		setActiveItem(rootNode);
	}

	public void addNodeAfterActive(TreeItemCellPresenter newPresenter) {
		registerCellPresenter(newPresenter);

		int indexOfActive = getRootItems().indexOf(getActiveItem());
		getRootItems().add(indexOfActive + 1, newPresenter);

		setActiveItem(newPresenter);
	}

	public void addNodeAfterActive(String title) {
		TreeItemCellPresenter newPresenter = new TreeItemCellPresenter(service,
				mapper);
		newPresenter.setTitle(title);

		addNodeAfterActive(newPresenter);
	}

	private void registerCellPresenter(TreeItemCellPresenter presenter) {
		EasyBind.subscribe(presenter.activatedProperty(), n -> {
			if (n) {
				setActiveItem(presenter);
			}
		});
	}

	public void load() {
		List<ItemDto> dtos = service.getRootItems();
		List<TreeItemCellPresenter> presenterResult = mapper.mapAsList(dtos,
				TreeItemCellPresenter.class);
		presenterResult.stream().forEach(i -> {
			i.setService(service);
			i.setMapper(mapper);
			registerCellPresenter(i);
		});
		
		getRootItems().clear();
		getRootItems().addAll(presenterResult);
	}
}
