package com.example.mvvmdemo.repositories;

/**
 * 带有状态的数据包装类
 *
 * @author funnywolf
 * @since 2019-04-22
 */
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

    /**
     * 构造 STATE_READY 的 data，error 为空，data 最好不要为 null
     */
    public static <T> StateData<T> ready(T data) {
        return new StateData<>(data, STATE_READY, null);
    }

    /**
     * 构造 STATE_LOADING 的 data，data 和 error 都为 null
     */
    public static <T> StateData<T> loading() {
        return new StateData<>(null, STATE_LOADING, null);
    }

    /**
     * 构造 STATE_ERROR 的 data，data 为 null，error 为出错信息
     */
    public static <T> StateData<T> error(Throwable error) {
        return new StateData<>(null, STATE_ERROR, error);
    }

    /**
     * @return 是否 ready
     */
    public static <T> boolean isReady(StateData<T> stateData) {
        return stateData != null && stateData.state == STATE_READY;
    }

    /**
     * @return ready 且数据非空
     */
    public static <T> boolean isOk(StateData<T> stateData) {
        return isReady(stateData) && stateData.data != null;
    }

    /**
     * @return STATE_ERROR
     */
    public static <T> boolean isError(StateData<T> stateData) {
        return stateData != null && stateData.state == STATE_ERROR;
    }

    /**
     * @return ready 返回 data，否则返回 null
     */
    public static <T> T getDataIfReady(StateData<T> stateData) {
        return isReady(stateData) ? stateData.data : null;
    }
}
