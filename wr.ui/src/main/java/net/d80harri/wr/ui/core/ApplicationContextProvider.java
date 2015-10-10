package net.d80harri.wr.ui.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationContextProvider {
	private static ApplicationContextProvider INSTANCE;
	
	private static void initialize() {
		if (INSTANCE == null) {
			INSTANCE = new ApplicationContextProvider();
			INSTANCE.setApplicationContext(new ClassPathXmlApplicationContext("/net/d80harri/wr/ui/application-context.xml"));
		} 
	}
	
	public static ApplicationContextProvider getInstance() {
		initialize();
		return INSTANCE;
	}
	
	public static ApplicationContext getApplicationContext() {
		return getInstance().applicationContext;
	}
	
	private ApplicationContext applicationContext;
	
	private ApplicationContextProvider() {
		
	}

	private void setApplicationContext(ApplicationContext ctx) {
		this.applicationContext = ctx;
	}

}
