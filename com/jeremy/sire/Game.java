package com.jeremy.sire;

import com.jeremy.sire.assets.AssetManager;
import com.jeremy.sire.graphics.GameWindow;
import com.jeremy.sire.graphics.Renderer;
import com.jeremy.sire.helpers.Controls;
import com.jeremy.sire.helpers.ui.chat.ConsoleSender;
import com.jeremy.sire.input.KeyInput;
import com.jeremy.sire.input.MouseInput;
import com.jeremy.sire.sound.SoundManager;
import com.jeremy.sire.state.StateManager;

public abstract class Game {

	private String name, version;

	private int width, height;

	private long age;
	private int maxFps;
	private int tps;
	private double currentTps, currentFps;

	private boolean running;

	private GameWindow window;
	private Renderer renderer;
	private ConsoleSender sender;

	private StateManager stateManager;
	private AssetManager assetManager;
	private SoundManager soundManager;
	private MouseInput mouseInput;
	private KeyInput keyInput;
	private Controls controls;

	public Game(String name, String version, int width, int height, int tps, int maxFps) {
		this.name = name;
		this.version = version;
		this.width = width;
		this.height = height;
		this.maxFps = maxFps;
		this.tps = tps;

		renderer = new Renderer(width, height);
		window = new GameWindow(name, renderer);
		sender = new ConsoleSender();
		stateManager = new StateManager();
		assetManager = new AssetManager();
		soundManager = new SoundManager(assetManager);
		mouseInput = new MouseInput(window);
		keyInput = new KeyInput(window);
		controls = new Controls(keyInput);
	}

	public Game(String name, String version, int width, int height) {
		this(name, version, width, height, 60, 60);
	}

	public final void start() {
		if (running) {
			throw new IllegalStateException("attempted to start a game that was already running");
		}
		running = true;
		run();
	}

	private final void run() {
		initiate();

		final int ups = 4;
		final long second = 1000000000;

		long currentTime;
		int ticks = 0;
		int frames = 0;
		long tickTime = second / getTps();
		long tickTimer = System.nanoTime();
		long updateTime = second / ups;
		long updateTimer = System.nanoTime();
		long frameTime = getMaxFps() == 0 ? 0 : second / getMaxFps();
		long frameTimer = System.nanoTime();
		long elapsedTime;
		while (running) {
			currentTime = System.nanoTime();
			if (currentTime - tickTimer > tickTime) {
				if (currentTime - tickTimer > tickTime * 2) {
					tickTimer = currentTime;
				}
				tickTimer += tickTime;
				ticks++;
				age++;
				tick();
			}
			if (currentTime - frameTimer > frameTime) {
				frameTimer += frameTime;
				renderer.beginRender();
				stateManager.renderCurrentState(renderer);
				renderer.endRender();
				frames++;
			}
			elapsedTime = currentTime - updateTimer;
			if (elapsedTime > updateTime) {
				currentTps = (double) ticks * second / elapsedTime;
				currentFps = (double) frames * second / elapsedTime;
				updateTimer = currentTime;
				frames = ticks = 0;
			}
		}

		terminate();
	}

	protected void initiate() {
		window.pack();
		window.center();
		window.setVisible(true);
	}

	protected void tick() {
		stateManager.tickCurrentState();
	}

	protected void render(Renderer renderer) {

	}

	protected void terminate() {
		window.dispose();
	}

	public final void stop() {
		if (!running) {
			throw new IllegalStateException("attempted to stop game that was not running");
		}
		running = false;
	}

	public int getMaxFps() {
		return maxFps;
	}

	public void setMaxFps(int maxFps) {
		this.maxFps = maxFps;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public long getAge() {
		return age;
	}

	public int getTps() {
		return tps;
	}

	public double getCurrentTps() {
		return currentTps;
	}

	public double getCurrentFps() {
		return currentFps;
	}

	public boolean isRunning() {
		return running;
	}

	public GameWindow getWindow() {
		return window;
	}

	public Renderer getRenderer() {
		return renderer;
	}

	public ConsoleSender getConsoleSender() {
		return sender;
	}

	public StateManager getStateManager() {
		return stateManager;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public SoundManager getSoundManager() {
		return soundManager;
	}

	public MouseInput getMouseInput() {
		return mouseInput;
	}

	public KeyInput getKeyInput() {
		return keyInput;
	}

	public Controls getControls() {
		return controls;
	}

}
