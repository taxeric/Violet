package com.lanier.violet.feature.pet.data

/**
 * Created by 幻弦让叶
 * Date 2024/5/26 23:24
 */
data class PetData(
    val id: Int,
    val lv: Int,
    val exp: Int,
    val sex: Int,
    val character: Int,
    val acquisitionTime: Long,
    val maxHp: Int,
    val hp: Int,
    val attack: Int,
    val defence: Int,
    val magicAttack: Int,
    val magicDefence: Int,
    val speed: Int,
    val hpGift: Int,
    val attackGift: Int,
    val defenceGift: Int,
    val magicAttackGift: Int,
    val magicDefenceGift: Int,
    val speedGift: Int,
    val hpEffort: Int,
    val attackEffort: Int,
    val defenceEffort: Int,
    val magicAttackEffort: Int,
    val magicDefenceEffort: Int,
    val speedEffort: Int,
    val skills: List<SkillData>,
) {

    var selected: Boolean = false
}