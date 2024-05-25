package com.lanier.violet

import android.view.View

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
