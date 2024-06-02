package com.lanier.violet.feature.main.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.lanier.violet.R
import com.lanier.violet.data.UserData
import com.lanier.violet.databinding.DialogFarmBinding
import com.lanier.violet.ext.collect
import com.lanier.violet.ext.launchSafe
import com.lanier.violet.feature.main.event.FarmEvent

/**
 * Created by 幻弦让叶
 * Date 2024/6/2 16:12
 */
class FarmDialog private constructor() : DialogFragment() {

    companion object {

        fun show(act: FragmentActivity) {
            val dialog = FarmDialog()
            dialog.show(act.supportFragmentManager, this::class.java.simpleName)
        }
    }

    private val viewbinding by lazy { DialogFarmBinding.inflate(layoutInflater) }

    private var actionSuccessCount = 0
    private var actionFailedCount = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return viewbinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewbinding.btnPlant.setOnClickListener {  }
        viewbinding.btnHarvest.setOnClickListener {  }

        launchSafe {
            collect<FarmEvent> {
                viewbinding.tvFarmHandleLand.text = buildFarmRowData(it.plantId)
                if (it.success) actionSuccessCount ++
                else actionFailedCount ++
                if (it.plantId < UserData.LAND_MAX_INDEX) {
                    viewbinding.tvFarmLandStatus.text = getString(R.string.farm_operating, "${it.plantId}")
                } else {
                    viewbinding.tvFarmLandStatus.text = getString(
                        R.string.farm_operation_result,
                        "$actionSuccessCount", "$actionFailedCount", "${it.action}"
                    )
                }
            }
        }
    }

    private fun buildFarmRowData(index: Int) : String {
        return buildString {
            append(getString(R.string.farm_land, "$index"))
        }
    }
}