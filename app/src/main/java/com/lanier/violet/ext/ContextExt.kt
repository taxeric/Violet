package com.lanier.violet.ext

import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * Created by 幻弦让叶
 * Date 2024/5/25 16:09
 */
inline fun <reified T> Context.startAct(
    crossinline action: ((Intent) -> Unit)
) {
    val intent = Intent(this, T::class.java).apply {
        action.invoke(this)
    }
    startActivity(intent)
}

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
