package net.d80harri.wr.service;

import net.d80harri.wr.db.EntityFactory;
import net.d80harri.wr.db.model.Item;
import net.d80harri.wr.service.model.ItemDto;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:net/d80harri/wr/service/application-context.xml" })
public class ServiceTest {
	
	private static final class DefaultAnswer implements Answer<Object> {

		@Override
		public Object answer(InvocationOnMock invocation) throws Throwable {
			throw new RuntimeException(invocation + " not yet mocked");
		}
		
	}
	
	private static final Answer<Object> DEFAULT_ANSWER = new DefaultAnswer();
	
	@Autowired
	private Service service;

	@Before
	public void initMock() {
		service.setEntityFactory(Mockito.mock(EntityFactory.class, DEFAULT_ANSWER));		
	}
	
	@Test
	public void saveOrUpdate() {
		Mockito.doAnswer(invocation -> {
			Item item = (Item)invocation.getArguments()[0];
			item.setId(1);
			item.setTitle("My title");
			return null;
		}).when(service.getEntityFactory()).saveOrUpdate(Mockito.any());
		
		ItemDto result = service.saveOrUpdate(new ItemDto("My title"));
		
		Assertions.assertThat(result.getId()).isEqualTo(1);
		Assertions.assertThat(result.getTitle()).isEqualTo("My title");
	}
}
