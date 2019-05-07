package com.example.mvvmdemo.repositories;

import androidx.lifecycle.LiveData;

import com.example.mvvmdemo.livedata.RxLiveData;
import com.example.mvvmdemo.utils.LiveDataUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Repository 层，只负责请求数据，不应该含有业务逻辑
 *
 * @author funnywolf
 * @since 2019-04-22
 */
public enum RandomNumberRepository {
    /**
     * 实例
     */
    INSTANCE;

    private final RxLiveData<Integer, StateData<Integer>> mLeftNumberLiveData = RxLiveData.simpleStateData();
    private final RxLiveData<Integer, StateData<Integer>> mRightNumberLiveData = RxLiveData.simpleStateData();

    public LiveData<StateData<Integer>> getLeftNumberLiveData() { return mLeftNumberLiveData; }
    public LiveData<StateData<Integer>> getRightNumberLiveData() { return mRightNumberLiveData; }

    public void updateLeft() {
        disposeLeft();
        LiveDataUtils.update(mLeftNumberLiveData, StateData.loading());
        Observable.timer((long) (Math.random() * 500 + 500), TimeUnit.MILLISECONDS)
                .map(it -> {
                    if (Math.random() > 0.2) {
                        return (int) (Math.random() * 10 + 0);
                    } else {
                        throw new Exception("Something wrong!!!");
                    }
                })
                .subscribe(mLeftNumberLiveData);
    }

    public void disposeLeft() {
        mLeftNumberLiveData.dispose();
    }

    public void clearLeft() {
        disposeLeft();
        LiveDataUtils.update(mLeftNumberLiveData, StateData.ready(null));
    }

    public void updateRight() {
        disposeRight();
        LiveDataUtils.update(mRightNumberLiveData, StateData.loading());
        Observable.timer((long) (Math.random() * 500 + 500), TimeUnit.MILLISECONDS)
                .map(it -> {
                    if (Math.random() > 0.2) {
                        return (int) (Math.random() * 10 + 10);
                    } else {
                        throw new Exception("Something wrong!!!");
                    }
                })
                .subscribe(mRightNumberLiveData);
    }

    public void disposeRight() {
        mRightNumberLiveData.dispose();
    }

    public void clearRight() {
        disposeRight();
        LiveDataUtils.update(mRightNumberLiveData, StateData.ready(null));
    }
}
