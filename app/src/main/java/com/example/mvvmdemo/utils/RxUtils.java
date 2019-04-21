package com.example.mvvmdemo.utils;

import io.reactivex.disposables.Disposable;

public class RxUtils {
    public static void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
