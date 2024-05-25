package com.lanier.violet.flowbus

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by 幻弦让叶
 * Date 2024/5/25 17:57
 */
object FlowBus {

    private val events = ConcurrentHashMap<String, MutableSharedFlow<Any>>()

    /**
     * 存储
     */
    private fun <T> with(key: String): MutableSharedFlow<T> {
        if (!events.containsKey(key)) {
            events[key] = MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
        }
        return events[key] as MutableSharedFlow<T>
    }

    /**
     * 发送
     */
    fun <T> post(key: String, value: T) {
        with<T>(key).tryEmit(value)
    }

    /**
     * 接收
     */
    suspend fun <T> collectLatest(key: String, action: suspend (T) -> Unit){
        with<T>(key).collectLatest(action)
    }

    suspend fun <T> collect(key: String, action: suspend (T) -> Unit){
        with<T>(key).collect(action)
    }

    fun <T> each(key: String, action: (T) -> Unit) {
        with<T>(key).onEach(action)
    }

    fun <T> eachSuspend(key: String, action: suspend (T) -> Unit) {
        with<T>(key).onEach(action)
    }
}