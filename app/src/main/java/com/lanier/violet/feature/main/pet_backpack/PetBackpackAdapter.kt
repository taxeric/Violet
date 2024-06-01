package com.lanier.violet.feature.main.pet_backpack

import coil.load
import com.lanier.violet.R
import com.lanier.violet.databinding.ItemPetBackpackBinding
import com.lanier.violet.feature.pet.data.PetData
import com.lanier.violet.views.rv.BaseViewBindingAdapter
import com.lanier.violet.views.rv.ViewBindHolder

/**
 * Created by 幻弦让叶
 * Date 2024/6/1 14:01
 */
class PetBackpackAdapter : BaseViewBindingAdapter<ItemPetBackpackBinding, PetData>() {

    private var mCurrentSelectedIndex = -1

    val currentSelectedPet: PetData?
        get() {
            return if (mCurrentSelectedIndex < 0) null
            else data[mCurrentSelectedIndex]
        }

    override fun onResetData() {
        mCurrentSelectedIndex = -1
    }

    fun modifySelectedIndex(index: Int) {
        if (mCurrentSelectedIndex == index) return
        if (mCurrentSelectedIndex != -1) {
            data[mCurrentSelectedIndex].selected = false
            notifyItemChanged(mCurrentSelectedIndex, Any())
        }
        data[index].selected = true
        notifyItemChanged(index, Any())
        mCurrentSelectedIndex = index
    }

    override fun onBind(
        holder: ViewBindHolder<ItemPetBackpackBinding>,
        data: PetData,
        position: Int
    ) {
        val mId = when {
            data.id < 10 -> {
                "00${data.id}"
            }
            data.id in 10..99 -> {
                "0${data.id}"
            }
            else -> {
                "${data.id}"
            }
        }
        val url = "https://res.17roco.qq.com/res/combat/icons/$mId-.png"
        holder.viewbinding.ivAvatar.load(url)
        holder.viewbinding.tvName.text = buildString {
            append(context.getString(R.string.m_lv, "${data.lv}"))
            append("\n")
            context.getString(R.string.m_hp_max_hp, "${data.hp}", "${data.maxHp}")
        }
        holder.viewbinding.tvName.setTextColor(if (data.selected) {
            context.getColor(R.color.md_theme_inversePrimary_mediumContrast)
        } else {
            context.getColor(R.color.md_theme_onBackground)
        })
    }
}
