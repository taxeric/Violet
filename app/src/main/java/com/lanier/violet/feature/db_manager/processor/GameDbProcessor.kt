package com.lanier.violet.feature.db_manager.processor

import com.lanier.violet.database.Constant
import com.lanier.violet.database.dao.GameDao
import com.lanier.violet.database.entity.Game
import com.lanier.violet.ext.calcRunTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameDbProcessor(
    filepath: String,
    private val dao: GameDao
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
            calcRunTime {
                val result = readFromOrigin(Constant.TN_GAME, GAMES)
                val games = parseGames(result.second)
                dao.upsertAll(games)
            }
        }
    }

    private fun parseGames(text: String) : List<Game> {
        val gameTexts = text.split("<Item ").drop(1).map { "<Item $it" }
        return gameTexts.map {
            val attributes = parseAttributes(it, "Item")
            Game(
                id = attributes["id"] ?: "",
                name = attributes["name"] ?: "",
                type = attributes["type"] ?: "",
                tips = attributes["tips"] ?: "",
                sceneId = attributes["sceneId"] ?: ""
            )
        }
    }
}