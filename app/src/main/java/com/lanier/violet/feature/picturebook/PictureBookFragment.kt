package com.lanier.violet.feature.picturebook

import android.content.res.Configuration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lanier.violet.R
import com.lanier.violet.base.BaseFra
import com.lanier.violet.database.entity.Prop
import com.lanier.violet.database.entity.Skill
import com.lanier.violet.database.entity.Spirit
import com.lanier.violet.databinding.FragmentPictureBookBinding
import com.lanier.violet.databinding.ItemPictureBookPropBinding
import com.lanier.violet.databinding.ItemPictureBookSkillBinding
import com.lanier.violet.databinding.ItemPictureBookSpiritBinding
import com.lanier.violet.ext.invisible
import com.lanier.violet.ext.visible
import com.lanier.violet.views.rv.BaseViewBindingAdapter
import com.lanier.violet.views.rv.ViewBindHolder

class PictureBookFragment(
    override val layoutId: Int = R.layout.fragment_picture_book
) : BaseFra<FragmentPictureBookBinding>() {
    
    companion object {
        
        fun newInstance(type: PictureBookType) : PictureBookFragment {
            val fragment = PictureBookFragment()
            fragment.type = type
            return fragment
        }
    }
    
    private var type : PictureBookType = PictureBookType.Spirit

    override fun onBind() {
        val config = processAdapterAndLMByType(type)
        viewbinding.recyclerView.adapter = config.first
        viewbinding.recyclerView.layoutManager = config.second
    }

    fun backToTop() {
        viewbinding.recyclerView.smoothScrollToPosition(0)
    }

    fun search(keyword: String) {
    }

    private fun processAdapterAndLMByType(type : PictureBookType) : Pair<BaseViewBindingAdapter<*, *>, RecyclerView.LayoutManager> {
        val adapter : BaseViewBindingAdapter<*, *>
        val layoutManager : RecyclerView.LayoutManager
        val orientation = resources.configuration.orientation
        when (type) {
            PictureBookType.Prop -> {
                adapter = PropAdapter()
                layoutManager = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    GridLayoutManager(context, 3)
                } else {
                    GridLayoutManager(context, 4)
                }
            }
            PictureBookType.Skill -> {
                adapter = SkillAdapter()
                layoutManager = LinearLayoutManager(context)
            }
            PictureBookType.Spirit -> {
                adapter = SpiritAdapter()
                layoutManager = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    GridLayoutManager(context, 3)
                } else {
                    GridLayoutManager(context, 4)
                }
            }
        }
        return Pair(adapter, layoutManager)
    }

    private class SpiritAdapter : BaseViewBindingAdapter<ItemPictureBookSpiritBinding, Spirit>() {
        override fun onBind(
            holder: ViewBindHolder<ItemPictureBookSpiritBinding>,
            data: Spirit,
            position: Int
        ) {
            holder.viewbinding.tvName.text = data.name
            val url = "https://res.17roco.qq.com/res/combat/icons/${data.iconSrc}"
            holder.viewbinding.ivAvatar.load(url)
            val properties = data.property.split(",")
            if (properties.isNotEmpty()) {
                holder.viewbinding.ivPropertyPrimary.visible()
                data.property
                if (properties.size > 1) {
                    holder.viewbinding.ivPropertySecondary.visible()
                }
            } else {
                holder.viewbinding.ivPropertyPrimary.invisible()
                holder.viewbinding.ivPropertySecondary.invisible()
            }
        }
    }

    private class SkillAdapter : BaseViewBindingAdapter<ItemPictureBookSkillBinding, Skill>() {
        override fun onBind(
            holder: ViewBindHolder<ItemPictureBookSkillBinding>,
            data: Skill,
            position: Int
        ) {
            holder.viewbinding.run {
                tvName.text = data.name
                tvId.text = context.getString(R.string.m_id_1, data.id)
                tvPP.text = context.getString(R.string.m_skill_pp, data.ppMax)
                tvPower.text = context.getString(R.string.power, data.power)
                tvAttackSpeed.text = context.getString(R.string.attack_speed, data.speed)
                tvDesc.text = data.description
//                ivAttackType.load("")
//                ivProperty.load("")
            }
        }
    }

    private class PropAdapter : BaseViewBindingAdapter<ItemPictureBookPropBinding, Prop>() {
        override fun onBind(
            holder: ViewBindHolder<ItemPictureBookPropBinding>,
            data: Prop,
            position: Int
        ) {
            holder.viewbinding.run {
                tvName.text = data.name
//                ivPic.load()
            }
        }
    }
}