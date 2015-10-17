package net.d80harri.wr.ui.itemtree;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ItemModel {
	private Integer id;
	private StringProperty title = new SimpleStringProperty();
	private StringProperty description = new SimpleStringProperty();

	public String getTitle() {
		return title.get();
	}

	public void setTitle(String title) {
		this.title.set(title);
	}

	public String getDescription() {
		return description.get();
	}

	public void setDescription(String description) {
		this.description.set(description);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Property<String> titleProperty() {
		return title;
	}

	public Property<String> descriptionProperty() {
		return description;
	}

	@Override
	public String toString() {
		return "ItemModel [id=" + id + ", title=" + title + ", description="
				+ description + "]";
	}
	
	
}
