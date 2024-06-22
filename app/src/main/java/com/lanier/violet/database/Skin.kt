package com.lanier.violet.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "skin")
data class Skin(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,
)
