package com.funnywolf.livedatautils;

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

    private StateData(T data, int status, Throwable error) {
        this.data = data;
        this.state = status;
        this.error = error;
    }

    /**
     * data 最好不要为空，state = STATE_READY 的 StateData
     */
    public static <T> StateData<T> ready(T data) {
        return new StateData<>(data, STATE_READY, null);
    }

    /**
     * STATE_LOADING 的 StateData，data 为 null，也不需要额外的 error 信息
     */
    public static <T> StateData<T> loading() {
        return new StateData<>(null, STATE_LOADING, null);
    }

    /**
     * STATE_LOADING 的 StateData，不需要额外的 error 信息
     */
    public static <T> StateData<T> loading(T data) {
        return new StateData<>(data, STATE_LOADING, null);
    }

    /**
     * STATE_ERROR 的 StateData，data 为 null，有额外的 error 信息
     */
    public static <T> StateData<T> error(Throwable e) {
        return new StateData<>(null, STATE_ERROR, e);
    }

    /**
     * STATE_ERROR 的 StateData，有额外的 error 信息
     */
    public static <T> StateData<T> error(T data, Throwable e) {
        return new StateData<>(data, STATE_ERROR, e);
    }

    /**
     * @return 是否 ready
     */
    public static boolean isReady(StateData stateData) {
        return stateData != null && stateData.state == STATE_READY;
    }

    /**
     * @return ready 且数据非空
     */
    public static boolean isOk(StateData stateData) {
        return isReady(stateData) && stateData.data != null;
    }

    /**
     * @return ready 返回 data，否则返回 null
     */
    public static <T> T getDataIfReady(StateData<T> stateData) {
        return isReady(stateData) ? stateData.data : null;
    }

    public static boolean isLoading(StateData stateData) {
        return stateData != null && stateData.state == STATE_LOADING;
    }

    public static boolean isError(StateData stateData) {
        return stateData != null && stateData.state == STATE_ERROR;
    }

}
