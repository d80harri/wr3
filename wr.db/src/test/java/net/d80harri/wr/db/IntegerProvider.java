package net.d80harri.wr.db;

import java.util.Random;

public class IntegerProvider implements ValueProvider<Integer> {
	private Random random;
	
	public Integer provide() {
		return random.nextInt();
	}

}
