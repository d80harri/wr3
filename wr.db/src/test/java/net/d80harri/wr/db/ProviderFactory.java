package net.d80harri.wr.db;

import java.util.HashMap;

public class ProviderFactory {
	
	private HashMap<Class<? extends ValueProvider<?>>, ValueProvider<?>> registeredProviders;
	
	public HashMap<Class<? extends ValueProvider<?>>, ValueProvider<?>> getRegisteredProviders() {
		return registeredProviders;
	}
	
	public void setRegisteredProviders(
			HashMap<Class<? extends ValueProvider<?>>, ValueProvider<?>> registeredProviders) {
		this.registeredProviders = registeredProviders;
	}
	
	@SuppressWarnings("unchecked")
	public <T> ValueProvider<T> get(Class<T> type) {
		return (ValueProvider<T>) registeredProviders.get(type);
	}
}
