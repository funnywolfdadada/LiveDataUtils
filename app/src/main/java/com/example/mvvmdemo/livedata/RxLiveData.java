package com.example.mvvmdemo.livedata;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvmdemo.repositories.StateData;
import com.example.mvvmdemo.utils.LiveDataUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * 为了简化 RxJava 与 LiveData 结合时样板代码过多的问题，继承自 MutableLiveData，实现了 Disposable 和
 * Observer 接口，并封装了 RxJava 的 Observer、Disposable 和更新 LiveData 等相关操作
 * 泛型 IN 为要观察的数据类型，OUT 为 LiveData 的数据类型
 *
 * @author funnywolf
 * @since 2019-04-22
 */
public class RxLiveData<IN, OUT> extends MutableLiveData<OUT> implements Disposable, Observer<IN> {
    private static final String TAG = "RxLiveData";

    private Disposable mDisposable;
    private final Object mDisposableLock = new Object();

    /**
     * 在 onNext 处理 IN 转 OUT 的工作
     */
    private final Function<IN, OUT> mOnNextMap;
    /**
     * 在 onError 处理 LiveData 要更新的值
     */
    private final Function<Throwable, OUT> mOnErrorMap;

    /**
     * 订阅时需要关掉之前的订阅，然后保留新的 Disposable
     */
    @Override
    public void onSubscribe(Disposable d) {
        disposeAndSet(d);
    }

    /**
     * onNext 时用 {@link ##mOnNextMap} 的值来更新 LiveData
     */
    @Override
    public void onNext(IN in) {
        if (isDisposed()) { return; }
        try {
            if (mOnNextMap != null) {
                LiveDataUtils.update(this, mOnNextMap.apply(in));
            }
        } catch (Throwable throwable) {
            // onNext 抛异常也走 onError
            onError(throwable);
        }
    }

    /**
     * onError 时用 {@link ##mOnErrorMap} 的值来更新 LiveData
     */
    @Override
    public void onError(Throwable error) {
        try {
            if (mOnErrorMap != null) {
                LiveDataUtils.update(this, mOnErrorMap.apply(error));
            }
            // 打印 error 日志
            Log.e(TAG, "onError: " + error);
        } catch (Throwable throwable) {
            Log.e(TAG, "onError: " + throwable);
        }
    }

    @Override
    public void onComplete() { }

    /**
     * @param onNextMap onNext 的回调处理，为空时不执行
     * @param onErrorMap onError 的回调处理，为空时不执行
     */
    private RxLiveData(@Nullable Function<IN, OUT> onNextMap, @Nullable Function<Throwable, OUT> onErrorMap) {
        mOnNextMap = onNextMap;
        mOnErrorMap = onErrorMap;
    }

    /**
     * 关闭订阅并清空数据
     */
    public void clear() {
        dispose();
        LiveDataUtils.update(this, null);
    }

    /**
     * 关闭之前的订阅，并设置新的 Disposable
     */
    private void disposeAndSet(Disposable d) {
        synchronized (mDisposableLock) {
            if (!isDisposed()) {
                mDisposable.dispose();
            }
            mDisposable = d;
        }
    }

    /**
     * 关闭之前的订阅并置为空
     */
    @Override
    public void dispose() {
        disposeAndSet(null);
    }

    @Override
    public boolean isDisposed() {
        synchronized (mDisposableLock) {
            return mDisposable == null || mDisposable.isDisposed();
        }
    }

    /**
     * 构造简单的 RxLiveData，输入和输出都一样，失败更新为 null
     */
    public static <T> RxLiveData<T, T> simple() {
        return new RxLiveData<>(success -> success, null);
    }

    /**
     * 构造简单的 RxLiveData，输入和输出分别为 IN 和 OUT，失败更新为 null
     */
    public static <IN, OUT> RxLiveData<IN, OUT> simple(@NonNull Function<IN, OUT> function) {
        return new RxLiveData<>(function, null);
    }

    /**
     * 构造简单的 RxLiveData，输入是 T，输出都是 StateData，失败时构造一个 StateData.error()
     */
    public static <T> RxLiveData<T, StateData<T>> simpleStateData() {
        return new RxLiveData<>(StateData::ready, StateData::error);
    }

}