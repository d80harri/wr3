package net.d80harri.wr.service;

import java.util.List;

import ma.glasnost.orika.impl.ConfigurableMapper;
import net.d80harri.wr.db.EntityFactory;
import net.d80harri.wr.db.model.Item;
import net.d80harri.wr.db.model.WrEntity;
import net.d80harri.wr.service.model.ItemDto;
import net.d80harri.wr.service.model.WrDto;

public class Service {

	private EntityFactory entityFactory;
	private ConfigurableMapper mapperFacade;
	
	public <T extends WrDto> T saveOrUpdate(T dto) {
		WrEntity entity = convert(dto);
		entityFactory.saveOrUpdate(entity);
		return (T)convert(entity, WrDto.class);
	}

	private WrDto convert(WrEntity entity, Class<WrDto> clazz) {
		return mapperFacade.map(entity, clazz);
	}

	private <T extends WrDto> WrEntity convert(T entity) {
		return mapperFacade.map(entity, WrEntity.class);
	}
	
	public EntityFactory getEntityFactory() {
		return entityFactory;
	}
	
	public void setEntityFactory(EntityFactory entityFactory) {
		this.entityFactory = entityFactory;
	}
	
	public ConfigurableMapper getMapperFacade() {
		return mapperFacade;
	}
	
	public void setMapperFacade(ConfigurableMapper mapperFacade) {
		this.mapperFacade = mapperFacade;
	}

	public List<ItemDto> getRootItems() {
		List<Item> itemResult = entityFactory.getRootItems();
		return mapperFacade.mapAsList(itemResult, ItemDto.class);
	}

	public List<ItemDto> getItemsByParentId(int id) {
		List<Item> itemResult = entityFactory.getChildItemsOf(id);
		return mapperFacade.mapAsList(itemResult, ItemDto.class);
	}

}
