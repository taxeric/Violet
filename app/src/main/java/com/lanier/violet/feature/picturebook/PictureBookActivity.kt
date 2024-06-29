package com.lanier.violet.feature.picturebook

import com.google.android.material.tabs.TabLayout
import com.lanier.violet.R
import com.lanier.violet.base.BaseAct
import com.lanier.violet.databinding.ActivityPictureBookBinding
import com.lanier.violet.ext.onTabListener
import com.lanier.violet.utils.FragmentHelper

/**
 * 图鉴
 */
class PictureBookActivity(
    override val layoutId: Int = R.layout.activity_picture_book
) : BaseAct<ActivityPictureBookBinding>() {

    private val tabs = mutableListOf<FragmentHelper.SwitchFragment>()

    private val fragmentHelper : FragmentHelper by lazy {
        FragmentHelper(R.id.frameLayout, supportFragmentManager)
    }

    private lateinit var tabSelector : TabLayout.OnTabSelectedListener

    override fun onBind() {
        tabSelector = viewbinding.tabLayout.onTabListener(
            onTabSelected = { tab ->
                tab?.position?.let {
                    fragmentHelper.switchFra(it)
                }
            }
        )
        tabs.add(
            FragmentHelper.SwitchFragment(
                fragment = PictureBookFragment.newInstance(PictureBookType.Spirit),
                tag = getString(R.string.spirit),
                title = getString(R.string.spirit)
            )
        )
        tabs.add(
            FragmentHelper.SwitchFragment(
                fragment = PictureBookFragment.newInstance(PictureBookType.Skill),
                tag = getString(R.string.skill),
                title = getString(R.string.skill)
            )
        )
        tabs.add(
            FragmentHelper.SwitchFragment(
                fragment = PictureBookFragment.newInstance(PictureBookType.Prop),
                tag = getString(R.string.prop),
                title = getString(R.string.prop)
            )
        )
        fragmentHelper.setFragments(tabs)
        fragmentHelper.switchFra(0)

        viewbinding.backToTop.setOnClickListener {
            (tabs[fragmentHelper.showIndex].fragment as? PictureBookFragment)?.let {
                it.backToTop()
            }
        }
    }

    override fun initData() {
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::tabSelector.isInitialized) {
            viewbinding.tabLayout.removeOnTabSelectedListener(tabSelector)
        }
    }
}