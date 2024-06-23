package com.lanier.violet.feature.db_manager

import com.lanier.violet.database.dao.SpiritDao
import com.lanier.violet.ext.launchSafe
import com.lanier.violet.feature.db_manager.processor.AbsCombineProcessor
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
}