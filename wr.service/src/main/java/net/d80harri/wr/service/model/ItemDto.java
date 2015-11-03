package net.d80harri.wr.service.model;

public class ItemDto extends WrDto {
	private String title;
	
	public ItemDto() {
	}
	
	public ItemDto(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
