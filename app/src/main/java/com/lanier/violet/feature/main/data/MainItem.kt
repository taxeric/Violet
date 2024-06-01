package com.lanier.violet.feature.main.data

import androidx.fragment.app.Fragment

/**
 * Created by 幻弦让叶
 * Date 2024/5/31 22:35
 */
data class MainItem(
    val index: Int,
    val fragment: Fragment,
    val tag: String = fragment::class.java.simpleName,
    val title: CharSequence = "",
)
