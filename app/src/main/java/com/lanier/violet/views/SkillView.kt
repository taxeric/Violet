package com.lanier.violet.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.lanier.violet.R
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

    init {
        View.inflate(context, R.layout.view_skill, this)
        tvInfo = findViewById(R.id.tvSkillInfo)
    }

    fun bind(skillData: SkillData?) {
        tvInfo.text = if (skillData == null) {
            context.getString(R.string.__empty)
        } else {
            buildString {
                append(context.getString(R.string.m_id, skillData.id))
                append("\n")
                append(context.getString(R.string.m_skill_pp, "${skillData.pp}"))
            }
        }
    }
}