package com.lanier.violet.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lanier.violet.database.dao.GameDao
import com.lanier.violet.database.dao.PropDao
import com.lanier.violet.database.dao.SceneDao
import com.lanier.violet.database.dao.SkillDao
import com.lanier.violet.database.dao.SpiritDao
import com.lanier.violet.database.entity.EffectDetails
import com.lanier.violet.database.entity.Equipment
import com.lanier.violet.database.entity.Game
import com.lanier.violet.database.entity.Prop
import com.lanier.violet.database.entity.Property
import com.lanier.violet.database.entity.Scene
import com.lanier.violet.database.entity.Seed
import com.lanier.violet.database.entity.Skill
import com.lanier.violet.database.entity.SkillEffect
import com.lanier.violet.database.entity.Skin
import com.lanier.violet.database.entity.Spirit
import com.lanier.violet.database.entity.SpiritGroup
import com.lanier.violet.database.entity.Talent

@Database(
    entities = [
        EffectDetails::class,
        Equipment::class,
        Game::class,
        Prop::class,
        Property::class,
        Scene::class,
        Seed::class,
        Skill::class,
        SkillEffect::class,
        Skin::class,
        Spirit::class,
        SpiritGroup::class,
        Talent::class,
    ],
    version = 1
)
abstract class VioletDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "violet-db"

        lateinit var db: VioletDatabase

        fun init(context: Context) {
            db = Room.databaseBuilder(context, VioletDatabase::class.java, DB_NAME)
                .build()
        }
    }

    abstract fun spiritDao(): SpiritDao
    abstract fun skillDao(): SkillDao
    abstract fun gameDao(): GameDao
    abstract fun sceneDao(): SceneDao
    abstract fun propDao(): PropDao
}