package com.jeremy.sire.state;

import java.util.LinkedList;

import com.jeremy.sire.Game;
import com.jeremy.sire.graphics.Renderer;
import com.jeremy.sire.helpers.ui.Component;
import com.jeremy.sire.input.KeyInput.KeyInputEvent;
import com.jeremy.sire.input.MouseInput.MouseInputEvent;

public class State {

	private final Game game;
	private StateManager manager;
	private final LinkedList<Component> components;
	private final LinkedList<Layer> layers;

	public State(Game game) {
		this.game = game;
		manager = game.getStateManager();
		components = new LinkedList<Component>();
		layers = new LinkedList<Layer>();
		game.getKeyInput().addKeyEventCallback(event -> {
			if (manager.getCurrentState() == this) keyEvent(event);
		});
		game.getMouseInput().addMouseCallback((event) -> {
			if (manager.getCurrentState() == this) mouseEvent(event);
		});
	}

	protected void keyEvent(KeyInputEvent event) {
		components.forEach(component -> component.keyEvent(event));
	}

	protected void mouseEvent(MouseInputEvent event) {
		components.forEach(component -> component.mouseEvent(event));
	}

	public void tick() {
		components.forEach(component -> component.tick());
		layers.forEach(layer -> layer.tick());
	}

	public void render(Renderer renderer) {
		renderComponents(renderer);
		renderLayers(renderer);
	}

	protected void renderComponents(Renderer renderer) {
		components.forEach(component -> component.render(renderer));
	}

	protected void renderLayers(Renderer renderer) {
		layers.forEach(layer -> layer.render(renderer));
	}

	public void enter() {
		if (manager.getCurrentState() != null) {
			manager.getCurrentState().exit();
		}
		manager.setCurrentState(this);
	}

	protected void exit() {

	}

	public Game getGame() {
		return game;
	}

	public LinkedList<Layer> getLayers() {
		return layers;
	}

	public void addLayer(Layer layer) {
		layers.add(layer);
	}

	public void removeLayer(Layer layer) {
		layers.remove(layer);
	}

}
