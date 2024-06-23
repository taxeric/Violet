package com.lanier.violet.feature.db_manager.processor

import com.lanier.violet.database.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SceneDbProcessor(
    filepath: String
) : AbsCombineProcessor(filepath) {

    companion object {

        private const val SCENE = "scenes.txt"
    }

    override val path: String
        get() = "/scene/"

    override suspend fun sync(
        onStart: (() -> Unit)?,
        onError: ((Throwable) -> Unit)?,
        onCompleted: (() -> Unit)?,
    ) {
        withContext(Dispatchers.IO) {
            readFromOrigin(Constant.TN_SCENE, SCENE)
        }
    }
}