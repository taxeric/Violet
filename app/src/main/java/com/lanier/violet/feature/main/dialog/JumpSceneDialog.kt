package com.lanier.violet.feature.main.dialog

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.lanier.violet.databinding.DialogJumpSceneBinding

/**
 * Created by 幻弦让叶
 * Date 2024/6/1 23:13
 */
class JumpSceneDialog private constructor() : DialogFragment() {

    companion object {

        fun show(act: FragmentActivity) {
            val dialog = JumpSceneDialog()
            dialog.show(act.supportFragmentManager, this::class.java.simpleName)
        }
    }

    private val viewbinding by lazy { DialogJumpSceneBinding.inflate(layoutInflater) }

    private var sceneIdFirstMode = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return viewbinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewbinding.rbSceneIdFirst.setOnCheckedChangeListener { _, isChecked ->
            sceneIdFirstMode = isChecked
            if (isChecked) {
                viewbinding.etScene.setRawInputType(InputType.TYPE_NUMBER_FLAG_SIGNED)
            } else {
                viewbinding.etScene.setRawInputType(InputType.TYPE_TEXT_VARIATION_NORMAL)
            }
        }
        viewbinding.btnGO.setOnClickListener {
            gotoScene()
            dismiss()
        }
    }

    private fun gotoScene() {
        val inputSceneContent = viewbinding.etScene.text.toString()
        if (inputSceneContent.isNullOrEmpty()) return
        if (sceneIdFirstMode) {
            try {
                val sceneId = inputSceneContent.toInt()
            } catch (e: Throwable) {
                return
            }
        } else {
        }
    }
}