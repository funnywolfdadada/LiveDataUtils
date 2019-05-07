package com.funnywolf.livedatautils;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 利用 LiveData 做的事件总线，用于不能通过 ViewModel 进行通讯的场景，部分替代 RxBus 的功能
 * 可以通过 tag 拿到不同的总线，区分不同业务
 * 注意：能用 ViewModel 通讯就用 ViewModel，实在不行在用这个
 *
 * @author funnywolf
 * @since 2019/4/11
 */
public class LiveDataBus {

    /**
     * 默认总线，tag 为空字符串
     */
    private static final LiveDataBus INSTANCE = new LiveDataBus("");

    /**
     * 存储以 {@link ##mTag} 标识的总线
     * key: tag 字符串
     * value: 对应的总线
     */
    private static final ConcurrentHashMap<String, LiveDataBus> BUSES = new ConcurrentHashMap<>(2);

    private final String mTag;

    /**
     * 存储事件类型和 MutableLiveData
     * key: 事件的 class
     * value: 包含事件 class 和 MutableLiveData 的 LiveDataItem
     */
    private final ConcurrentHashMap<Class, LiveDataItem> mArray = new ConcurrentHashMap<>();

    /**
     * 返回默认总线
     */
    public static LiveDataBus get() {
        return get("");
    }

    /**
     * 根据 tag 拿对应总线
     * @param tag 标识事件总线的 tag
     * @return 返回 tag 标识的总线，tag 为 null 或空字符串时返回默认总线
     */
    public static LiveDataBus get(String tag) {
        if (tag == null || tag.length() == 0) {
            return INSTANCE;
        }
        LiveDataBus bus = BUSES.get(tag);
        if (bus == null) {
            synchronized (BUSES) {
                bus = BUSES.get(tag);
                if (bus == null) {
                    bus = new LiveDataBus(tag);
                    BUSES.put(tag, bus);
                }
            }
        }
        return bus;
    }

    private LiveDataBus(String tag) { mTag = tag; }

    public String getTag() { return mTag; }

    /**
     * 返回事件 T 的 MutableLiveData
     */
    public <T> MutableLiveData<T> getData(Class<T> eventClass) {
        if (eventClass == null) {
            return null;
        }
        return getItem(eventClass).mutableLiveData;
    }

    public <T> void observe(Class<T> clazz, LifecycleOwner owner, Observer<T> observer) {
        if (clazz == null || owner == null || observer == null) { return; }
        getData(clazz).observe(owner, observer);
    }

    public <T> void observeForever(Class<T> clazz, Observer<T> observer) {
        if (clazz == null || observer == null) { return; }
        getData(clazz).observeForever(observer);
    }

    public <T> void removeObserver(Class<T> clazz, Observer<T> observer) {
        if (clazz == null || observer == null) { return; }
        getData(clazz).removeObserver(observer);
    }

    public <T> void post(Class<T> clazz, T data) {
        if (clazz == null) { return; }
        // setValue 保证事件不丢失
        LiveDataUtils.setValue(getData(clazz), data);
    }

    private <T> LiveDataItem<T> getItem(Class<T> eventClass) {
        LiveDataItem<T> item = getItemInternal(eventClass);
        if (item == null) {
            synchronized (this) {
                item = getItemInternal(eventClass);
                if (item == null) {
                    item = new LiveDataItem<>(eventClass);
                    mArray.put(eventClass, item);
                }
            }
        }
        return item;
    }

    private <T> LiveDataItem<T> getItemInternal(Class<T> clazz) {
        LiveDataItem item = mArray.get(clazz);
        if (item != null && item.clazz.equals(clazz)) {
            // Item 可以保证泛型与 clazz 一致
            return item;
        }
        return null;
    }

    /**
     * 存储事件类型和 MutableLiveData，为了保证事件 class 的泛型和 MutableLiveData 的泛型一致
     */
    private static class LiveDataItem<T> {
        private final Class<T> clazz;
        private final MutableLiveData<T> mutableLiveData;

        LiveDataItem(Class<T> clazz) {
            this.clazz = clazz;
            mutableLiveData = new MutableLiveData<>();
        }
    }
}
