package com.jeremy.sire.helpers.inventory;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.jeremy.sire.Game;
import com.jeremy.sire.graphics.Renderer;
import com.jeremy.sire.helpers.inventory.itemStack.ItemStack;
import com.jeremy.sire.helpers.tools.Anchor;
import com.jeremy.sire.input.MouseInput;
import com.jeremy.sire.input.MouseInput.MouseAction;

public abstract class Inventory {

	public static float slotSize = 0.05f;

	protected final Game game;

	private InventoryHolder holder;
	private float x, y;
	private int width, height;
	private ItemStack[] items;

	private BufferedImage texture;
	private BufferedImage hoverTexture;

	private ItemStack cursor;

	public Inventory(Game game, InventoryHolder holder, int width, int height) {
		this.game = game;
		this.holder = holder;
		this.x = 0.5f - ((float) width / 2 * slotSize);
		this.y = 0.5f - ((float) height * slotSize);
		this.width = width;
		this.height = height;
		items = new ItemStack[width * height];

		final MouseInput mouseInput = game.getMouseInput();
		mouseInput.addMouseCallback(event -> {
			if (event.action == MouseAction.PRESS) {
				final Renderer renderer = game.getRenderer();
				final float ratio = renderer.getAspectRatio();
				for (int i = 0; i < height; i++) {
					for (int j = 0; j < width; j++) {
						int slot = i * getWidth() + j;
						float x = getX() + j * slotSize;
						float y = getY() + (i * slotSize) * ratio;
						float mx = (float) event.x / renderer.getWidth();
						float my = (float) event.y / renderer.getHeight();
						boolean hover = mx > x && mx <= x + slotSize && my > y && my <= y + slotSize * ratio;
						if (hover && event.button == MouseInput.MOUSE_LEFT) {
							leftClick(slot);
						} else if (hover && event.button == MouseInput.MOUSE_RIGHT) {
							rightClick(slot);
						}
					}
				}
			} else if (event.action == MouseAction.DOUBLE_CLICK) {
				for (int i = 0; i < getItems().length; i++) {
					ItemStack itemStack = getItem(i);
					if (itemStack != null) {
						if (itemStack.equals(cursor)) {
							cursor.setAmount(cursor.getAmount() + itemStack.getAmount());
							setItem(i, null);
						}
					}
				}
			}
		});
	}

	public ItemStack[] getItems() {
		return items;
	}

	public ItemStack getItem(int slot) {
		return getItems()[slot];
	}

	public void setItems(ItemStack[] items) {
		this.items = items;
	}

	public ItemStack setItem(int slot, ItemStack item) {
		ItemStack before = items[slot];
		items[slot] = item;
		return before;
	}

	public int getSize() {
		return items.length;
	}

	public void tick() {
	}

	public void render(Renderer renderer) {
		final float ratio = renderer.getAspectRatio();
		final MouseInput mouseInput = game.getMouseInput();
		float mx = mouseInput.getXPercent(renderer.getWidth());
		float my = mouseInput.getYPercent(renderer.getHeight());
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				float x = this.x + j * slotSize;
				float y = this.y + (i * slotSize) * ratio;
//				renderer.setColor(Color.GRAY);
//				renderer.traceRectangle(x, y, slotSize, slotSize * ratio);
				ItemStack itemStack = getItem(i * width + j);
				if (itemStack != null) {
					renderItemStack(renderer, x, y, itemStack);
				}
			}
		}
		if (cursor != null) {
			renderItemStack(renderer, mx - slotSize / 2, my - slotSize / 2, cursor);
		} else {
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					float x = this.x + j * slotSize;
					float y = this.y + (i * slotSize) * ratio;
					ItemStack itemStack = getItem(i * width + j);
					boolean hover = mx > x && mx <= x + slotSize && my > y && my <= y + slotSize * ratio;
					if (cursor == null && itemStack != null && hover) {
						String name = itemStack.getDisplayName();
						float width = renderer.getTextWidthAsScreenPercentage(name);
						float height = renderer.getTextHeightAsScreenPercentage();
						x = mx - width;
						y = my - height;
						renderer.setColor(Color.BLACK);
						renderer.fillRectangle(x, y, width, height);
						renderer.setColor(Color.WHITE);
						renderer.drawText(name, x, y, Anchor.NORTH_WEST);
					}
				}
			}
		}
	}

	protected void renderItemStack(Renderer renderer, float x, float y, ItemStack itemStack) {
		renderer.drawImage(itemStack.getType().getTexture(), x, y, slotSize, slotSize * renderer.getAspectRatio());
		if (itemStack.getAmount() == 1) {
			return;
		}
		renderer.setColor(Color.WHITE);
		renderer.setFontHeight(0.025f);
		renderer.drawText(String.valueOf(itemStack.getAmount()), x + slotSize / 8, y + slotSize * renderer.getAspectRatio() - slotSize / 8);
	}

	private void leftClick(int slot) {
		ItemStack slotItem = getItem(slot);
		if (cursor != null && slotItem != null) {
			if (slotItem.equals(cursor)) {
				slotItem.setAmount(slotItem.getAmount() + cursor.getAmount());
				cursor = null;
				return;
			}
		}
		cursor = setItem(slot, cursor);
	}

	private void rightClick(int slot) {
		if (cursor == null && getItem(slot) == null) {
			return;
		}
		ItemStack slotItem = getItem(slot);
		if (cursor == null && slotItem != null) {
			int remainingAmount = Math.floorDiv(slotItem.getAmount(), 2);
			int cursorAmount = slotItem.getAmount() - remainingAmount;
			slotItem.setAmount(remainingAmount);
			if (remainingAmount <= 0)
				setItem(slot, null);
			cursor = slotItem.clone();
			cursor.setAmount(cursorAmount);
		} else if (cursor != null && slotItem == null) {
			slotItem = cursor.clone();
			slotItem.setAmount(1);
			cursor.setAmount(cursor.getAmount() - 1);
			if (cursor.getAmount() <= 0)
				cursor = null;
			setItem(slot, slotItem);
		} else {
			if (cursor.equals(slotItem)) {
				slotItem.setAmount(slotItem.getAmount() + 1);
				cursor.setAmount(cursor.getAmount() - 1);
				if (cursor.getAmount() <= 0)
					cursor = null;
			} else {
				leftClick(slot);
			}
		}
	}

	public BufferedImage getTexture() {
		return texture;
	}

	public BufferedImage getHoverTexture() {
		return hoverTexture;
	}

	public void setTexture(BufferedImage texture) {
		this.texture = texture;
	}

	public void setHoverTexture(BufferedImage hoverTexture) {
		this.hoverTexture = hoverTexture;
	}

	public InventoryHolder getHolder() {
		return holder;
	}

	public float getX() {
		return x;
	}

	protected void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	protected void setY(float y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	protected void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	protected void setHeight(int height) {
		this.height = height;
	}

}
