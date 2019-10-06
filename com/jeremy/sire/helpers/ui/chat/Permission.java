package com.jeremy.sire.helpers.ui.chat;

public class Permission {

	public String string;

	public Permission(String string) {
		this.string = string.toLowerCase();
	}

	private static boolean stringMatch(String permissionString1, String permissionString2) {
		String[] pieces1 = permissionString1.split("\\.");
		String[] pieces2 = permissionString2.split("\\.");
		for (int i = 0, len = Math.max(pieces1.length, pieces2.length); i < len; i++) {
			int i1 = Math.min(pieces1.length - 1, i);
			int i2 = Math.min(pieces2.length - 1, i);
			if (i1 != i2) {
				if (i1 < i2) {
					if (!pieces1[i1].equals("*") && !(i2 - i1 == 1 && pieces2[i2].equals("*"))) return false;
				} else {
					if (!pieces2[i2].equals("*") && !(i1 - i2 == 1 && pieces1[i1].equals("*"))) return false;
				}
			} else {
				if (!pieces1[i].equals(pieces2[i])) {
					if (!pieces1[i].equals("*") && !pieces2[i].equals("*")) {
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean equals(Object permission) {
		if (permission instanceof Permission) {
			return stringMatch(string, ((Permission) permission).string);
		} else if (permission instanceof String) {
			return stringMatch(string, (String) permission);
		}
		return false;
	}

	@Override
	public String toString() {
		return string;
	}

}
