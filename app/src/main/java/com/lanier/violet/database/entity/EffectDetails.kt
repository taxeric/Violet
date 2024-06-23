package com.lanier.violet.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lanier.violet.database.Constant

@Entity(tableName = Constant.TN_EFFECT_DETAILS)
data class EffectDetails(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    @ColumnInfo val description2: String,
    @ColumnInfo val property: String,
    @ColumnInfo val src: String,
)
