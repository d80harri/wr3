package net.d80harri.wr.ui.core;

import java.util.List;

import net.d80harri.wr.db.EntityFactory;
import net.d80harri.wr.ui.itemtree.ItemModel;

public class ServiceProxy {
	private EntityFactory entityFactory;

	public EntityFactory getEntityFactory() {
		return entityFactory;
	}

	public void setEntityFactory(EntityFactory entityFactory) {
		this.entityFactory = entityFactory;
	}

	public List<ItemModel> getRootItems() {
		throw new RuntimeException("NYI");
	}

	public List<ItemModel> getChildItemsOf(int id) {
		throw new RuntimeException("NYI");
	}

}
