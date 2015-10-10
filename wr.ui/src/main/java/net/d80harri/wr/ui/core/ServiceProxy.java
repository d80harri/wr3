package net.d80harri.wr.ui.core;

import net.d80harri.wr.db.EntityFactory;

public class ServiceProxy {
	private EntityFactory entityFactory;

	public EntityFactory getEntityFactory() {
		return entityFactory;
	}

	public void setEntityFactory(EntityFactory entityFactory) {
		this.entityFactory = entityFactory;
	}
}
