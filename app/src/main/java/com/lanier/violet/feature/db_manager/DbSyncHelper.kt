package com.lanier.violet.feature.db_manager

import com.lanier.violet.database.dao.GameDao
import com.lanier.violet.database.dao.SceneDao
import com.lanier.violet.database.dao.SkillDao
import com.lanier.violet.database.dao.SpiritDao
import com.lanier.violet.ext.launchSafe
import com.lanier.violet.feature.db_manager.processor.AbsCombineProcessor
import com.lanier.violet.feature.db_manager.processor.GameDbProcessor
import com.lanier.violet.feature.db_manager.processor.SceneDbProcessor
import com.lanier.violet.feature.db_manager.processor.SkillDbProcessor
import com.lanier.violet.feature.db_manager.processor.SpiritDbProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object DbSyncHelper {

    private val mainScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    var cachePath = ""

    fun syncSpirit(scope: CoroutineScope = mainScope, dao: SpiritDao) {
        scope.launchSafe(
            error = {
                println(">>>> error ${it.message}")
            }
        ) {
            val processor: AbsCombineProcessor = SpiritDbProcessor(cachePath, dao)
            processor.sync()
        }
    }

    fun syncSkill(scope: CoroutineScope = mainScope, dao: SkillDao) {
        scope.launchSafe {
            val processor : AbsCombineProcessor = SkillDbProcessor(cachePath, dao)
            processor.sync()
        }
    }

    fun syncScene(scope: CoroutineScope = mainScope, dao: SceneDao) {
        scope.launchSafe {
            val processor : AbsCombineProcessor = SceneDbProcessor(cachePath, dao)
            processor.sync()
        }
    }

    fun syncGame(scope: CoroutineScope = mainScope, dao: GameDao) {
        scope.launchSafe {
            val processor : AbsCombineProcessor = GameDbProcessor(cachePath, dao)
            processor.sync()
        }
    }
}