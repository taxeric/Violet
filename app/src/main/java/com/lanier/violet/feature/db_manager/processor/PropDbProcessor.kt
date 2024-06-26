package com.lanier.violet.feature.db_manager.processor

import com.lanier.violet.database.Constant
import com.lanier.violet.database.dao.PropDao
import com.lanier.violet.database.entity.Prop
import com.lanier.violet.database.entity.Seed
import com.lanier.violet.ext.calcRunTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PropDbProcessor(
    filepath: String,
    private val dao: PropDao
) : AbsCombineProcessor(filepath) {

    companion object {

        private const val PROPS = "common_props.txt"
        private const val SEED = "farm_seeds.txt"
    }

    override val path: String
        get() = "/props/"

    override suspend fun sync(
        onStart: (() -> Unit)?,
        onError: ((Throwable) -> Unit)?,
        onCompleted: (() -> Unit)?,
    ) {
        withContext(Dispatchers.IO) {
            launch {
                calcRunTime {
                    val result = readFromOrigin(Constant.TN_PROP, PROPS)
                    val props = parsePotionData(result.second)
                    dao.upsertAllProps(props)
                }
            }
            launch {
                calcRunTime {
                    val result = readFromOrigin(Constant.TN_SEED, SEED)
                    val seeds = parseSeeds(result.second)
                    dao.upsertAllSeeds(seeds)
                }
            }
        }
    }

    private fun parsePotionData(data: String): List<Prop> {
        val pattern = Regex(
            """<ID>(\d+)</ID>\s*<Name>([^<]+)</Name>\s*<Price>(\d+)</Price>\s*<Desc><!\[CDATA\[([^]]+)]\]></Desc>\s*<Unique>(\d+)</Unique>\s*<ExpireTime>(\d+)</ExpireTime>"""
        )
        val matches = pattern.findAll(data)
        return matches.map { match ->
            val (id, name, price, desc, unique, expireTime) = match.destructured
            Prop(
                id = id,
                name = name,
                price = price,
                desc = desc
            )
        }.toList()
    }

    private fun parseSeeds(text: String) : List<Seed> {
        val seedTexts = text.split("<ManorSeedDes ").drop(1).map { "<ManorSeedDes $it" }
        return seedTexts.map {
            val attributes = parseAttributes(it, "ManorSeedDes")
            Seed(
                id = attributes["id"] ?: "",
                name = attributes["name"] ?: "",
                buyLv = attributes["buyLevel"] ?: "",
                grownTime = attributes["grownTime"] ?: "",
                harvestExpDesc = attributes["harvestExpdes"] ?: "",
                harvestId = attributes["harvestId"] ?: "",
                harvestNumber = attributes["harvestN"] ?: "",
                season = attributes["seasonN"] ?: "",
                seedExp = attributes["seedExp"] ?: "",
                proficiencyExp = attributes["proficiencyExp"] ?: "",
                proficiencyType = attributes["proficiencyType"] ?: "",
                proficiencyLv = attributes["proficiencylv"] ?: "",
                desc = attributes["des"] ?: ""
            )
        }
    }
}