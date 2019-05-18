package com.funnywolf.livedatautils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 该注解只用于 {@link androidx.lifecycle.LiveData}，用于表示 LiveData 是当成事件总线用的，需要注意：
 * - 观察者在非激活状态（onStart 之前，onStop 之后）时不会产生回调，会丢失事件
 * - postValue 可能会被覆盖，只能用 setValue 来更新值
 * - LiveData 的事件都是黏性的，不使用时手动抛出一个 null 事件，以防下次绑定时会发送存在之前的旧数据；
 *
 * @see LiveDataUtils
 * @see LiveEventObserver
 * @see EventMutableLiveData
 * @see EventMediatorLiveData
 *
 * @author funnywolf
 * @since 2019-05-06
 */
@Target(ElementType.FIELD)
public @interface AsEventBus {
}
