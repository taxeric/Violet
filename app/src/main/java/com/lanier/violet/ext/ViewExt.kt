package com.lanier.violet.ext

import android.view.View
import com.google.android.material.tabs.TabLayout

/**
 * Created by 幻弦让叶
 * Date 2024/5/25 16:50
 */
fun View.visibleOrInvisible(visible: Boolean) {
    if (visible) visible() else invisible()
}

fun View.visibleOrGone(visible: Boolean) {
    if (visible) visible() else gone()
}

fun View.visible() {
    visibility = View.VISIBLE
}
fun View.invisible() {
    visibility = View.INVISIBLE
}


fun View.gone() {
    visibility = View.GONE
}

fun TabLayout.onTabListener(
    onTabSelected: ((TabLayout.Tab?) -> Unit)? = null,
    onTabUnselected: ((TabLayout.Tab?) -> Unit)? = null,
    onTabReselected: ((TabLayout.Tab?) -> Unit)? = null,
) : TabLayout.OnTabSelectedListener {
    val onTabSelector = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            onTabSelected?.invoke(tab)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            onTabUnselected?.invoke(tab)
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
            onTabReselected?.invoke(tab)
        }
    }
    addOnTabSelectedListener(onTabSelector)
    return onTabSelector
}

