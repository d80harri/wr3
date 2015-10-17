package net.d80harri.wr.db;

import net.d80harri.wr.db.model.Item;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/net/d80harri/wr/db/test-application-context.xml"})
public class MyTest {
	
	@Autowired
	private EntityFactory entityFactory;
	
	@Autowired
	private ProviderFactory providerFactory;
	
	@Test
//	@Transactional
	public void myTest() throws ValueProviderException {
		entityFactory.selectAll(Item.class);
		
		ValueProvider<Item> provider = providerFactory.get(Item.class);
		System.out.println(provider);
		
		Item item = provider.provide();
		Item parent = provider.provide();
		
		item.setParentItem(parent);
		System.out.println(item);
		
		entityFactory.saveOrUpdate(parent);
		entityFactory.saveOrUpdate(item);
		
		item = entityFactory.selectById(item.getId(), Item.class);
		
		item.getParentItem().getTitle();
	}
}
