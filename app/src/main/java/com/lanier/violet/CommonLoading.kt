package com.lanier.violet

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.lanier.violet.databinding.DialogCommonLoadingBinding

/**
 * Created by 幻弦让叶
 * Date 2024/4/5 15:57
 */
class CommonLoading : DialogFragment() {

    companion object {

        fun newInstance(): CommonLoading {
            val args = Bundle()

            val fragment = CommonLoading()
            fragment.arguments = args
            return fragment
        }
    }

    private val binding by lazy {
        DialogCommonLoadingBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (view.parent as? View)?.setBackgroundColor(Color.TRANSPARENT)
        dialog?.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
    }
}