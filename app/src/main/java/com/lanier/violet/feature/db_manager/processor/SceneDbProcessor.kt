package com.lanier.violet.feature.db_manager.processor

import com.lanier.violet.database.Constant
import com.lanier.violet.database.dao.SceneDao
import com.lanier.violet.database.entity.Scene
import com.lanier.violet.ext.calcRunTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SceneDbProcessor(
    filepath: String,
    private val dao: SceneDao
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
            calcRunTime {
                val result = readFromOrigin(Constant.TN_SCENE, SCENE)
                val scenes = parseScenes(result.second)
                dao.upsertAll(scenes)
            }
        }
    }

    private fun parseScenes(text: String) : List<Scene> {
        val sceneTexts = text.split("<SceneDes ").drop(1).map { "<SceneDes $it" }
        return sceneTexts.map {
            val attributes = parseAttributes(it, "SceneDes")
            Scene(
                id = attributes["id"] ?: "",
                name = attributes["name"] ?: "",
                bgMusic = attributes["bgMusic"] ?: ""
            )
        }
    }
}