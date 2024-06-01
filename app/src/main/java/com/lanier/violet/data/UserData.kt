package com.lanier.violet.data

/**
 * Created by 幻弦让叶
 * Date 2024/5/19 21:34
 */
object UserData {

    var mainKey = ""
    var farmKey = ""

    var currentSceneId = -1

    @OptIn(ExperimentalStdlibApi::class)
    var QQ = ""
        set(value) {
            field = value
            hexQQ = value.toInt().toHexString()
        }

    /**
     * 16进制QQ
     */
    var hexQQ = ""
        private set
}