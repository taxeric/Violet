package com.lanier.violet.database.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

abstract class AbsDBHelperProcessor {

    protected val mainScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

    fun <T> asyncAction(
        block: (() -> T?)?,
        check: (T?) -> Boolean,
        onSuccess: (T) -> Unit,
        onFailure: (() -> Unit)? = null
    ) {
        mainScope.launch {
            block?.invoke()?.let {
                if (check(it)) {
                    onSuccess.invoke(it)
                } else {
                    onFailure?.invoke()
                }
            } ?: onFailure?.invoke()
        }
    }
}