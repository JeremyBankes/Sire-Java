package com.jeremy.sire.state;

import com.jeremy.sire.graphics.Renderer;

public abstract class Layer {

	public abstract void tick();

	public abstract void render(Renderer renderer);

}
