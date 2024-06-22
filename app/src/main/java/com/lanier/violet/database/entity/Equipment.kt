package com.lanier.violet.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "equipment")
data class Equipment(
    @PrimaryKey val id: String,
    @ColumnInfo val level: String,
    @ColumnInfo val name: String,
    @ColumnInfo(name = "cdt_level") val cdtLevel: String,
    @ColumnInfo(name = "get_from") val getFrom: String,
    @ColumnInfo val price1: String,
    @ColumnInfo val price2: String,
    @ColumnInfo val price3: String,
    @ColumnInfo val price4: String,
    @ColumnInfo val type: String,
)