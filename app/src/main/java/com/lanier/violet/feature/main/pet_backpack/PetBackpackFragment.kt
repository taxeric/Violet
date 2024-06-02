package com.lanier.violet.feature.main.pet_backpack

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.lanier.violet.R
import com.lanier.violet.base.BaseFra
import com.lanier.violet.client.RocoServerClient
import com.lanier.violet.data.PetBackpackData
import com.lanier.violet.data.UserData
import com.lanier.violet.databinding.FragmentPetBackpackBinding
import com.lanier.violet.ext.collect
import com.lanier.violet.ext.launchSafe
import com.lanier.violet.ext.toFormattedString
import com.lanier.violet.feature.main.event.PetBackpackHandleEvent
import com.lanier.violet.feature.pet.data.PetData
import com.lanier.violet.views.rv.BaseRVAdapter
import com.lanier.violet.views.rv.OnItemClickListener

/**
 * Created by 幻弦让叶
 * Date 2024/5/31 22:50
 */
class PetBackpackFragment private constructor(
    override val layoutId: Int = R.layout.fragment_pet_backpack
) : BaseFra<FragmentPetBackpackBinding>() {

    companion object {

        fun newInstance(): PetBackpackFragment {
            val args = Bundle()

            val fragment = PetBackpackFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val mAdapter by lazy {
        PetBackpackAdapter().apply {
            onItemClickListener = object : OnItemClickListener {
                override fun onItemClick(adapter: BaseRVAdapter<*, *>, position: Int) {
                    (adapter.getItem(position) as? PetData)?.let {
                        (adapter as PetBackpackAdapter).run {
                            modifySelectedIndex(position)
                            modifyProfile(currentSelectedPet)
                        }
                    }
                }
            }
        }
    }

    override fun onBind() {
        launchSafe {
            collect<PetBackpackHandleEvent> {
                if (it.success) {
                    mAdapter.data = PetBackpackData.petData()
                    if (!PetBackpackData.isEmpty()) {
                        mAdapter.modifySelectedIndex(0)
                    }
                }
            }
        }
        viewbinding.recyclerView.adapter = mAdapter

        val command = buildString {
            append("95 27 00 00 00 0B 00 17")
            append(UserData.hexQQ)
            append("00 00 00 00 00 00 00 00")
        }
        RocoServerClient.sendCommand(command)
    }

    private fun modifyProfile(petData: PetData?) {
        petData?: return
        viewbinding.tvPetInfoPanel1.text = buildPetInfo1(petData)
        viewbinding.tvPetInfoPanel2.text = buildPetInfo2(petData)
        val skillCount = petData.skills.size
        viewbinding.skill1.bind(if (skillCount > 0) petData.skills[0] else null)
        viewbinding.skill2.bind(if (skillCount > 1) petData.skills[1] else null)
        viewbinding.skill3.bind(if (skillCount > 2) petData.skills[2] else null)
        viewbinding.skill4.bind(if (skillCount > 3) petData.skills[3] else null)
    }

    private fun buildPetInfo1(petData: PetData) : String {
        return buildString {
            append(petData.sex)
            append("\n")
            append(getString(R.string.m_character_s, "${petData.character}"))
            append("\n")
            append(getString(R.string.acquisition_time, (petData.acquisitionTime * 1000L).toFormattedString()))
        }
    }

    private fun buildPetInfo2(petData: PetData) : String {
        return buildString {
            append(getString(R.string.m_hp_max_hp, "${petData.hp}", "${petData.maxHp}"))
            append("\t")
            append(getString(R.string.m_gift, "${petData.hpGift}"))
            append("\t")
            append(getString(R.string.m_effort, "${petData.hpEffort}"))
            append("\n")

            append(getString(R.string.m_attack, "${petData.attack}"))
            append("\t")
            append(getString(R.string.m_gift, "${petData.attackGift}"))
            append("\t")
            append(getString(R.string.m_effort, "${petData.attackEffort}"))
            append("\n")

            append(getString(R.string.m_defence, "${petData.defence}"))
            append("\t")
            append(getString(R.string.m_gift, "${petData.defenceGift}"))
            append("\t")
            append(getString(R.string.m_effort, "${petData.defenceEffort}"))
            append("\n")

            append(getString(R.string.m_magic_attack, "${petData.magicAttack}"))
            append("\t")
            append(getString(R.string.m_gift, "${petData.magicAttackGift}"))
            append("\t")
            append(getString(R.string.m_effort, "${petData.magicAttackEffort}"))
            append("\n")

            append(getString(R.string.m_magic_defence, "${petData.magicDefence}"))
            append("\t")
            append(getString(R.string.m_gift, "${petData.magicDefenceGift}"))
            append("\t")
            append(getString(R.string.m_effort, "${petData.magicDefenceEffort}"))
            append("\n")

            append(getString(R.string.m_speed, "${petData.speed}"))
            append("\t")
            append(getString(R.string.m_gift, "${petData.speedGift}"))
            append("\t")
            append(getString(R.string.m_effort, "${petData.speedEffort}"))
        }
    }
}