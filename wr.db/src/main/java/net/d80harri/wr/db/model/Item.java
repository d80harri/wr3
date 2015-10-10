package net.d80harri.wr.db.model;

import java.util.Set;

public class Item extends WrEntity {

	private String title;
	private String description;

	private Item parentItem;
	private Set<Item> childItems;

	private Set<ItemList> itemLists;
	private ItemType type;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Item getParentItem() {
		return parentItem;
	}

	public void setParentItem(Item parentItem) {
		this.parentItem = parentItem;
	}

	public Set<Item> getChildItems() {
		return childItems;
	}

	public void setChildItems(Set<Item> childItems) {
		this.childItems = childItems;
	}

	public Set<ItemList> getItemLists() {
		return itemLists;
	}

	public void setItemLists(Set<ItemList> itemLists) {
		this.itemLists = itemLists;
	}

	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}
}
