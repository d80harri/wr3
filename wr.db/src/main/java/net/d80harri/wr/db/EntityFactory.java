package net.d80harri.wr.db;

import java.util.List;

import net.d80harri.wr.db.model.WrEntity;

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
}
