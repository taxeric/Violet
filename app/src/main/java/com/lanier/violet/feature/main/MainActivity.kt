package com.lanier.violet.feature.main

import com.google.android.material.tabs.TabLayout
import com.lanier.violet.base.BaseAct
import com.lanier.violet.R
import com.lanier.violet.client.RocoServerClient
import com.lanier.violet.databinding.ActivityMainBinding
import com.lanier.violet.ext.collect
import com.lanier.violet.ext.launchSafe
import com.lanier.violet.feature.main.event.ClientEvent
import com.lanier.violet.feature.main.event.UserInfoEvent
import com.lanier.violet.ext.toast
import com.lanier.violet.feature.main.home.HomeFragment
import com.lanier.violet.feature.main.pet_backpack.PetBackpackFragment
import com.lanier.violet.feature.main.profile.MineFragment
import com.lanier.violet.utils.FragmentHelper
import kotlinx.coroutines.launch

class MainActivity(
    override val layoutId: Int = R.layout.activity_main
) : BaseAct<ActivityMainBinding>() {

    private val tabs = mutableListOf<FragmentHelper.SwitchFragment>()

    private val helper by lazy {
        FragmentHelper(R.id.frameLayout, supportFragmentManager)
    }

    override fun onBind() {

        tabs.add(FragmentHelper.SwitchFragment(HomeFragment.newInstance(), title = getString(R.string.menu_home)))
        tabs.add(FragmentHelper.SwitchFragment(PetBackpackFragment.newInstance(), title = getString(R.string.menu_pet_backpack)))
        tabs.add(FragmentHelper.SwitchFragment(MineFragment.newInstance(), title = getString(R.string.menu_mine)))

        tabs.forEach {
            viewbinding.tabLayout.addTab(
                viewbinding.tabLayout
                    .newTab().apply {
                        tag = it.tag
                        text = it.title
                    }
            )
        }

        viewbinding.tabLayout.addOnTabSelectedListener(onTabSelectedListener)

        launchSafe {
            launch {
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
        }

        helper.setFragments(tabs)
        helper.switchFra(0)

        RocoServerClient.linkServer(
            onSuccess = {},
            onFailed = {
                if (RocoServerClient.isConnected) {
                    toast("消息处理失败: ${it.message}")
                } else {
                    toast("连接到洛克王国服务器失败: ${it.message}")
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        viewbinding.tabLayout.removeOnTabSelectedListener(onTabSelectedListener)
    }

    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let {
                val position = tab.position
                helper.switchFra(position)
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
        }
    }
}