package com.lanier.violet.views.rv

import androidx.recyclerview.widget.RecyclerView

/**
 * Created by 幻弦让叶
 * Date 2024/6/1 14:07
 */
interface OnItemClickListener {

    fun onItemClick(adapter: BaseRVAdapter<*, *>, position: Int)
}