package net.d80harri.wr.service;

import ma.glasnost.orika.impl.ConfigurableMapper;
import net.d80harri.wr.db.EntityFactory;
import net.d80harri.wr.db.model.WrEntity;
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

}
