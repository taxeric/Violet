package com.lanier.violet.feature.db_manager

import com.lanier.violet.R
import com.lanier.violet.base.BaseAct
import com.lanier.violet.database.VioletDatabase
import com.lanier.violet.databinding.ActivityDbManagerBinding
import com.lanier.violet.views.DbSyncView

class DbManagerActivity(
    override val layoutId: Int = R.layout.activity_db_manager
) : BaseAct<ActivityDbManagerBinding>(), DbSyncView.OnSyncActionListener {

    override fun onBind() {
        viewbinding.viewSyncSpirit.onSyncActionListener = this
        viewbinding.viewSyncSkill.onSyncActionListener = this
        viewbinding.viewSyncProp.onSyncActionListener = this
        viewbinding.viewSyncScene.onSyncActionListener = this
        viewbinding.viewSyncGame.onSyncActionListener = this

        DbSyncHelper.cachePath = externalCacheDir!!.absolutePath
        println(">>>> ${DbSyncHelper.cachePath}")
    }

    override fun initData() {
    }

    override fun onSync(view: DbSyncView) {
        when (view.id) {
            R.id.viewSyncSpirit -> {
                DbSyncHelper.syncSpirit(
                    dao = VioletDatabase.db.spiritDao()
                )
            }
            R.id.viewSyncSkill -> {
                println(">>>> 同步技能")

            }
            R.id.viewSyncProp -> {
                println(">>>> 同步道具")

            }
            R.id.viewSyncScene -> {
                println(">>>> 同步场景")

            }
            R.id.viewSyncGame -> {
                println(">>>> 同步游戏")

            }
        }
    }
}