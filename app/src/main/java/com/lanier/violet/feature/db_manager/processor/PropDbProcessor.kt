package com.lanier.violet.feature.db_manager.processor

import com.lanier.violet.database.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PropDbProcessor(
    filepath: String
) : AbsCombineProcessor(filepath) {

    companion object {

        private const val PROPS = "common_props.txt"
        private const val SEED = "farm_seeds.txt"
    }

    override val path: String
        get() = "/props/"

    override suspend fun sync(
        onStart: (() -> Unit)?,
        onError: ((Throwable) -> Unit)?,
        onCompleted: (() -> Unit)?,
    ) {
        withContext(Dispatchers.IO) {
            readFromOrigin(Constant.TN_PROP, PROPS)
        }
        withContext(Dispatchers.IO) {
            readFromOrigin(Constant.TN_SEED, SEED)
        }
    }
}