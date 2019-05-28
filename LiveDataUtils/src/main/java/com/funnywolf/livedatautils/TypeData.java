package com.funnywolf.livedatautils;

import android.util.SparseArray;

public class TypeData {
    public final int type;
    public final SparseArray<Object> data = new SparseArray<>(2);

    public TypeData(int type) {
        this.type = type;
    }

    public TypeData put(int key, Object data) {
        this.data.put(key, data);
        return this;
    }

}
