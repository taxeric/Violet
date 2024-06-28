package com.lanier.violet.feature.db_manager.processor

import com.lanier.violet.database.Constant
import com.lanier.violet.database.dao.SkillDao
import com.lanier.violet.database.entity.EffectDetails
import com.lanier.violet.database.entity.Skill
import com.lanier.violet.database.entity.SkillEffect
import com.lanier.violet.ext.calcRunTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SkillDbProcessor(
    filepath: String,
    private val dao: SkillDao,
) : AbsCombineProcessor(filepath) {

    companion object {

        private const val SKILL = "skills.txt"
        private const val SKILL_EFFECT = "skill_effects.txt"
        private const val SKILL_EFFECT_DESC = "effect_desc.txt"
    }

    override val path: String
        get() = "/spirit/"

    override suspend fun sync(
        onStart: (() -> Unit)?,
        onCompleted: (() -> Unit)?,
    ) {
        withContext(Dispatchers.IO) {
            onStart?.invoke()
            launch {
                calcRunTime("skill") {
                    val result = readFromOrigin(Constant.TN_SKILL, SKILL)
                    val skills = parseSkills(result.second)
                    dao.upsertSkillAll(skills)
                }
            }
            launch {
                calcRunTime("skill_effect") {
                    val result = readFromOrigin(Constant.TN_SKILL_EFFECT, SKILL_EFFECT)
                    val skillEffects = parseSkillEffects(result.second)
                    dao.upsertSkillEffectsAll(skillEffects)
                }
            }
            launch {
                calcRunTime("effect_desc") {
                    val result = readFromOrigin(Constant.TN_EFFECT_DETAILS, SKILL_EFFECT_DESC)
                    val effectDetails = parseEffectDetails(result.second)
                    dao.upsertEffectDetailsAll(effectDetails)
                }
            }
            onCompleted?.invoke()
        }
    }

    private fun parseEffectDetails(text: String) : List<EffectDetails> {
        val effectDetailsTexts = text.split("<SpiritSkillDes ").drop(1).map { "<SpiritSkillDes $it" }
        return effectDetailsTexts.map {
            val attributes = parseAttributes(it, "SpiritSkillDes")
            EffectDetails(
                id = attributes["id"] ?: "",
                name = attributes["name"] ?: "",
                description = attributes["description"] ?: "",
                description2 = attributes["description2"] ?: "",
                property = attributes["property"] ?: "",
                src = attributes["src"] ?: ""
            )
        }
    }

    private fun parseSkillEffects(text: String) : List<SkillEffect> {
        val skillEffectTexts = text.split("<PropertyDes ").drop(1).map { "<PropertyDes $it" }
        return skillEffectTexts.map {
            val attributes = parseAttributes(it, "PropertyDes")
            SkillEffect(
                id = attributes["id"] ?: "",
                name = attributes["name"] ?: ""
            )
        }
    }

    private fun parseSkills(text: String) : List<Skill> {
        val skillTexts = text.split("<SpiritSkillDes ").drop(1).map { "<SpiritSkillDes $it" }
        return skillTexts.map {
            val attributes = parseAttributes(it, "SpiritSkillDes")
            Skill(
                id = attributes["id"] ?: "",
                name = attributes["name"] ?: "",
                description = attributes["description"] ?: "",
                attackType = attributes["attackType"] ?: "",
                damageType = attributes["damageType"] ?: "",
                power = attributes["power"] ?: "",
                ppMax = attributes["ppMax"] ?: "",
                property = attributes["property"] ?: "",
                speed = attributes["speed"] ?: "",
                src = attributes["src"] ?: ""
            )
        }
    }
}