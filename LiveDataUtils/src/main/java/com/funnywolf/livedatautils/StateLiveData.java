package com.funnywolf.livedatautils;

import androidx.lifecycle.LiveData;

public class StateLiveData extends LiveData<State> {
    public void setState(State state) {

    }

    public State getState() {
        return getValue();
    }
}
