package com.lanier.violet.views.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 * Created by 幻弦让叶
 * Date 2024/6/1 15:17
 */
abstract class BaseViewBindingAdapter<VB: ViewBinding, T> : BaseRVAdapter<ViewBindHolder<VB>, T>() {

    override val layoutId: Int
        get() = 0

    override fun createVH(parent: ViewGroup): ViewBindHolder<VB> {
        val tClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val inflate = tClass.getDeclaredMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        val mBinding = inflate.invoke(null, LayoutInflater.from(parent.context), parent, false) as VB
        return ViewBindHolder(mBinding, mBinding.root)
    }
}

class ViewBindHolder<VB: ViewBinding>(val viewbinding: VB, view: View) : BaseViewHolder(view)