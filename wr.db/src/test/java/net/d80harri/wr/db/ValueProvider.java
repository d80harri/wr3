package net.d80harri.wr.db;


public interface ValueProvider<T> {
	T provide() throws ValueProviderException;
}
