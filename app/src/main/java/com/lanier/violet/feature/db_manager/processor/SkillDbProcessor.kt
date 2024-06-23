package com.lanier.violet.feature.db_manager.processor

import com.lanier.violet.database.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SkillDbProcessor(
    filepath: String
) : AbsCombineProcessor(filepath) {

    companion object {

        private const val SKILL = "skills.txt"
        private const val SKILL_EFFECT = "skill_effects.txt"
        private const val SKILL_EFFECT_DESC = "effects_desc.txt"
    }

    override val path: String
        get() = "/spirit/"

    override suspend fun sync(
        onStart: (() -> Unit)?,
        onError: ((Throwable) -> Unit)?,
        onCompleted: (() -> Unit)?,
    ) {
        withContext(Dispatchers.IO) {
            readFromOrigin(Constant.TN_SKILL, SKILL)
        }

        withContext(Dispatchers.IO) {
            readFromOrigin(Constant.TN_SKILL_EFFECT, SKILL_EFFECT)
        }

        withContext(Dispatchers.IO) {
            readFromOrigin(Constant.TN_EFFECT_DETAILS, SKILL_EFFECT_DESC)
        }
    }
}