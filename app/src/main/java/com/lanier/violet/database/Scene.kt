package com.lanier.violet.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scene")
data class Scene(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,
    @ColumnInfo(name = "bg_music") val bgMusic: String,
)
