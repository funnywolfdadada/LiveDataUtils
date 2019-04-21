package com.example.mvvmdemo.utils;

import android.os.Looper;

import androidx.lifecycle.MutableLiveData;

public class LiveDataUtils {
    public static <T> void update(MutableLiveData<T> mutableLiveData, T data) {
        if (mutableLiveData == null) { return; }
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            mutableLiveData.setValue(data);
        } else {
            mutableLiveData.postValue(data);
        }
    }
}
