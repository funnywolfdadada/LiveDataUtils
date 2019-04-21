package com.example.mvvmdemo.repositories;

public class StateData<T> {
    public static final int STATE_READY = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;

    public final T data;
    public final int state;
    public final Throwable error;

    private StateData(T data, int state, Throwable error) {
        this.data = data;
        this.state = state;
        this.error = error;
    }

    public static <T> StateData<T> ready(T data) {
        return new StateData<>(data, STATE_READY, null);
    }

    public static <T> StateData<T> loading() {
        return new StateData<>(null, STATE_LOADING, null);
    }

    public static <T> StateData<T> loading(Class<T> clazz) {
        return new StateData<>(null, STATE_LOADING, null);
    }

    public static <T> StateData<T> error(Throwable error) {
        return new StateData<>(null, STATE_ERROR, error);
    }

    public static <T> StateData<T> error(Class<T> clazz, Throwable error) {
        return new StateData<>(null, STATE_ERROR, error);
    }

}
