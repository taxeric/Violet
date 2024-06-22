package com.lanier.violet.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "skill_effect")
data class SkillEffect(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,
)
