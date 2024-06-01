package com.lanier.violet.feature.main.profile

import android.os.Bundle
import com.lanier.violet.R
import com.lanier.violet.base.BaseFra
import com.lanier.violet.databinding.FragmentMineBinding

/**
 * Created by 幻弦让叶
 * Date 2024/5/31 22:53
 */
class MineFragment private constructor(
    override val layoutId: Int = R.layout.fragment_mine
) : BaseFra<FragmentMineBinding>() {

    companion object {

        fun newInstance(): MineFragment {
            val args = Bundle()

            val fragment = MineFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onBind() {
    }
}