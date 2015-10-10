package net.d80harri.wr.db.model;

public class ItemType extends WrEntity {
	private ItemTypeDiscriminator discriminator;

	public ItemTypeDiscriminator getDiscriminator() {
		return discriminator;
	}

	public void setDiscriminator(ItemTypeDiscriminator discriminator) {
		this.discriminator = discriminator;
	}

	public static enum ItemTypeDiscriminator {
		TASK, NOTE
	}
}
