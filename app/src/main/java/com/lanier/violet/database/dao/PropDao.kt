package com.lanier.violet.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lanier.violet.database.entity.Prop

@Dao
interface PropDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertAllProps(prop: List<Prop>) : List<Long>

    @Query("select * from prop where id=:id")
    fun getPropById(id: String) : Prop
}