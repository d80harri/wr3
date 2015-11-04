package net.d80harri.wr.ui.core;

import net.d80harri.wr.service.Service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class WrUiAppContext implements ApplicationContextAware {
	private ApplicationContext context;
	

	private WrUiAppContext() {
	}
	
	public Service getService() {
		return context.getBean(Service.class);
	}
	
	public <T> T getBean(Class<T> requiredType) throws BeansException {
		return context.getBean(requiredType);
	}
	
	public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
		return context.getBean(requiredType, args);
	}
	

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.context = applicationContext;
	}
	
	private static WrUiAppContext INSTANCE;
	
	public static WrUiAppContext get() {
		if (INSTANCE == null) {
			ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("net/d80harri/wr/ui/application-context.xml");
			INSTANCE = ctx.getBean(WrUiAppContext.class);
		}
		return INSTANCE;
	}
}
