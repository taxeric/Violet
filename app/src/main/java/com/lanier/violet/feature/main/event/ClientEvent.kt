package com.lanier.violet.feature.main.event

/**
 * Created by 幻弦让叶
 * Date 2024/5/25 18:18
 */
data class ClientEvent(val action: Int, val message: String = "") {

    companion object {

        const val ACTION_CONNECTING = 0
        const val ACTION_CONNECTED = 1
        const val ACTION_DISCONNECTED = 2
        const val ACTION_ENTER_CHANNEL = 3
        const val ACTION_ENTER_CHANNEL_FAILED = 4
    }
}
