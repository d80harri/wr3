package net.d80harri.wr.ui.itemtree;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import net.d80harri.wr.db.EntityFactory;
import net.d80harri.wr.ui.core.ServiceProxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemTreePresenterLogic {
	
	@Autowired
	private ServiceProxy serviceProxy;
	
	@Autowired
    private MapperFacade mapper = null;

	public List<ItemModel> getRootItems() {
		return mapper.mapAsList(serviceProxy.getRootItems(), ItemModel.class);
	}

	public List<ItemModel> getChildItemsOf(int id) {
		return mapper.mapAsList(serviceProxy.getChildItemsOf(id), ItemModel.class);
	}
	
	
}
