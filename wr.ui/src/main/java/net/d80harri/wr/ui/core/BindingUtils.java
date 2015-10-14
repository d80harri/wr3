package net.d80harri.wr.ui.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.beans.property.Property;

public class BindingUtils {
	private List<Entry> bindings = new ArrayList<>();
	
	public <T> void bindBidirectional(Property<T> p1,
			Property<T> p2) {
		bindings.add(new Entry(p1, p2));
		p1.bindBidirectional(p2);
	}

	public void unbindAll( ){
		Iterator<Entry> it = bindings.iterator();
		
		while (it.hasNext()) {
			Entry entry = it.next();
			entry.p1.unbindBidirectional(entry.p2);
			it.remove();
		}
	}
	
	private static class Entry {
		public Property p1, p2;
		
		public Entry(Property p1, Property p2) {
			this.p1 = p1;
			this.p2 = p2;
		}
	}
}
