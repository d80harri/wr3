package net.d80harri.wr.db;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map.Entry;

public class CompositeProvider<T> implements ValueProvider<T> {

	private HashMap<String, ValueProvider<?>> properties = new HashMap<>();
	
	private Class<T> type;
	
	public CompositeProvider(Class<T> type) {
		this.type = type;
	}
	
	public HashMap<String, ValueProvider<?>> getProperties() {
		return properties;
	}
	
	public void setProperties(HashMap<String, ValueProvider<?>> properties) {
		this.properties = properties;
	}
	
	public T provide() throws ValueProviderException {
		try {
			T result = type.getConstructor().newInstance();
			setFields(result);
			return result;
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | NoSuchFieldException e) {
			throw new ValueProviderException(e);
		}		
	}

	private void setFields(T result) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, ValueProviderException {
		for (Entry<String, ValueProvider<?>> subProvider : properties.entrySet()) {
			setField(result, subProvider.getKey(), subProvider.getValue());
		}
	}

	private void setField(T result, String fieldName, ValueProvider<?> value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, ValueProviderException {
		Field field = result.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(result, value.provide());
	}

}
