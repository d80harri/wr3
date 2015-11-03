package net.d80harri.wr.db.model;

public abstract class WrEntity {
	private Integer id;
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
}
