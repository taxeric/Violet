package com.lanier.violet

import android.app.Application
import com.lanier.violet.database.VioletDatabase

/**
 * Created by 幻弦让叶
 * Date 2024/5/19 23:48
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        VioletDatabase.init(this)
    }
}