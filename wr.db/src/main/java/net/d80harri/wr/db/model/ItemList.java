package net.d80harri.wr.db.model;

import java.util.List;

public class ItemList extends WrEntity {
	private String name;
	private ItemList parentList;
	private List<ItemList> childLists;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ItemList getParentList() {
		return parentList;
	}

	public void setParentList(ItemList parentList) {
		this.parentList = parentList;
	}

	public List<ItemList> getChildLists() {
		return childLists;
	}
	
	public void setChildLists(List<ItemList> childLists) {
		this.childLists = childLists;
	}
}
