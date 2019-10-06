package com.jeremy.sire.helpers.ui;

import com.jeremy.sire.Game;
import com.jeremy.sire.graphics.Renderer;
import com.jeremy.sire.helpers.tools.Anchor;
import com.jeremy.sire.input.MouseInput.MouseAction;
import com.jeremy.sire.input.MouseInput.MouseInputEvent;

public class Button extends Component {

	private String text;
	private float textSize;
	private Anchor textAnchor;
	private Runnable action;

	public Button(Game game, Anchor anchor, float x, float y, float width, float height, String text, Anchor textAnchor, float textSize) {
		super(game, anchor, x, y, width, height);
		this.text = text;
		this.textAnchor = textAnchor;
		this.textSize = textSize;
	}

	public Button(Game game, Anchor anchor, float x, float y, float height, String text) {
		this(game, anchor, x, y, game.getRenderer().getTextWidthAsScreenPercentage(text), height, text, Anchor.CENTER, 0.1f);
	}

	@Override
	public void mouseEvent(MouseInputEvent event) {
		super.mouseEvent(event);
		if (event.action == MouseAction.PRESS) if (action != null) action.run();
	}

	@Override
	protected void renderContent(Renderer renderer) {
		super.renderContent(renderer);
		renderer.setFontHeight(textSize);
		if (textAnchor == Anchor.CENTER) {
			renderer.drawText(text, getBounds().getCenterX(), getBounds().getCenterY(), Anchor.CENTER);
		} else if (textAnchor == Anchor.NORTH) {
			renderer.drawText(text, getBounds().getCenterX(), getBounds().getTop(), Anchor.NORTH);
		} else if (textAnchor == Anchor.NORTH_EAST) {
			renderer.drawText(text, getBounds().getLeft(), getBounds().getTop(), Anchor.NORTH_EAST);
		} else if (textAnchor == Anchor.EAST) {
			renderer.drawText(text, getBounds().getRight(), getBounds().getCenterY(), Anchor.EAST);
		} else if (textAnchor == Anchor.SOUTH_EAST) {
			renderer.drawText(text, getBounds().getRight(), getBounds().getBottom(), Anchor.SOUTH_EAST);
		} else if (textAnchor == Anchor.SOUTH) {
			renderer.drawText(text, getBounds().getCenterX(), getBounds().getBottom(), Anchor.SOUTH);
		} else if (textAnchor == Anchor.SOUTH_WEST) {
			renderer.drawText(text, getBounds().getLeft(), getBounds().getBottom(), Anchor.SOUTH_WEST);
		} else if (textAnchor == Anchor.WEST) {
			renderer.drawText(text, getBounds().getLeft(), getBounds().getCenterX(), Anchor.WEST);
		} else if (textAnchor == Anchor.NORTH_WEST) {
			renderer.drawText(text, getBounds().getLeft(), getBounds().getTop(), Anchor.NORTH_WEST);
		}
	}

	public Runnable getAction() {
		return action;
	}

	public void setAction(Runnable action) {
		this.action = action;
	}

}
