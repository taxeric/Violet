package com.lanier.violet.feature.db_manager.processor

import com.lanier.violet.database.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameDbProcessor(
    filepath: String
) : AbsCombineProcessor(filepath) {

    companion object {

        private const val GAMES = "games.txt"
    }

    override val path: String
        get() = "/game/"

    override suspend fun sync(
        onStart: (() -> Unit)?,
        onError: ((Throwable) -> Unit)?,
        onCompleted: (() -> Unit)?,
    ) {
        withContext(Dispatchers.IO) {
            readFromOrigin(Constant.TN_GAME, GAMES)
        }
    }
}