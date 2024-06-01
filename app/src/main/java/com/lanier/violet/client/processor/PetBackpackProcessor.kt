package com.lanier.violet.client.processor

import com.lanier.violet.client.IProcessor
import com.lanier.violet.feature.pet.data.PetData
import com.lanier.violet.feature.pet.data.SkillData

/**
 * Created by 幻弦让叶
 * Date 2024/6/1 1:25
 */
class PetBackpackProcessor(private val info: String) : IProcessor<List<PetData>> {
    @OptIn(ExperimentalStdlibApi::class)
    override fun process(): List<PetData> {
        val list = mutableListOf<PetData>()
        var startIndex = 56
        var endIndex = 58
        val petsCount = info.substring(startIndex, endIndex).hexToInt()
        startIndex = endIndex
        for (i in 0 until petsCount) {
            endIndex = startIndex + 98
            val petBaseInfo = info.substring(startIndex, endIndex)
            val id = petBaseInfo.substring(0, 8).hexToInt()
            val lv = petBaseInfo.substring(8, 10).hexToInt()
            val exp = petBaseInfo.substring(10, 18).hexToInt()
            val character = petBaseInfo.substring(18, 20).hexToInt()
            val sex = petBaseInfo.substring(20, 22).hexToInt()
            val acquisitionTime = petBaseInfo.substring(22, 30).hexToInt()
            val hp = petBaseInfo.substring(46, 50).hexToInt()
            val attack = petBaseInfo.substring(50, 54).hexToInt()
            val defence = petBaseInfo.substring(54, 58).hexToInt()
            val magicAttack = petBaseInfo.substring(58, 62).hexToInt()
            val magicDefence = petBaseInfo.substring(62, 66).hexToInt()
            val speed = petBaseInfo.substring(66, 70).hexToInt()
            val currentHP = petBaseInfo.substring(70, 74).hexToInt()
            val hpGift = petBaseInfo.substring(74, 76).hexToInt()
            val attackGift = petBaseInfo.substring(76, 78).hexToInt()
            val defenceGift = petBaseInfo.substring(78, 80).hexToInt()
            val magicAttackGift = petBaseInfo.substring(80, 82).hexToInt()
            val magicDefenceGift = petBaseInfo.substring(82, 84).hexToInt()
            val speedGift = petBaseInfo.substring(84, 86).hexToInt()
            val hpEffort = petBaseInfo.substring(86, 88).hexToInt()
            val attackEffort = petBaseInfo.substring(88, 90).hexToInt()
            val defenceEffort = petBaseInfo.substring(90, 92).hexToInt()
            val magicAttackEffort = petBaseInfo.substring(92, 94).hexToInt()
            val magicDefenceEffort = petBaseInfo.substring(94, 96).hexToInt()
            val speedEffort = petBaseInfo.substring(96, 98).hexToInt()
            startIndex = endIndex
            val skillCount = info.substring(startIndex, startIndex + 4).hexToInt()
            startIndex += 4
            val skills = mutableListOf<SkillData>()
            repeat(skillCount) {
                endIndex = startIndex + 8
                val skillDataStr = info.substring(startIndex, endIndex)
                val skillId = skillDataStr.substring(0, 4)
                val skillPP = skillDataStr.substring(4, 6).hexToInt()
                val skill = SkillData(skillId, skillPP)
                skills.add(skill)
                startIndex = endIndex
            }
            startIndex += 16
            val petData = PetData(
                id = id,
                lv = lv,
                exp = exp,
                sex = sex,
                character = character,
                acquisitionTime = acquisitionTime.toLong(),
                maxHp = hp,
                hp = currentHP,
                attack = attack,
                defence = defence,
                magicAttack = magicAttack,
                magicDefence = magicDefence,
                speed = speed,
                hpGift = hpGift,
                attackGift = attackGift,
                defenceGift = defenceGift,
                magicAttackGift = magicAttackGift,
                magicDefenceGift = magicDefenceGift,
                speedGift = speedGift,
                hpEffort = hpEffort,
                attackEffort = attackEffort,
                defenceEffort = defenceEffort,
                magicAttackEffort = magicAttackEffort,
                magicDefenceEffort = magicDefenceEffort,
                speedEffort = speedEffort,
                skills = skills
            )
            list.add(petData)
        }
        return list
    }
}