package net.d80harri.wr.db;

import java.util.UUID;

public class StringProvider implements ValueProvider<String> {

	public String provide() {
		return UUID.randomUUID().toString();
	}

}
