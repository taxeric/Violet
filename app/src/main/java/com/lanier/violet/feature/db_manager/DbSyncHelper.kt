package com.lanier.violet.feature.db_manager

import com.lanier.violet.database.dao.GameDao
import com.lanier.violet.database.dao.PropDao
import com.lanier.violet.database.dao.SceneDao
import com.lanier.violet.database.dao.SkillDao
import com.lanier.violet.database.dao.SpiritDao
import com.lanier.violet.ext.launchSafe
import com.lanier.violet.feature.db_manager.processor.AbsCombineProcessor
import com.lanier.violet.feature.db_manager.processor.GameDbProcessor
import com.lanier.violet.feature.db_manager.processor.PropDbProcessor
import com.lanier.violet.feature.db_manager.processor.SceneDbProcessor
import com.lanier.violet.feature.db_manager.processor.SkillDbProcessor
import com.lanier.violet.feature.db_manager.processor.SpiritDbProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object DbSyncHelper {

    private val mainScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    var cachePath = ""

    fun syncSpirit(
        scope: CoroutineScope = mainScope,
        dao: SpiritDao,
        onStart: (() -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        onSuccess: (() -> Unit)? = null,
    ) {
        scope.launchSafe(
            error = {
                println(">>>> error ${it.message}")
                onError?.invoke(it)
            }
        ) {
            val processor: AbsCombineProcessor = SpiritDbProcessor(cachePath, dao)
            processor.sync(
                onStart = onStart,
                onCompleted = onSuccess,
            )
        }
    }

    fun syncSkill(
        scope: CoroutineScope = mainScope,
        dao: SkillDao,
        onStart: (() -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        onSuccess: (() -> Unit)? = null,
    ) {
        scope.launchSafe(
            error = {
                onError?.invoke(it)
            }
        ) {
            val processor : AbsCombineProcessor = SkillDbProcessor(cachePath, dao)
            processor.sync(
                onStart = onStart,
                onCompleted = onSuccess,
            )
        }
    }

    fun syncScene(
        scope: CoroutineScope = mainScope,
        dao: SceneDao,
        onStart: (() -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        onSuccess: (() -> Unit)? = null,
    ) {
        scope.launchSafe(
            error = {
                onError?.invoke(it)
            }
        ) {
            val processor : AbsCombineProcessor = SceneDbProcessor(cachePath, dao)
            processor.sync(
                onStart = onStart,
                onCompleted = onSuccess,
            )
        }
    }

    fun syncGame(
        scope: CoroutineScope = mainScope,
        dao: GameDao,
        onStart: (() -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        onSuccess: (() -> Unit)? = null,
    ) {
        scope.launchSafe(
            error = {
                onError?.invoke(it)
            }
        ) {
            val processor : AbsCombineProcessor = GameDbProcessor(cachePath, dao)
            processor.sync(
                onStart = onStart,
                onCompleted = onSuccess,
            )
        }
    }

    fun syncProps(
        scope: CoroutineScope = mainScope,
        dao: PropDao,
        onStart: (() -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        onSuccess: (() -> Unit)? = null,
    ) {
        scope.launchSafe(
            error = {
                onError?.invoke(it)
            }
        ) {
            val processor : AbsCombineProcessor = PropDbProcessor(cachePath, dao)
            processor.sync(
                onStart = onStart,
                onCompleted = onSuccess,
            )
        }
    }
}