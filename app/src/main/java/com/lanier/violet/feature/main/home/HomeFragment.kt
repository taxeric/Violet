package com.lanier.violet.feature.main.home

import android.os.Bundle
import com.lanier.violet.R
import com.lanier.violet.base.BaseFra
import com.lanier.violet.client.RocoServerClient
import com.lanier.violet.data.UserData
import com.lanier.violet.databinding.FragmentHomeBinding
import com.lanier.violet.ext.collect
import com.lanier.violet.ext.launchSafe
import com.lanier.violet.feature.main.dialog.FarmDialog
import com.lanier.violet.feature.main.dialog.JumpSceneDialog
import com.lanier.violet.feature.main.event.ClientEvent
import com.lanier.violet.feature.main.event.SceneEvent
import com.lanier.violet.feature.main.event.UserInfoEvent
import kotlinx.coroutines.launch

/**
 * Created by 幻弦让叶
 * Date 2024/5/31 22:45
 */
class HomeFragment private constructor(
    override val layoutId: Int = R.layout.fragment_home
) : BaseFra<FragmentHomeBinding>() {

    companion object {

        fun newInstance(): HomeFragment {
            val args = Bundle()

            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onBind() {

        launchSafe {
            launch {
                collect<ClientEvent> {
                    when (it.action) {
                        ClientEvent.ACTION_CONNECTING -> showLoading()
                        ClientEvent.ACTION_ENTER_CHANNEL -> {
//                            requestPersonalInfo()
                        }
                        else -> dismissLoading()
                    }
                }
            }

            launch {
                launch {
                    collect<UserInfoEvent> {
                    }
                }
            }

            launch {
                collect<SceneEvent> {
                    viewbinding.tvChannelAndScene.text = buildString {
                        append("频道: ${RocoServerClient.channel}")
                        append("\t")
                        append("场景ID: ${it.sceneId}")
                        if (it.lastSceneId != -1) {
                            append("\t")
                            append("上个场景ID: ${it.lastSceneId}")
                        }
                    }
                }
            }
        }

        viewbinding.btnInfo.setOnClickListener {
            requestPersonalInfo()
        }

        viewbinding.tvJumpScene.setOnClickListener {
            activity?.let { JumpSceneDialog.show(it) }
        }

        viewbinding.tvFarm.setOnClickListener {
            activity?.let { FarmDialog.show(it) }
        }
    }

    /**
     * 请求个人信息
     */
    private fun requestPersonalInfo() {
        val command = buildString {
            append("95 27 00 00 00 03 00 15")
            append(UserData.hexQQ)
            append("00 00 00 40 00 00 00 04")
            append(UserData.hexQQ)
        }
        RocoServerClient.sendCommand(command)
    }
}