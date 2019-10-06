package com.jeremy.sire.state;

import java.util.HashMap;

import com.jeremy.sire.graphics.Renderer;

public class StateManager {

	private State currentState;

	private HashMap<Class<? extends State>, State> states;

	public StateManager() {
		states = new HashMap<Class<? extends State>, State>();
	}

	public void registerState(State state) {
		states.put(state.getClass(), state);
		if (currentState == null) {
			state.enter();
		}
	}

	public <T extends State> T getState(Class<T> stateClass) {
		return stateClass.cast(states.get(stateClass));
	}

	public <T extends State> T enterState(Class<T> stateClass) {
		T state = getState(stateClass);
		state.enter();
		return state;
	}

	public State getCurrentState() {
		return currentState;
	}

	void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public void tickCurrentState() {
		getCurrentState().tick();
	}

	public void renderCurrentState(Renderer renderer) {
		getCurrentState().render(renderer);
	}

}
