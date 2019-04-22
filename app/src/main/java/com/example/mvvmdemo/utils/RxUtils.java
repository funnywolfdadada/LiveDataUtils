package com.example.mvvmdemo.utils;

import io.reactivex.disposables.Disposable;

/**
 * @author funnywolf
 * @since 2019-04-22
 */
public class RxUtils {

    /**
     * 安全地 dispose RxJava 链
     */
    public static void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
