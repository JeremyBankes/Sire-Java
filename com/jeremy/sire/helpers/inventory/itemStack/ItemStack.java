package com.jeremy.sire.helpers.inventory.itemStack;

import com.jeremy.sire.helpers.inventory.itemStack.itemMeta.ItemMeta;

public class ItemStack {

	private int amount;
	private ItemType type;
	private ItemMeta meta;

	private String alternateName;

	public ItemStack(int amount, ItemType type) {
		this.amount = amount;
		this.type = type;
		this.meta = new ItemMeta(null, null);
	}

	public ItemStack(ItemStack clone) {
		this.amount = clone.amount;
		this.type = clone.type;
		this.meta = clone.meta;
	}

	public String getDisplayName() {
		if (meta.hasDisplayName()) {
			return meta.getDisplayName();
		} else {
			if (alternateName == null) {
				StringBuffer buffer = new StringBuffer();
				char[] characters = type.getClass().getSimpleName().toCharArray();
				for (int i = 0; i < characters.length; i++) {
					char character = characters[i];
					if (i != 0 && Character.isUpperCase(character)) {
						buffer.append(' ');
					}
					buffer.append(character);
				}
				alternateName = buffer.toString();
			}
			return alternateName;
		}
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public ItemType getType() {
		return type;
	}

	public ItemMeta getMeta() {
		return meta;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj.getClass() != ItemStack.class) return false;
		ItemStack itemStack = (ItemStack) obj;
		return itemStack.getType().equals(type) && itemStack.getMeta().equals(meta);
	}

	@Override
	public ItemStack clone() {
		return new ItemStack(this);
	}

}
