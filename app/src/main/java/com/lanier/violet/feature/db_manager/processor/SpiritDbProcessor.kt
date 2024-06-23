package com.lanier.violet.feature.db_manager.processor

import com.lanier.violet.database.Constant
import com.lanier.violet.database.dao.SpiritDao
import com.lanier.violet.database.entity.Equipment
import com.lanier.violet.database.entity.Property
import com.lanier.violet.database.entity.Skin
import com.lanier.violet.database.entity.Spirit
import com.lanier.violet.database.entity.SpiritGroup
import com.lanier.violet.database.entity.Talent
import com.lanier.violet.ext.calcRunTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SpiritDbProcessor(
    filepath: String,
    private val dao: SpiritDao,
) : AbsCombineProcessor(filepath) {

    companion object {

        private const val SPIRITS = "spirits.txt"
        private const val SKINS = "skins.txt"
        private const val PROPERTY = "property.txt"
        private const val GROUP = "group.txt"
        private const val TALENTS = "talents.txt"
        private const val EQUIPMENT = "equipment.txt"
    }

    override val path: String
        get() = "/spirit/"

    override suspend fun sync(
        onStart: (() -> Unit)?,
        onError: ((Throwable) -> Unit)?,
        onCompleted: (() -> Unit)?,
    ) {
        withContext(Dispatchers.IO) {
            launch {
                calcRunTime("spirit") {
                    val result = readFromOrigin(Constant.TN_SPIRIT, SPIRITS)
                    val spirits = parseSpirits(result.second)
                    dao.upsertAllSpirits(spirits)
                }
            }
            launch {
                calcRunTime("skin") {
                    val result = readFromOrigin(Constant.TN_SKIN, SKINS)
                    val skins = parseSkins(result.second)
                    dao.upsertAllSkins(skins)
                }
            }
            launch {
                calcRunTime("property") {
                    val result = readFromOrigin(Constant.TN_PROPERTY, PROPERTY)
                    val properties = parseProperties(result.second)
                    dao.upsertAllProperty(properties)
                }
            }
            launch {
                calcRunTime("group") {
                    val result = readFromOrigin(Constant.TN_SPIRIT_GROUP, GROUP)
                    val eggGroups = parseEggGroups(result.second)
                    dao.upsertAllEggGroup(eggGroups)
                }
            }
            launch {
                calcRunTime("equipment") {
                    val result = readFromOrigin(Constant.TN_EQUIPMENT, EQUIPMENT)
                    val equipments = parseEquipments(result.second)
                    dao.upsertAllEquipments(equipments)
                }
            }
            launch {
                calcRunTime("talent") {
                    val result = readFromOrigin(Constant.TN_TALENT, TALENTS)
                    val talents = parseTalents(result.second)
                    dao.upsertAllTalents(talents)
                }
            }
        }
    }

    private fun parseSpirits(text: String) : List<Spirit> {
        val spiritTexts = text.split("<SpiritDes ").drop(1).map { "<SpiritDes $it" }
        return spiritTexts.map { parseToSpirit(it) }
    }

    private fun parseSkins(text: String) : List<Skin> {
        val skinTexts = text.split("<spiritSkinDes ").drop(1).map { "<spiritSkinDes $it" }
        return skinTexts.map { parseSpiritSkin(it) }
    }

    private fun parseProperties(text: String) : List<Property> {
        val propertyTexts = text.split("<PropertyDes ").drop(1).map { "<PropertyDes $it" }
        return propertyTexts.map { parseProperty(it) }
    }

    private fun parseEggGroups(text: String) : List<SpiritGroup> {
        val groups = text.split("<groupTypeDes ").drop(1).map { "<groupTypeDes $it" }
        return groups.map { parseEggGroup(it) }
    }

    private fun parseTalents(text: String) : List<Talent> {
        val talents = text.split("<talentDes ").drop(1).map { "<talentDes $it" }
        return talents.map { parseTalent(it) }
    }

    private fun parseEquipments(text: String) : List<Equipment> {
        val equipments = text.split("<EquipmentDes ").drop(1).map { "<EquipmentDes $it" }
        return equipments.map { parseEquipment(it) }
    }

    private fun parseEquipment(text: String) : Equipment {
        val attributes = parseAttributes(text, "EquipmentDes")
        return Equipment(
            id = attributes["id"] ?: "",
            name = attributes["name"] ?: "",
            level = attributes["level"] ?: "",
            cdtLevel = attributes["cdtLevel"] ?: "",
            getFrom = attributes["getFrom"] ?: "",
            price1 = attributes["price1"] ?: "",
            price2 = attributes["price2"] ?: "",
            price3 = attributes["price3"] ?: "",
            price4 = attributes["price4"] ?: "",
            type = attributes["type"] ?: ""
        )
    }

    private fun parseTalent(text: String) : Talent {
        val attributes = parseAttributes(text, "talentDes")
        return Talent(
            id = attributes["id"] ?: "",
            name = attributes["name"] ?: "",
            desc = attributes["des"] ?: ""
        )
    }

    private fun parseEggGroup(text: String) : SpiritGroup {
        val attributes = parseAttributes(text, "groupTypeDes")
        return SpiritGroup(
            id = attributes["id"] ?: "",
            name = attributes["name"] ?: ""
        )
    }

    private fun parseProperty(text: String) : Property {
        val attributes = parseAttributes(text, "PropertyDes")
        return Property(
            id = attributes["id"] ?: "",
            name = attributes["name"] ?: ""
        )
    }

    private fun parseSpiritSkin(text: String) : Skin {
        val attributes = parseAttributes(text, "spiritSkinDes")
        return Skin(
            id = attributes["id"] ?: "",
            name = attributes["name"] ?: "",
        )
    }

    private fun parseToSpirit(text: String) : Spirit {
        val attributes = parseAttributes(text, "SpiritDes")
//        val attributes = text
//            .removePrefix("<SpiritDes ")
//            .removeSuffix("\n")
//            .removeSuffix("/>")
//            .split(" (?=\\w+=\"[^\"]*\")".toRegex())
//            .map { it.split("=") }
//            .associate { it[0] to it[1].removeSurrounding("\"") }

        return Spirit(
            id = attributes["id"] ?: "",
            name = attributes["name"] ?: "",
            iconSrc = attributes["iconSrc"] ?: "",
            interest = attributes["interest"] ?: "",
            color = attributes["color"] ?: "",
            height = attributes["height"] ?: "",
            weight = attributes["weight"] ?: "",
            group = attributes["group"] ?: "",
            firstID = attributes["firstID"] ?: "",
            getForm = attributes["getForm"] ?: "",
            description = attributes["description"] ?: "",
            sm = attributes["sm"] ?: "",
            wg = attributes["wg"] ?: "",
            fy = attributes["fy"] ?: "",
            mg = attributes["mg"] ?: "",
            mk = attributes["mk"] ?: "",
            sd = attributes["sd"] ?: "",
            evolutionFormID = attributes["EvolutionFormID"] ?: "",
            evolutionToIDs = attributes["EvolutiontoIDs"] ?: "",
            endTime = attributes["endTime"] ?: "",
            expType = attributes["expType"] ?: "",
            property = attributes["features"] ?: "",
            habitat = attributes["habitat"] ?: "",
            isInBook = attributes["isInBook"] ?: "",
            mType = attributes["Mtype"] ?: "",
            mspeed = attributes["mspeed"] ?: "",
            previewSrc = attributes["previewSrc"] ?: "",
            propoLevel = attributes["propoLevel"] ?: "",
            skinNum = attributes["skinnum"] ?: "",
            src = attributes["src"] ?: "",
            state = attributes["state"] ?: "",
            catchRate = attributes["catchrate"] ?: ""
        )
    }
}