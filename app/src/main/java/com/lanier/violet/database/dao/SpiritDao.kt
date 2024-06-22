package com.lanier.violet.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.lanier.violet.database.entity.Equipment
import com.lanier.violet.database.entity.Property
import com.lanier.violet.database.entity.Spirit
import com.lanier.violet.database.entity.SpiritGroup
import com.lanier.violet.database.entity.Talent

@Dao
interface SpiritDao {

    @Insert(onConflict = REPLACE)
    fun upsertAllSpirits(spirits: List<Spirit>) : List<Long>

    @Insert(onConflict = REPLACE)
    fun upsertAllProperty(properties: List<Property>) : List<Long>

    @Insert(onConflict = REPLACE)
    fun upsertAllEggGroup(groups: List<SpiritGroup>) : List<Long>

    @Insert(onConflict = REPLACE)
    fun upsertAllEquipments(equipments: List<Equipment>) : List<Long>

    @Insert(onConflict = REPLACE)
    fun upsertAllTalents(talents: List<Talent>) : List<Long>

    @Query("select * from spirit where id=:id")
    fun getSpiritById(id: String) : Spirit

    @Query("select * from spirit where name like '%'||:name||'%'")
    fun getSpiritsByName(name: String) : List<Spirit>

    @Query("select * from spirit where group_id=:groupId")
    fun getSpiritsByGroupId(groupId: String) : List<Spirit>

    @Query("select * from spirit where property=:propertyId")
    fun getSpiritsByPropertyId(propertyId: String) : List<Spirit>

    @Query("select * from property")
    fun getAllProps() : List<Property>

    @Query("select * from spirit_group")
    fun getAllEggGroup() : List<SpiritGroup>

    @Query("select * from equipment")
    fun getAllEquipments() : List<Equipment>

    @Query("select * from talent")
    fun getAllTalents() : List<Talent>
}