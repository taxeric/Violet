package com.lanier.violet.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.lanier.violet.views.CommonLoading

abstract class BaseFra<VDB : ViewDataBinding> : Fragment() {

    protected lateinit var viewbinding: VDB

    private var loadingDialog : CommonLoading? = null

    abstract val layoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewbinding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
        return viewbinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBind()
        initData()
    }

    protected open fun onBind() {}
    protected open fun initData() {}

    protected open fun showLoading() {
        loadingDialog?.dismiss()
        loadingDialog = CommonLoading.newInstance()
        loadingDialog?.show(parentFragmentManager, CommonLoading::class.java.simpleName)
    }

    protected open fun dismissLoading() {
        loadingDialog?.dismiss()
    }
}