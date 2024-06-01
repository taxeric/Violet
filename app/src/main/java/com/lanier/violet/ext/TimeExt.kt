package com.lanier.violet.ext

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by 幻弦让叶
 * Date 2024/6/1 17:00
 */
fun Long.toFormattedString(
    format: String = "yyyy年MM月dd日"
): String {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    val date = Date(this)
    return sdf.format(date)
}
