package com.funnywolf.livedatautils;

public class State extends TypeData {
    public static final int STATE_READY = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;

    public final int state;
    public final Throwable error;

    private State(int type, int state, Throwable error) {
        super(type);
        this.state = state;
        this.error = error;
    }

    public State loading(int type) {
        return new State(type, STATE_LOADING, null);
    }

    public State ready(int type) {
        return new State(type, STATE_READY, null);
    }

    public State error(int type, Throwable error) {
        return new State(type, STATE_ERROR, error);
    }
}
