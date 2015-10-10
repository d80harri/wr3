package net.d80harri.wr.db;

import java.util.List;

import net.d80harri.wr.db.model.Item;
import net.d80harri.wr.db.model.WrEntity;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

public class EntityFactory {
	private SessionFactory sessionFactory;
	 
    public EntityFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @SuppressWarnings("unchecked")
    @Transactional
	public <T extends WrEntity> List<T> selectAll(Class<T> type) {
    	return sessionFactory.getCurrentSession().createQuery("FROM " + type.getName()).list();
    }
    
    @Transactional
    public void saveOrUpdate(WrEntity dbo) {
    	sessionFactory.getCurrentSession().saveOrUpdate(dbo);
    }

	@SuppressWarnings("unchecked")
	@Transactional
	public <T extends WrEntity> T selectById(Integer id, Class<T> type) {
		return (T)sessionFactory.getCurrentSession().createQuery("FROM " + type.getName() + " WHERE id = :id").setParameter("id", id).uniqueResult();		
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Item> getRootItems() {
		return query("FROM Item where parentItem = null").list();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Item> getChildItemsOf(int id) {
		return query("FROM Item where parentItem.id = :id").setParameter("id", id).list();
	}
	
	private Query query(String hql) {
		return sessionFactory.getCurrentSession().createQuery(hql);
	}


}
