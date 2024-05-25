package com.lanier.violet

import com.lanier.violet.client.RocoServerClient
import com.lanier.violet.databinding.ActivityMainBinding
import com.lanier.violet.ext.collect
import com.lanier.violet.ext.launchSafe
import com.lanier.violet.feature.main.event.ClientEvent
import com.lanier.violet.feature.main.event.SceneEvent

class MainActivity(
    override val layoutId: Int = R.layout.activity_main
) : BaseAct<ActivityMainBinding>() {

    override fun onBind() {

        launchSafe {
            launchSafe {
                collect<ClientEvent> {
                    when (it.action) {
                        ClientEvent.ACTION_CONNECTING -> showLoading()
                        ClientEvent.ACTION_ENTER_CHANNEL -> {
                            toast("已进入频道: ${RocoServerClient.channel}")
                        }
                        else -> dismissLoading()
                    }
                }
            }
            launchSafe {
                collect<SceneEvent> {
                    viewbinding.tvChannelAndScene.text = buildString {
                        append("频道: ${RocoServerClient.channel}")
                        append("\n")
                        append("场景ID: ${it.sceneId}")
                    }
                }
            }
        }

        RocoServerClient.linkServer(
            onSuccess = {},
            onFailed = {
                toast("连接到洛克王国服务器失败: ${it.message}")
            }
        )
    }
}