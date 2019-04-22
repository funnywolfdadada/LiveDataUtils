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

    private final RxLiveData<Integer, StateData<Integer>> mTopNumberLiveData = RxLiveData.simpleStateData();
    private final RxLiveData<Integer, StateData<Integer>> mBottomNumberLiveData = RxLiveData.simpleStateData();
    private final RxLiveData<Integer, StateData<Integer>> mLeftNumberLiveData = RxLiveData.simpleStateData();
    private final RxLiveData<Integer, StateData<Integer>> mRightNumberLiveData = RxLiveData.simpleStateData();

    public LiveData<StateData<Integer>> getTopNumberLiveData() { return mTopNumberLiveData; }
    public LiveData<StateData<Integer>> getBottomNumberLiveData() { return mBottomNumberLiveData; }
    public LiveData<StateData<Integer>> getLeftNumberLiveData() { return mLeftNumberLiveData; }
    public LiveData<StateData<Integer>> getRightNumberLiveData() { return mRightNumberLiveData; }

    public void updateTop() {
        disposeTop();
        LiveDataUtils.update(mTopNumberLiveData, StateData.loading());
        Observable.timer((long) (Math.random() * 500 + 500), TimeUnit.MILLISECONDS)
                .map(it -> {
                    if (Math.random() > 0.2) {
                        return (int) (Math.random() * 10);
                    } else {
                        throw new Exception("Something wrong!!!");
                    }
                })
                .subscribe(mTopNumberLiveData);
    }

    public void disposeTop() {
        mTopNumberLiveData.dispose();
    }

    public void updateBottom() {
        disposeBottom();
        LiveDataUtils.update(mBottomNumberLiveData, StateData.loading());
        Observable.timer((long) (Math.random() * 500 + 500), TimeUnit.MILLISECONDS)
                .map(it -> {
                    if (Math.random() > 0.2) {
                        return (int) (Math.random() * 10 + 10);
                    } else {
                        throw new Exception("Something wrong!!!");
                    }
                })
                .subscribe(mBottomNumberLiveData);
    }

    public void disposeBottom() {
        mBottomNumberLiveData.dispose();
    }

    public void updateLeft() {
        disposeLeft();
        LiveDataUtils.update(mLeftNumberLiveData, StateData.loading());
        Observable.timer((long) (Math.random() * 500 + 500), TimeUnit.MILLISECONDS)
                .map(it -> {
                    if (Math.random() > 0.2) {
                        return (int) (Math.random() * 10 + 20);
                    } else {
                        throw new Exception("Something wrong!!!");
                    }
                })
                .subscribe(mLeftNumberLiveData);
    }

    public void disposeLeft() {
        mLeftNumberLiveData.dispose();
    }

    public void updateRight() {
        disposeRight();
        LiveDataUtils.update(mRightNumberLiveData, StateData.loading());
        Observable.timer((long) (Math.random() * 500 + 500), TimeUnit.MILLISECONDS)
                .map(it -> {
                    if (Math.random() > 0.2) {
                        return (int) (Math.random() * 10 + 30);
                    } else {
                        throw new Exception("Something wrong!!!");
                    }
                })
                .subscribe(mRightNumberLiveData);
    }

    public void disposeRight() {
        mRightNumberLiveData.dispose();
    }

}
