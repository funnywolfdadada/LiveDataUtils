package com.funnywolf.livedatautils;


import android.util.SparseArray;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

public class StateEventViewModel extends ViewModel {
    private final SparseArray<StateLiveData> liveDataArray = new SparseArray<>();
    private final List<BaseEventHandler> handlers = new ArrayList<>();

    public void addHandler(BaseEventHandler handler) {
        if (handler == null || handlers.contains(handler)) { return; }
        handlers.add(handler);
        handler.attach(this);
    }

    public void removeHandler(BaseEventHandler handler) {
        if (handler == null) { return; }
        handlers.remove(handler);
        handler.detach();
    }

    public void sendEvent(Event event) {
        if (event == null) { return; }
        for (BaseEventHandler handler : handlers) {
            handler.handleEvent(event.type, event.data);
        }
    }

    private StateLiveData getStateLiveData(int type) {
        StateLiveData liveData = liveDataArray.get(type);
        if (liveData == null) {
            liveData = new StateLiveData();
            liveDataArray.put(type, liveData);
        }
        return liveData;
    }

    public void sendState(State state) {
        if (state == null) { return; }
        getStateLiveData(state.type).setState(state);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        for (BaseEventHandler handler : handlers) {
            removeHandler(handler);
        }
    }

    public static StateEventViewModel get(FragmentActivity activity) {
        return ViewModelProviders.of(activity).get(StateEventViewModel.class);
    }

    public static StateEventViewModel get(Fragment fragment) {
        return ViewModelProviders.of(fragment).get(StateEventViewModel.class);
    }
}
