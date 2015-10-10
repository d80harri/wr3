package net.d80harri.wr.ui.itemtree;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import net.d80harri.wr.db.EntityFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemTreePresenterLogic {
	@Autowired
	private EntityFactory ef;
	
	@Autowired
    private MapperFacade mapper = null;

	public List<ItemModel> getRootItems() {
		return mapper.mapAsList(ef.getRootItems(), ItemModel.class);
	}

	public List<ItemModel> getChildItemsOf(int id) {
		return mapper.mapAsList(ef.getChildItemsOf(id), ItemModel.class);
	}
	
	
}
