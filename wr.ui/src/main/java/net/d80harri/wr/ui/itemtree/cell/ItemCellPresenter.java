package net.d80harri.wr.ui.itemtree.cell;

import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import net.d80harri.wr.service.Service;
import net.d80harri.wr.service.model.ItemDto;
import net.d80harri.wr.service.util.SpringAwareBeanMapper;

public class ItemCellPresenter {

	private Service service;
	private SpringAwareBeanMapper mapper;

	private ObjectProperty<Integer> id;
	private StringProperty title;
	private ObservableList<ItemCellPresenter> children;
	private ObjectProperty<ItemCellPresenter> parent;
	private IntegerProperty titleCaretPosition;
	private BooleanProperty expanded;

	public ItemCellPresenter() {
	}

	public ItemCellPresenter(Service service, SpringAwareBeanMapper mapper) {
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

	public ObservableList<ItemCellPresenter> getChildren() {
		if (children == null) {
			children = FXCollections.observableArrayList();
			children.addListener(new ListChangeListener<ItemCellPresenter>() {

				@Override
				public void onChanged(Change<? extends ItemCellPresenter> c) {
					while (c.next()) {
						if (c.wasPermutated() || c.wasReplaced()
								|| c.wasUpdated()) {
							throw new UnsupportedOperationException("NYI");
						}
						if (c.wasAdded()) {
							c.getAddedSubList().stream().forEach(i -> {
								i.setParent(ItemCellPresenter.this);
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

	public final ObjectProperty<ItemCellPresenter> parentProperty() {
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

	public final ItemCellPresenter getParent() {
		return this.parentProperty().get();
	}

	public final void setParent(final ItemCellPresenter parent) {
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

	public final IntegerProperty titleCaretPositionProperty() {
		if (titleCaretPosition == null) {
			titleCaretPosition = new SimpleIntegerProperty(this,
					"titleCaretPosition");
		}
		return this.titleCaretPosition;
	}

	public final int getTitleCaretPosition() {
		return this.titleCaretPositionProperty().get();
	}

	public final void setTitleCaretPosition(final int titleCaretPosition) {
		this.titleCaretPositionProperty().set(titleCaretPosition);
	}

	public final BooleanProperty expandedProperty() {
		if (expanded == null) {
			expanded = new SimpleBooleanProperty(this, "expanded");
		}
		return this.expanded;
	}

	public final boolean isExpanded() {
		return this.expandedProperty().get();
	}

	public final void setExpandedProperty(final boolean expandedProperty) {
		this.expandedProperty().set(expandedProperty);
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
						ItemCellPresenter.class));
		this.getChildren().stream().forEach(i -> {
			i.setService(this.service);
			i.setMapper(this.mapper);
		});
	}

	public void gotoPreviousSibling() {
		ItemCellPresenter prev = getPrevious();
		if (prev != null) {
			prev.setActivated(true);
		}
	}

	public void gotoNextSibling() {
		ItemCellPresenter next = getNext();
		if (next != null) {
			this.setActivated(false);
			next.setActivated(true);
		}
	}

	public void delete() {
		ItemCellPresenter parent = getParent();
		if (parent != null) {
			this.setActivated(false);
			parent.getChildren().remove(this);
		}
	}

	public void mergeNextInto() {
		ItemCellPresenter next = getNext();
		if (next != null)
			mergeInto(next);
	}

	public ItemCellPresenter getPrevious() {
		ItemCellPresenter result = null;
		ItemCellPresenter parent = getParent();
		if (parent != null) {
			int idxOfThis = parent.getChildren().indexOf(this);
			if (idxOfThis != 0) {
				result = getParent().getChildren().get(idxOfThis - 1);
			}
		}
		return result;
	}

	public ItemCellPresenter getNext() {
		ItemCellPresenter result = null;
		ItemCellPresenter parent = getParent();
		if (parent != null) {
			int idx = parent.getChildren().indexOf(this);
			if (idx != parent.getChildren().size()) {
				result = parent.getChildren().get(idx + 1);
			}
		}
		return result;
	}

	public void mergePreviousInto() {
		ItemCellPresenter previous = getPrevious();
		if (previous != null) {
			previous.mergeInto(this);
		}
	}

	public void mergeInto(ItemCellPresenter toMerge) {
		this.setTitle(this.getTitle() + toMerge.getTitle());
		toMerge.delete();
	}

	public void switchWithNext() {
		ItemCellPresenter next = getNext();
		if (next != null) {
			switchWith(next);
		}
	}

	public void switchWithPrev() {
		ItemCellPresenter prev = getPrevious();
		if (prev != null) {
			switchWith(prev);
		}
	}

	public void switchWith(ItemCellPresenter toSwitch) {
		ItemCellPresenter thisParent = this.getParent();
		int thisIdx = this.getChildIndex();
		ItemCellPresenter toSwitchParent = toSwitch.getParent();
		int switchIdx = toSwitch.getChildIndex();

		this.setParent(null);
		toSwitchParent.getChildren().add(switchIdx, this);
		toSwitch.setParent(null);
		thisParent.getChildren().add(thisIdx, toSwitch);
	}

	public void indent() {
		ItemCellPresenter previous = getPrevious();
		if (previous != null)
			this.setParent(previous);
	}

	public void outdent() {
		if (getParent() != null) {
			ItemCellPresenter grandParent = getParent().getParent();
			int idx = getParent().getChildIndex();
			getParent().getChildren().remove(this);
			grandParent.getChildren().add(idx + 1, this);
		}
	}

	public ItemCellPresenter splitItem() {
		ItemCellPresenter result = null;
		if (getParent() != null) {
			int caretPosition = getTitleCaretPosition();
			String newText = getTitle().substring(caretPosition);
			setTitle(getTitle().substring(0, caretPosition));

			result = new ItemCellPresenter(this.service, this.mapper);
			result.setTitle(newText);
			this.getParent().getChildren().add(getChildIndex() + 1, result);
		}
		return result;
	}

	public void toggleExpand() {
		boolean expanded = isExpanded();
		if (expanded) {
			setExpandedProperty(false);
			setActivated(true);
		} else {
			setExpandedProperty(true);
			List<ItemCellPresenter> children = getChildren();
			if (!children.isEmpty()) {
				children.get(0).setActivated(true);
			}
		}
	}

}
