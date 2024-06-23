package com.lanier.violet.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lanier.violet.database.Constant

@Entity(tableName = Constant.TN_TALENT)
data class Talent(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,
    @ColumnInfo val desc: String,
)
