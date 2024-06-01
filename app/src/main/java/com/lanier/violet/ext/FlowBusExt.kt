package com.lanier.violet.ext

import com.lanier.violet.flowbus.FlowBus

/**
 * Created by 幻弦让叶
 * Date 2024/5/25 17:59
 */
inline fun <reified T> T.post() {
    post(T::class.java.name, this)
}

inline fun <reified T> T.post(value: T) {
    post(T::class.java.name, value)
}

fun <T> T.post(key: String, value: T) {
    FlowBus.post(key, value)
}

inline fun <reified T> each(
    crossinline action: (T) -> Unit
) {
    FlowBus.each<T>(T::class.java.name) {
        action.invoke(it)
    }
}

suspend inline fun <reified T> collect(
    crossinline action: (T) -> Unit
) {
    FlowBus.collect<T>(T::class.java.name) {
        action.invoke(it)
    }
}

suspend inline fun <reified T> collectSuspendAction(
    crossinline action: suspend (T) -> Unit
) {
    FlowBus.collect<T>(T::class.java.name) {
        action.invoke(it)
    }
}

suspend inline fun <reified T> collect(
    key: String,
    crossinline action: (T) -> Unit
) {
    FlowBus.collect<T>(key) {
        action.invoke(it)
    }
}
