package com.funnywolf.livedatautils;

import android.util.SparseArray;

public interface StateObserver {
    void onStateChanged(int state, SparseArray<Object> data, Throwable error);
}
