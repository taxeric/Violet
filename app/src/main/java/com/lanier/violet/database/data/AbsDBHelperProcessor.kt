package com.lanier.violet.database.data

import com.lanier.violet.ext.launchSafe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext

abstract class AbsDBHelperProcessor {

    protected val mainScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

    suspend fun <T> suspendAction(
        block: (() -> T?)?,
    ) : T? {
        val data = withContext(Dispatchers.IO) {
            block?.invoke()
        }
        return data
    }

    fun <T> asyncAction(
        block: (() -> T?)?,
        check: (T) -> Boolean,
        onSuccess: (T) -> Unit,
        onFailure: (() -> Unit)? = null
    ) {
        mainScope.launchSafe {
            val data = withContext(Dispatchers.IO) {
                block?.invoke()
            }
            data?.let {
                if (check(it)) {
                    onSuccess.invoke(it)
                } else {
                    onFailure?.invoke()
                }
            } ?: onFailure?.invoke()
        }
    }
}