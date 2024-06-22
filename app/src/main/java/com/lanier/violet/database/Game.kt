package com.lanier.violet.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game")
data class Game(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,
    @ColumnInfo val tips: String,
    @ColumnInfo val type: String,
    @ColumnInfo(name = "scene_id") val sceneId: String,
)