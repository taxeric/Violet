package com.lanier.violet.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "skill")
data class Skill(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    @ColumnInfo val power: String,
    @ColumnInfo(name = "pp_max") val ppMax: String,
    @ColumnInfo val speed: String,
    @ColumnInfo val property: String,
    @ColumnInfo(name = "attack_type") val attackType: String,
    @ColumnInfo(name = "damage_type") val damageType: String,
    @ColumnInfo val src: String,
)
