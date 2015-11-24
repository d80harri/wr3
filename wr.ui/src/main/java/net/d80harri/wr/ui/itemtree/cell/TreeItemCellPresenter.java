package net.d80harri.wr.ui.itemtree.cell;

import java.util.function.Function;
import java.util.stream.Stream;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import net.d80harri.wr.service.Service;
import net.d80harri.wr.service.model.ItemDto;
import net.d80harri.wr.service.util.SpringAwareBeanMapper;

import org.fxmisc.easybind.EasyBind;
import org.fxmisc.easybind.monadic.MonadicBinding;

public class TreeItemCellPresenter {

	private Service service;
	private SpringAwareBeanMapper mapper;

	private ObjectProperty<Integer> id;
	private StringProperty title;
	private ObservableList<TreeItemCellPresenter> children;
	private ObjectProperty<TreeItemCellPresenter> parent;

	public TreeItemCellPresenter() {
	}

	public TreeItemCellPresenter(Service service, SpringAwareBeanMapper mapper) {
		this.service = service;
		this.mapper = mapper;
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

	public ObservableList<TreeItemCellPresenter> getChildren() {
		if (children == null) {
			children = FXCollections.observableArrayList();
			children.addListener(new ListChangeListener<TreeItemCellPresenter>() {

				@Override
				public void onChanged(Change<? extends TreeItemCellPresenter> c) {
					while (c.next()) {
						if (c.wasPermutated() || c.wasReplaced()
								|| c.wasUpdated()) {
							throw new UnsupportedOperationException("NYI");
						}
						if (c.wasAdded()) {
							c.getAddedSubList().stream().forEach(i -> {
								i.setParent(TreeItemCellPresenter.this);
							});
						}
						if (c.wasRemoved()) {
							c.getRemoved().stream()
									.forEach(i -> i.setParent(null));
						}
					}
				}

			});
		}
		return children;
	}

	public final StringProperty titleProperty() {
		if (title == null) {
			title = new SimpleStringProperty(this, "title");
		}
		return this.title;
	}

	public final String getTitle() {
		return this.titleProperty().get();
	}

	public final void setTitle(final String title) {
		this.titleProperty().set(title);
	}

	public final ObjectProperty<Integer> idProperty() {
		if (id == null) {
			id = new SimpleObjectProperty<>(this, "id");
		}
		return this.id;
	}

	public final Integer getId() {
		return this.idProperty().get();
	}

	public final void setId(final Integer id) {
		this.idProperty().set(id);
	}

	private BooleanProperty activated;

	public final BooleanProperty activatedProperty() {
		if (activated == null) {
			activated = new SimpleBooleanProperty(false);
			activated.addListener((obs, o, n) -> {
				if (!n) {
					this.saveOrUpdate();
				}
			});
		}
		return this.activated;
	}

	public final boolean isActivated() {
		return this.activatedProperty().get();
	}

	public final void setActivated(final boolean activated) {
		this.activatedProperty().set(activated);
	}

	public final ObjectProperty<TreeItemCellPresenter> parentProperty() {
		if (parent == null) {
			parent = new SimpleObjectProperty<>(this, "parent");
			parent.addListener((obs, o, n) -> {
				if (o != null) {
					o.getChildren().remove(this);
				}
				if (n != null) {
					if (!n.getChildren().contains(this))
						n.getChildren().add(this);
				}
			});
		}
		return this.parent;
	}

	public final TreeItemCellPresenter getParent() {
		return this.parentProperty().get();
	}

	public final void setParent(final TreeItemCellPresenter parent) {
		this.parentProperty().set(parent);
	}

	private IntegerBinding childIndex;

	public final IntegerBinding childIndexProperty() {
		if (childIndex == null) {
			childIndex = Bindings.createIntegerBinding(() -> getParent()
					.getChildren().indexOf(this), getParent().getChildren());
		}
		return this.childIndex;
	}

	public final int getChildIndex() {
		return this.childIndexProperty().get();
	}

	// =================================================================
	// Operations
	// =================================================================
	public void saveOrUpdate() {
		ItemDto dto = mapper.map(this, ItemDto.class);
		ItemDto persisted = service.saveOrUpdate(dto);
		mapper.map(persisted, this);
	}

	public void loadChildren() {
		this.getChildren().clear();
		this.getChildren().addAll(
				mapper.mapAsList(service.getItemsByParentId(this.getId()),
						TreeItemCellPresenter.class));
		this.getChildren().stream().forEach(i -> {
			i.setService(this.service);
			i.setMapper(this.mapper);
		});
	}

	public void gotoPreviousSibling() {
		TreeItemCellPresenter parent = getParent();
		if (parent != null) {
			int idxOfThis = parent.getChildren().indexOf(this);
			if (idxOfThis != 0) {
				parent.getChildren().get(idxOfThis - 1).setActivated(true);
			}
		}
	}

	public void gotoNextSibling() {
		TreeItemCellPresenter parent = getParent();
		if (parent != null) {
			int idxOfThis = parent.getChildren().indexOf(this);
			if (idxOfThis != parent.getChildren().size()) {
				this.setActivated(false);
				parent.getChildren().get(idxOfThis + 1).setActivated(true);
			}
		}
	}

	public void delete() {
		TreeItemCellPresenter parent = getParent();
		if (parent != null) {
			this.setActivated(false);
			parent.getChildren().remove(this);
		}
	}

	public void mergeNextInto() {
		TreeItemCellPresenter parent = getParent();
		if (parent != null) {
			int idx = getChildIndex();
			if (idx != parent.getChildren().size()) {
				TreeItemCellPresenter toMerge = parent.getChildren().get(
						idx + 1);
				mergeInto(toMerge);
			}
		}
	}

	public TreeItemCellPresenter getPrevious() {
		TreeItemCellPresenter result = null;
		TreeItemCellPresenter parent = getParent();
		if (parent != null) {
			int idxOfThis = parent.getChildren().indexOf(this);
			if (idxOfThis != 0) {
				result = getParent().getChildren().get(idxOfThis - 1);
			}
		}
		return result;
	}

	public TreeItemCellPresenter getNext() {
		TreeItemCellPresenter result = null;
		TreeItemCellPresenter parent = getParent();
		if (parent != null) {
			int idx = parent.getChildren().indexOf(this);
			if (idx != parent.getChildren().size()) {
				result = parent.getChildren().get(idx + 1);
			}
		}
		return result;
	}

	public void mergePreviousInto() {
		TreeItemCellPresenter previous = getPrevious();
		if (previous != null) {
			previous.mergeInto(this);
		}
	}

	public void mergeInto(TreeItemCellPresenter toMerge) {
		this.setTitle(this.getTitle() + toMerge.getTitle());
		toMerge.delete();
	}

	public void switchWithNext() {
		TreeItemCellPresenter next = getNext();
		if (next != null) {
			switchWith(next);
		}
	}

	public void switchWith(TreeItemCellPresenter toSwitch) {
		TreeItemCellPresenter thisParent = this.getParent();
		int thisIdx = this.getChildIndex();
		TreeItemCellPresenter toSwitchParent = toSwitch.getParent();
		int switchIdx = toSwitch.getChildIndex();

		this.setParent(null);
		toSwitchParent.getChildren().add(switchIdx, this);
		toSwitch.setParent(null);
		thisParent.getChildren().add(thisIdx, toSwitch);
	}

	public void indent() {
		TreeItemCellPresenter previous = getPrevious();
		if (previous != null)
			this.setParent(previous);
	}

}
