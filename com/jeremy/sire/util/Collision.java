package com.jeremy.sire.util;

public class Collision {

	public static final boolean isRectangleCollide1(double x1, double y1, double width1, double height1, double x2, double y2, double width2,
			double height2) {
		return isRectangleCollide2(x1 - width1 / 2, x1 + width1 / 2, y1 + height1 / 2, y1 - height1 / 2, x2 - width2 / 2, x2 + width2 / 2,
				y2 + height2 / 2, y2 - height2 / 2);
	}

	public static final boolean isRectangleCollide2(double left1, double right1, double top1, double bottom1, double left2, double right2,
			double top2, double bottom2) {
		return left2 < right1 && left1 < right2 && bottom2 < top1 && bottom1 < top2;
	}

}
