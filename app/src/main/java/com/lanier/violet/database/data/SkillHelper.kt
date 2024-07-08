package com.lanier.violet.database.data

import com.lanier.violet.database.VioletDatabase
import com.lanier.violet.database.entity.Skill

object SkillHelper : AbsDBHelperProcessor() {

    private val dao by lazy { VioletDatabase.db.skillDao() }

    @OptIn(ExperimentalStdlibApi::class)
    fun getSkillByHexId(
        hexId: String,
        onSuccess: (Skill) -> Unit,
        onFailure: (() -> Unit)? = null,
    ) {
        val id = hexId.hexToInt().toString()
        getSkillById(id, onSuccess, onFailure)
    }

    /**
     * 获取技能
     */
    fun getSkillById(
        id: String,
        onSuccess: (Skill) -> Unit,
        onFailure: (() -> Unit)? = null,
    ) {
        asyncAction(
            block = { dao.getSkillById(id) },
            check = { it.validSkill() },
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    /**
     * 获取技能
     */
    fun getSkillsByName(
        name: String,
        onSuccess: (List<Skill>) -> Unit,
        onFailure: (() -> Unit)? = null
    ) {
        asyncAction(
            block = { dao.getSkillsByName(name) },
            check = { true },
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}