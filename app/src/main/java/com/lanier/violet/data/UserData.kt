package com.lanier.violet.data

/**
 * Created by 幻弦让叶
 * Date 2024/5/19 21:34
 */
object UserData {

    const val LAND_MAX_INDEX = 15

    var mainKey = ""
        set(value) {
            field = value
            println(">>>> set main key : $value")
        }
    var farmKey = ""
        set(value) {
            field = value
            println(">>>> set farm key : $value")
        }

    var currentSceneId = -1

    @OptIn(ExperimentalStdlibApi::class)
    var QQ = ""
        set(value) {
            field = value
            hexQQ = value.toInt().toHexString()
        }

    var farmLandHandleIndex = 0


    /**
     * 16进制QQ
     */
    var hexQQ = ""
        private set
}