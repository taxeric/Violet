package com.lanier.violet.feature.main.event

/**
 * Created by 幻弦让叶
 * Date 2024/6/2 15:52
 */
data class FarmEvent(
    val action: Int,
    val plantId: Int,
    val success: Boolean = false,
) {

    companion object {

        const val HARVEST = 0
        const val PLANT = 1
    }
}