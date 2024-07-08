package com.lanier.violet.database.data

import com.lanier.violet.database.VioletDatabase
import com.lanier.violet.database.entity.Spirit

object SpiritHelper : AbsDBHelperProcessor() {

    private val dao by lazy { VioletDatabase.db.spiritDao() }

    @OptIn(ExperimentalStdlibApi::class)
    fun getSpiritByHexId(
        hexId: String,
        onSuccess: (Spirit) -> Unit,
        onFailure: (() -> Unit)? = null,
    ) {
        val id = hexId.hexToInt().toString()
        getSpiritById(id, onSuccess, onFailure)
    }

    /**
     * 获取精灵
     */
    fun getSpiritById(
        id: String,
        onSuccess: (Spirit) -> Unit,
        onFailure: (() -> Unit)? = null,
    ) {
        asyncAction(
            block = { dao.getSpiritById(id) },
            check = { it.validSpirit() },
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}