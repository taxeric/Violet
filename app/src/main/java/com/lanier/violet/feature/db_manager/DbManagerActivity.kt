package com.lanier.violet.feature.db_manager

import com.lanier.violet.R
import com.lanier.violet.base.BaseAct
import com.lanier.violet.data.Constant
import com.lanier.violet.data.getStringFromMMKV
import com.lanier.violet.data.putStringToMMKV
import com.lanier.violet.database.VioletDatabase
import com.lanier.violet.databinding.ActivityDbManagerBinding
import com.lanier.violet.ext.toFormattedString
import com.lanier.violet.ext.toast
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

        showSyncTime(Constant.MMKVKey.KEY_SYNC_SPIRIT_DATA_TIME, viewbinding.viewSyncSpirit)
        showSyncTime(Constant.MMKVKey.KEY_SYNC_SKILL_DATA_TIME, viewbinding.viewSyncSkill)
        showSyncTime(Constant.MMKVKey.KEY_SYNC_PROP_DATA_TIME, viewbinding.viewSyncProp)
        showSyncTime(Constant.MMKVKey.KEY_SYNC_SCENE_DATA_TIME, viewbinding.viewSyncScene)
        showSyncTime(Constant.MMKVKey.KEY_SYNC_GAME_DATA_TIME, viewbinding.viewSyncGame)

        DbSyncHelper.cachePath = externalCacheDir!!.absolutePath
        println(">>>> ${DbSyncHelper.cachePath}")
    }

    override fun initData() {
    }

    override fun onSync(view: DbSyncView) {
        when (view.id) {
            R.id.viewSyncSpirit -> {
                DbSyncHelper.syncSpirit(
                    dao = VioletDatabase.db.spiritDao(),
                    onStart = { showLoading() },
                    onError = {
                        toast(it.message?: "process spirit data failed")
                        dismissLoading()
                    },
                    onSuccess = {
                        dismissLoading()
                        saveAndShowSyncTime(Constant.MMKVKey.KEY_SYNC_SPIRIT_DATA_TIME, viewbinding.viewSyncSpirit)
                    }
                )
            }
            R.id.viewSyncSkill -> {
                DbSyncHelper.syncSkill(
                    dao = VioletDatabase.db.skillDao(),
                    onStart = { showLoading() },
                    onError = {
                        toast(it.message?: "process skill data failed")
                        dismissLoading()
                    },
                    onSuccess = {
                        dismissLoading()
                        saveAndShowSyncTime(Constant.MMKVKey.KEY_SYNC_SKILL_DATA_TIME, viewbinding.viewSyncSkill)
                    }
                )
            }
            R.id.viewSyncProp -> {
                DbSyncHelper.syncProps(
                    dao = VioletDatabase.db.propDao(),
                    onStart = { showLoading() },
                    onError = {
                        toast(it.message?: "process prop data failed")
                        dismissLoading()
                    },
                    onSuccess = {
                        dismissLoading()
                        saveAndShowSyncTime(Constant.MMKVKey.KEY_SYNC_PROP_DATA_TIME, viewbinding.viewSyncProp)
                    }
                )
            }
            R.id.viewSyncScene -> {
                DbSyncHelper.syncScene(
                    dao = VioletDatabase.db.sceneDao(),
                    onStart = { showLoading() },
                    onError = {
                        toast(it.message?: "process scene data failed")
                        dismissLoading()
                    },
                    onSuccess = {
                        dismissLoading()
                        saveAndShowSyncTime(Constant.MMKVKey.KEY_SYNC_SCENE_DATA_TIME, viewbinding.viewSyncScene)
                    }
                )
            }
            R.id.viewSyncGame -> {
                DbSyncHelper.syncGame(
                    dao = VioletDatabase.db.gameDao(),
                    onStart = { showLoading() },
                    onError = {
                        toast(it.message?: "process game data failed")
                        dismissLoading()
                    },
                    onSuccess = {
                        dismissLoading()
                        saveAndShowSyncTime(Constant.MMKVKey.KEY_SYNC_GAME_DATA_TIME, viewbinding.viewSyncGame)
                    }
                )
            }
        }
    }

    private fun showSyncTime(key: String, view: DbSyncView, time: String? = null) {
        val tempTime = if (time.isNullOrEmpty()) getStringFromMMKV(key) else time
        view.refreshLastTime(tempTime)
    }

    private fun saveAndShowSyncTime(key: String, view: DbSyncView) {
        val time = System.currentTimeMillis()
        val formatTime = time.toFormattedString(format = "yyyy-MM-dd HH:mm:ss")
        putStringToMMKV(key, formatTime)
        showSyncTime(key, view, formatTime)
    }
}