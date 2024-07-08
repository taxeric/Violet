package com.lanier.violet.views

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.lanier.violet.R
import com.lanier.violet.database.data.SkillHelper
import com.lanier.violet.database.entity.Skill
import com.lanier.violet.feature.pet.data.SkillData

/**
 * Created by 幻弦让叶
 * Date 2024/6/1 16:29
 */
class SkillView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private var tvInfo: TextView
    private var skill: Skill? = null

    init {
        View.inflate(context, R.layout.view_skill, this)
        tvInfo = findViewById(R.id.tvSkillInfo)
        tvInfo.setOnClickListener { detailsDialog() }
    }

    fun bind(skillData: SkillData?) {
        if (skillData == null) {
            tvInfo.text = context.getString(R.string.__empty)
        } else {
            SkillHelper.getSkillByHexId(
                skillData.id,
                onSuccess = { dbSkill ->
                    this.skill = dbSkill
                    tvInfo.text = buildString {
                        append(dbSkill.name.ifEmpty { context.getString(R.string.__unknown) })
                        append("\n")
                        append(skillData.pp)
                        append("/")
                        append(dbSkill.ppMax)
                        append("\n")
                        append(context.getString(R.string.attack_speed, dbSkill.speed))
                        append("\n")
                        append(context.getString(R.string.skill_power, dbSkill.power))
                    }
                },
                onFailure = {
                    tvInfo.text = buildString {
                        append(context.getString(R.string.m_id, skillData.id))
                        append("\n")
                        append(context.getString(R.string.m_skill_pp, "${skillData.pp}"))
                    }
                }
            )
        }
    }

    private fun detailsDialog() {
        skill?.let {
            val onClickListener = DialogInterface.OnClickListener { dialog, which -> dialog?.dismiss() }
            AlertDialog.Builder(context)
                .setTitle(it.name)
                .setMessage(it.description)
                .setPositiveButton(context.getString(R.string.dialog_sure), onClickListener)
                .show()
        }
    }
}