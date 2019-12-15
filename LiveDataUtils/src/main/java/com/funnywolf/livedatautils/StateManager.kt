package com.funnywolf.livedatautils

import android.os.Looper
import android.util.SparseArray
import androidx.annotation.IntRange
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import java.lang.Exception

/**
 * @author funnywolf
 * @since 2019-12-15
 */
class State(
    /**
     * 类型
     */
    val type: Int,
    /**
     * 源数据
     */
    val data: Any? = null,
    /**
     * 当前状态，只有定义的三种
     */
    @IntRange(from = STATUS_LOADING, to = STATUS_ERROR) val status: Long,
    /**
     * 出错时的信息
     */
    val error: Exception? = null
) {
    companion object {
        const val STATUS_LOADING = 0L
        const val STATUS_READY = 1L
        const val STATUS_ERROR = 2L

        fun loading(type: Int, data: Any? = null) = State(type, data, STATUS_LOADING, null)
        fun ready(type: Int, data: Any? = null) = State(type, data, STATUS_READY, null)
        fun error(type: Int, error: Exception?, data: Any? = null) = State(type, data, STATUS_ERROR, error)
    }
}

interface StateObserver {
    /**
     * [StateObserver] 监听所有 [State] 不区分 [State.type]，具体类型的分发交给使用者处理
     */
    fun onChanged(state: State)

    /**
     * [onAdded] 和 [onRemoved] 是为了让 [StateObserver] 知道被添加到的 [StateManager]
     * 方便之后随生命周期自动从 [StateManager] 中移除
     */
    fun onAdded(manager: StateManager) {}
    fun onRemoved(manager: StateManager) {}
}

interface StateManager {
    fun addObserver(observer: StateObserver)
    fun removeObserver(observer: StateObserver)
    fun get(type: Int): State?
    fun postAction(type: Int, data: Any? = null)
}

inline fun <reified T> StateManager.getData(type: Int): T? {
    return get(type)?.data as? T
}

private val MainHandler by lazy { android.os.Handler(Looper.getMainLooper()) }

abstract class BaseStateManager: StateManager {
    private val observerSet = HashSet<StateObserver?>()
    private val stateArray = SparseArray<State?>()

    override fun addObserver(observer: StateObserver) {
        if (!isOnMain()) {
            MainHandler.post { addObserver(observer) }
            return
        }
        if (observerSet.add(observer)) {
            observer.onAdded(this)
        }
    }

    override fun removeObserver(observer: StateObserver) {
        if (!isOnMain()) {
            MainHandler.post { removeObserver(observer) }
            return
        }
        if (observerSet.remove(observer)) {
            observer.onRemoved(this)
        }
    }

    override fun get(type: Int): State? {
        return stateArray[type]
    }

    protected fun dispatchState(state: State) {
        if (!isOnMain()) {
            MainHandler.post { dispatchState(state) }
            return
        }
        observerSet.forEach {
            it?.onChanged(state)
        }
        stateArray.put(state.type, state)
    }

    protected fun isOnMain() = Looper.getMainLooper().thread == Thread.currentThread()
}

class LifecycleWrappedObserver(
    private val observer: StateObserver,
    private val owner: LifecycleOwner
): StateObserver, LifecycleObserver {
    private var currentManager: StateManager? = null
    private val pendingStates = ArrayList<State>()

    override fun onChanged(state: State) {
        // 还没有加入 manager，直接退出
        currentManager ?: return
        if (owner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            // 生命周期处于激活状态，则直接分发
            observer.onChanged(state)
        } else if (owner.lifecycle.currentState != Lifecycle.State.DESTROYED) {
            // 生命周期还没结束，暂存 State
            pendingStates.add(state)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onEvent(owner: LifecycleOwner, event: Lifecycle.Event) {
        if (Lifecycle.Event.ON_DESTROY == event) {
            // 销毁时将自己从 manager 中移除
            currentManager?.removeObserver(this)
        } else if (Lifecycle.Event.ON_START == event || Lifecycle.Event.ON_RESUME == event) {
            // 生命周期处于激活状态时分发暂存的 State
            pendingStates.forEach {
                observer.onChanged(it)
            }
            pendingStates.clear()
        }
    }

    override fun onAdded(manager: StateManager) {
        super.onAdded(manager)
        // 从之前的 manager 中移除
        currentManager?.removeObserver(this)
        // 如果已经销毁，就将自己从 manager 中移除
        if (owner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
            manager.removeObserver(this)
            return
        }
        // 清空暂存，设置新的 manager，并监听生命周期
        pendingStates.clear()
        currentManager = manager
        owner.lifecycle.addObserver(this)
    }

    override fun onRemoved(manager: StateManager) {
        super.onRemoved(manager)
        // 移除生命周期监听和 manager，清空暂存
        owner.lifecycle.removeObserver(this)
        currentManager = null
        pendingStates.clear()
    }

}
