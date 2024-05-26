package com.lanier.violet.feature.main.event

/**
 * Created by 幻弦让叶
 * Date 2024/5/25 21:50
 */
data class UserInfoEvent(
    val username: String,
    val userLevel: Int,
    val userStamina: Int,
    val userWisdom: Int,
    val userCharm: Int,
    val userCoins: Int,
    val userCredit: Int,
    val userTargetCredit: Int,
    val vipDays: Int,
)