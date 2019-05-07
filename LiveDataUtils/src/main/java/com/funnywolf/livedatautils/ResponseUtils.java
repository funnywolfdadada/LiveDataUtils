package com.funnywolf.livedatautils;

import androidx.annotation.NonNull;

import retrofit2.Response;

/**
 * @author funnywolf
 * @since 2019-05-07
 */
public class ResponseUtils {
    /**
     * @param response 请求的 response
     * @return 成功返回 response.body()，失败返回 null
     */
    public static <T> T getBodyIfSuccess(Response<T> response) {
        return (response != null && response.isSuccessful()) ? response.body() : null;
    }

    /**
     * @param response 请求的 response
     * @return 成功返回 STATE_READY 的 StateData(response.body())，失败返回 STATE_ERROR 的 StateData
     */
    public static <T> StateData<T> getStateDataFromResponse(Response<T> response) {
        T body = getBodyIfSuccess(response);
        if (body != null) {
            return StateData.ready(body);
        }
        return StateData.error(new Exception("Response error"));
    }

    /**
     * @param response 请求的 response
     * @throws Exception 会在 response 为空、失败和 body 为空时抛出异常
     * @return 返回 response.body()
     */
    @NonNull
    public static <T> T getOrError(@NonNull Response<T> response) throws Exception {
        if (!response.isSuccessful()) {
            throw new Exception("Response error");
        }
        if (response.body() == null) {
            throw new IllegalArgumentException("解析出为 null");
        }
        return response.body();
    }
}
