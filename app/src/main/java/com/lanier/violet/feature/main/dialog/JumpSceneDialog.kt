package com.lanier.violet.feature.main.dialog

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.lanier.violet.R
import com.lanier.violet.client.RocoServerClient
import com.lanier.violet.data.UserData
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
        viewbinding.tvCurrentScene.text = getString(R.string.current_scene_id, "${UserData.currentSceneId}")
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

    @OptIn(ExperimentalStdlibApi::class)
    private fun gotoScene() {
        val inputSceneContent = viewbinding.etScene.text.toString()
        if (inputSceneContent.isNullOrEmpty()) return
        if (sceneIdFirstMode) {
            try {
                val sceneId = inputSceneContent.toInt().toHexString()
                val currentSceneId = UserData.currentSceneId.toHexString()
                var last = "0"
                val message = buildString {
                    append("95 27 00 00 00 03 00 04")
                    append(UserData.hexQQ)
                    append("00 00 00 00 00 00 00 0A")
                    append(currentSceneId.substring(4, currentSceneId.length))
                    append(sceneId.substring(4, sceneId.length))
                    append("00 00 00 00 00 0")
                    append(last)
                }
                RocoServerClient.sendCommand(message)
            } catch (e: Throwable) {
                return
            }
        } else {
        }
    }
}