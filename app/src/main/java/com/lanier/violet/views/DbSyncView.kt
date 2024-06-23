package com.lanier.violet.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.buildSpannedString
import com.lanier.violet.R

class DbSyncView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private var tvTitle: TextView
    private var tvLastTime: TextView
    private var btnSync: ImageButton

    var onSyncActionListener: OnSyncActionListener? = null

    init {
        View.inflate(context, R.layout.view_db_sync, this)
        tvTitle = findViewById(R.id.tvTitle)
        tvLastTime = findViewById(R.id.tvLastTime)
        btnSync = findViewById(R.id.btnSync)

        attributeSet?.let {
            val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.DbSyncView)
            val title = typedArray.getString(R.styleable.DbSyncView_db_sync_title)
            typedArray.recycle()

            tvTitle.text = title
        }
        refreshLastTime()

        btnSync.setOnClickListener { onSyncActionListener?.onSync(this) }
    }

    fun bindTitle(title: String) {
        tvTitle.text = title
    }

    fun refreshLastTime(lastTime: String? = null) {
        tvLastTime.text = buildSpannedString {
            append(context.getString(R.string.db_sync_last_time))
            append(": ")
            append(if (lastTime.isNullOrEmpty()) "--" else lastTime)
        }
    }

    interface OnSyncActionListener {

        fun onSync(view: DbSyncView)
    }
}