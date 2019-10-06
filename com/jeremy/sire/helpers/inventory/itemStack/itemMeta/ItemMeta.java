package com.jeremy.sire.helpers.inventory.itemStack.itemMeta;

import java.util.ArrayList;

public class ItemMeta {

	private String displayName;
	private ArrayList<String> lore;

	public ItemMeta(String displayName, ArrayList<String> lore) {
		this.displayName = displayName;
		this.lore = lore;
	}

	public boolean hasDisplayName() {
		return displayName != null;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public boolean hasLore() {
		return lore != null;
	}

	public ArrayList<String> getLore() {
		return lore;
	}

	public void setLore(int line, String text) {
		if (hasLore()) {
			while (getLore().size() <= line) {
				getLore().add("");
			}
		} else {
			lore = new ArrayList<String>(line + 1);
		}
		getLore().set(line, text);
	}

	public void setLore(ArrayList<String> lore) {
		this.lore = lore;
	}

}
