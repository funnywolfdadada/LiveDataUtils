package com.example.mvvmdemo.utils;

import android.os.Looper;

import androidx.lifecycle.MutableLiveData;

/**
 * @author funnywolf
 * @since 2019-04-22
 */
public class LiveDataUtils {

    /**
     * 更新 LiveData，如果在主线程就用 setValue 否则用 postValue
     */
    public static <T> void update(MutableLiveData<T> mutableLiveData, T data) {
        if (mutableLiveData == null) { return; }
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            mutableLiveData.setValue(data);
        } else {
            mutableLiveData.postValue(data);
        }
    }
}
