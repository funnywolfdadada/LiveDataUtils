package com.funnywolf.livedatautils;

import android.util.SparseArray;

public abstract class BaseEventHandler {
    protected boolean attached = false;
    protected StateEventViewModel mViewModel;

    public abstract void handleEvent(int type, SparseArray<Object> data);

    final public void attach(StateEventViewModel viewModel) {
        mViewModel = viewModel;
        onAttach();
        attached = true;
    }

    protected void onAttach() {}

    final public void detach() {
        mViewModel = null;
        onDetach();
        attached = false;
    }

    protected void onDetach() {}
}
