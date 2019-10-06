package com.jeremy.sire.helpers.world.tile;

class LocationKey {

	private final int x;
	private final int y;

	public LocationKey(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof LocationKey)) {
			return false;
		}

		final LocationKey pair = (LocationKey) o;

		if (x != pair.x) {
			return false;
		}
		if (y != pair.y) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		return result;
	}

}
