package com.lanier.violet.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lanier.violet.database.entity.Skill

@Dao
interface SkillDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertAll(skills: List<Skill>) : List<Long>

    @Query("select * from skill where id=:id")
    fun getSkillById(id: String) : Skill

    @Query("select * from skill where name like '%'||:name||'%'")
    fun getSkillsByName(name: String) : List<Skill>
}