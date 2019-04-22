package com.example.mvvmdemo.viewmodels;

import com.example.mvvmdemo.repositories.RandomNumberRepository;
import com.example.mvvmdemo.repositories.StateData;
import com.example.mvvmdemo.utils.LiveDataUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel 层，主要处理与 View 层和 Repository(Model) 层交互，处理业务逻辑，数据请求交给 Repository 层完成
 *
 * @author funnywolf
 * @since 2019-04-22
 */
public class RandomNumberViewModel extends ViewModel {

    private boolean mUpdateAll;

    private final MediatorLiveData<StateData<Integer>> mTopNumberLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<StateData<Integer>> mBottomNumberLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<StateData<Integer>> mLeftNumberLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<StateData<Integer>> mRightNumberLiveData = new MediatorLiveData<>();

    public LiveData<StateData<Integer>> getTopNumberLiveData() { return mTopNumberLiveData; }

    public LiveData<StateData<Integer>> getBottomNumberLiveData() { return mBottomNumberLiveData; }

    public LiveData<StateData<Integer>> getLeftNumberLiveData() { return mLeftNumberLiveData; }

    public LiveData<StateData<Integer>> getRightNumberLiveData() { return mRightNumberLiveData; }

    public RandomNumberViewModel() {
        mTopNumberLiveData.addSource(RandomNumberRepository.INSTANCE.getTopNumberLiveData(), data -> {
            LiveDataUtils.update(mTopNumberLiveData, data);
            if (data == null) {
                mUpdateAll = false;
                return;
            }
            switch (data.state) {
                case StateData.STATE_READY:
                    if (mUpdateAll) { updateBottom(); }
                    break;
                case StateData.STATE_ERROR:
                    mUpdateAll = false;
                    break;
                default:
                    break;
            }
        });
        mBottomNumberLiveData.addSource(RandomNumberRepository.INSTANCE.getBottomNumberLiveData(), data -> {
            LiveDataUtils.update(mBottomNumberLiveData, data);
            if (data == null) {
                mUpdateAll = false;
                return;
            }
            switch (data.state) {
                case StateData.STATE_READY:
                    if (mUpdateAll) { updateLeft(); }
                    break;
                case StateData.STATE_ERROR:
                    mUpdateAll = false;
                    break;
                default:
                    break;
            }
        });
        mLeftNumberLiveData.addSource(RandomNumberRepository.INSTANCE.getLeftNumberLiveData(), data -> {
            LiveDataUtils.update(mLeftNumberLiveData, data);
            if (data == null) {
                mUpdateAll = false;
                return;
            }
            switch (data.state) {
                case StateData.STATE_READY:
                    if (mUpdateAll) { updateRight(); }
                    break;
                case StateData.STATE_ERROR:
                    mUpdateAll = false;
                    break;
                default:
                    break;
            }
        });
        mRightNumberLiveData.addSource(RandomNumberRepository.INSTANCE.getRightNumberLiveData(), data -> {
            LiveDataUtils.update(mRightNumberLiveData, data);
            mUpdateAll = false;
        });
        clear();
    }

    public void updateAll() {
        mUpdateAll = true;
        updateTop();
    }

    public void updateTop() {
        RandomNumberRepository.INSTANCE.updateTop();
    }

    public void updateBottom() {
        RandomNumberRepository.INSTANCE.updateBottom();
    }

    public void updateLeft() {
        RandomNumberRepository.INSTANCE.updateLeft();
    }

    public void updateRight() {
        RandomNumberRepository.INSTANCE.updateRight();
    }

    public void clear() {
        mTopNumberLiveData.setValue(StateData.ready(0));
        mBottomNumberLiveData.setValue(StateData.ready(0));
        mLeftNumberLiveData.setValue(StateData.ready(0));
        mRightNumberLiveData.setValue(StateData.ready(0));
    }
}
