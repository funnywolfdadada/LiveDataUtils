package com.example.mvvmdemo.viewmodels;

import com.example.mvvmdemo.repositories.StateData;
import com.example.mvvmdemo.utils.LiveDataUtils;
import com.example.mvvmdemo.utils.RxUtils;

import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class RandomNumberViewModel extends ViewModel {
    private Disposable mDisposeTop, mDisposeBottom, mDisposeLeft, mDisposeRight;

    private MutableLiveData<StateData<Integer>> mTopNumberLiveData = new MutableLiveData<>();
    private MutableLiveData<StateData<Integer>> mBottomNumberLiveData = new MutableLiveData<>();
    private MutableLiveData<StateData<Integer>> mLeftNumberLiveData = new MutableLiveData<>();
    private MutableLiveData<StateData<Integer>> mRightNumberLiveData = new MutableLiveData<>();

    public LiveData<StateData<Integer>> getTopNumberLiveData() { return mTopNumberLiveData; }

    public LiveData<StateData<Integer>> getBottomNumberLiveData() { return mBottomNumberLiveData; }

    public LiveData<StateData<Integer>> getLeftNumberLiveData() { return mLeftNumberLiveData; }

    public LiveData<StateData<Integer>> getRightNumberLiveData() { return mRightNumberLiveData; }

    public RandomNumberViewModel() {
        clear();
    }

    public void updateAll() {
        updateTop(true);
    }

    public void updateTop() { updateTop(false); }

    public void updateTop(boolean updateAll) {
        RxUtils.dispose(mDisposeTop);
        LiveDataUtils.update(mTopNumberLiveData, StateData.loading());
        mDisposeTop = Observable.timer((long) (Math.random() * 500 + 500), TimeUnit.MILLISECONDS)
                .map(it -> {
                    if (Math.random() > 0.2) {
                        return (int) (Math.random() * 100);
                    } else {
                        throw new Exception("Something wrong!!!");
                    }
                })
                .subscribe(data -> {
                    LiveDataUtils.update(mTopNumberLiveData, StateData.ready(data));
                    if (updateAll) { updateBottom(true); }
                }, error -> LiveDataUtils.update(mTopNumberLiveData, StateData.error(error)));
    }

    public void updateBottom() { updateBottom(false); }

    public void updateBottom(boolean updateAll) {
        RxUtils.dispose(mDisposeBottom);
        LiveDataUtils.update(mBottomNumberLiveData, StateData.loading());
        mDisposeBottom = Observable.timer((long) (Math.random() * 500 + 500), TimeUnit.MILLISECONDS)
                .map(it -> {
                    if (Math.random() > 0.2) {
                        return (int) (Math.random() * 100);
                    } else {
                        throw new Exception("Something wrong!!!");
                    }
                })
                .subscribe(data -> {
                    LiveDataUtils.update(mBottomNumberLiveData, StateData.ready(data));
                    if (updateAll) { updateLeft(true); }
                }, error -> LiveDataUtils.update(mBottomNumberLiveData, StateData.error(error)));
    }

    public void updateLeft() { updateLeft(false); }

    public void updateLeft(boolean updateAll) {
        RxUtils.dispose(mDisposeLeft);
        LiveDataUtils.update(mLeftNumberLiveData, StateData.loading());
        mDisposeLeft = Observable.timer((long) (Math.random() * 500 + 500), TimeUnit.MILLISECONDS)
                .map(it -> {
                    if (Math.random() > 0.2) {
                        return (int) (Math.random() * 100);
                    } else {
                        throw new Exception("Something wrong!!!");
                    }
                })
                .subscribe(data -> {
                    LiveDataUtils.update(mLeftNumberLiveData, StateData.ready(data));
                    if (updateAll) { updateRight(); }
                }, error -> LiveDataUtils.update(mLeftNumberLiveData, StateData.error(error)));
    }

    public void updateRight() {
        RxUtils.dispose(mDisposeRight);
        LiveDataUtils.update(mRightNumberLiveData, StateData.loading());
        mDisposeRight = Observable.timer((long) (Math.random() * 500 + 500), TimeUnit.MILLISECONDS)
                .map(it -> {
                    if (Math.random() > 0.2) {
                        return (int) (Math.random() * 100);
                    } else {
                        throw new Exception("Something wrong!!!");
                    }
                })
                .subscribe(data -> LiveDataUtils.update(mRightNumberLiveData, StateData.ready(data)),
                        error -> LiveDataUtils.update(mRightNumberLiveData, StateData.error(error)));
    }

    public void clear() {
        mTopNumberLiveData.setValue(StateData.ready(0));
        mBottomNumberLiveData.setValue(StateData.ready(0));
        mLeftNumberLiveData.setValue(StateData.ready(0));
        mRightNumberLiveData.setValue(StateData.ready(0));
    }
}
