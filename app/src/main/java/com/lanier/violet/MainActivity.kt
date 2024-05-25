package com.lanier.violet

import com.lanier.violet.client.RocoServerClient
import com.lanier.violet.databinding.ActivityMainBinding

class MainActivity(
    override val layoutId: Int = R.layout.activity_main
) : BaseAct<ActivityMainBinding>() {

    override fun onBind() {
        RocoServerClient.linkServer(
            onSuccess = {},
            onFailed = {
                toast("连接到洛克王国服务器失败: ${it.message}")
            }
        )
    }
}