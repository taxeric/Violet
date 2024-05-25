package com.lanier.violet

import com.lanier.violet.client.RocoServerClient
import com.lanier.violet.databinding.ActivityMainBinding
import com.lanier.violet.ext.collect
import com.lanier.violet.ext.launchSafe
import com.lanier.violet.feature.main.event.ClientEvent

class MainActivity(
    override val layoutId: Int = R.layout.activity_main
) : BaseAct<ActivityMainBinding>() {

    override fun onBind() {

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

        RocoServerClient.linkServer(
            onSuccess = {},
            onFailed = {
                toast("连接到洛克王国服务器失败: ${it.message}")
            }
        )
    }
}