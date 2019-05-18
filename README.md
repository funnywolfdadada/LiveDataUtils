# LiveDataUtils
自己常用的一些 `LiveData` 的工具类，目前包括：
 - `AsEventBus`，标识 `LiveData` 用作事件总线
 - `EventMediatorLiveData`，用于事件总线的 `MediatorLiveData`，能够保证事件不丢失
 - `EventMutableLiveData`，用于事件总线的 `MutableLiveData`，能够保证事件不丢失
 - `LiveDataBus`，用 `LiveData` 做的事件总线
 - `LiveDataUtils`，设置 `LiveData` 的工具类
 - `LiveEventObserver`，`LiveData` 用作事件总线时的观察者，保证事件不丢失
 - `ResponseUtils`，处理 `Retrofit` 的 `Response`
 - `RxLiveData`，`RxJava` 链与 `LiveData` 适配
 - `StateData`，带有状态的数据包装类
 
gradle 依赖方法：
```
dependencies {
    implementation 'com.funnywolf:LiveDataUtils:1.1.0'
}
```
