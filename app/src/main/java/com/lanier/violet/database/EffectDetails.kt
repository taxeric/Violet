package com.lanier.violet.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "effect_details")
data class EffectDetails(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    @ColumnInfo val description2: String,
    @ColumnInfo val property: String,
    @ColumnInfo val src: String,
)
