package net.d80harri.wr.db.testutils;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

public class DatabaseTruncator {
	private static final Log LOG = LogFactory.getLog(DatabaseTruncator.class);
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Transactional
	public void truncateAll() {
		Map<String, ClassMetadata>  map = (Map<String, ClassMetadata>) sessionFactory.getAllClassMetadata();
	    for(String entityName : map.keySet()){
	    	int count = sessionFactory.getCurrentSession().createQuery("DELETE FROM " + entityName).executeUpdate();
	    	LOG.info("Deleted " + count + " rows from entity " + entityName);
	    }
	}
	
	public static void main(String[] args) {
		try(ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/net/d80harri/wr/db/testutils/application-context.xml")){			
			DatabaseTruncator truncator = context.getBean(DatabaseTruncator.class);
			truncator.truncateAll();
		};
	}
}
