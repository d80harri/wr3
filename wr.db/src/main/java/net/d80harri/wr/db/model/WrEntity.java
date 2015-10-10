package net.d80harri.wr.db.model;

public class WrEntity {
	private Integer id;
	
	@SuppressWarnings("unused")
	private void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
}
