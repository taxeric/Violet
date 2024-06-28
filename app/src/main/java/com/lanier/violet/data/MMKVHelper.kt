package com.lanier.violet.data

import com.tencent.mmkv.MMKV

fun putBoolToMMKV(key: String, value: Boolean) {
    MMKV.defaultMMKV().encode(key, value)
}

fun getBoolFromMMKV(key: String, def: Boolean = false) =
    MMKV.defaultMMKV().decodeBool(key, def)

fun putStringToMMKV(key: String, value: String) {
    MMKV.defaultMMKV().encode(key, value)
}

fun getStringFromMMKV(key: String, def: String = "") =
    MMKV.defaultMMKV().decodeString(key, def)

fun putIntToMMKV(key: String, value: Int) {
    MMKV.defaultMMKV().encode(key, value)
}

fun getIntFromMMKV(key: String, def: Int = -1) =
    MMKV.defaultMMKV().decodeInt(key, def)

fun putLongToMMKV(key: String, value: Long) {
    MMKV.defaultMMKV().encode(key, value)
}

fun getLongFromMMKV(key: String, def: Long = 0L) =
    MMKV.defaultMMKV().decodeLong(key, def)

fun putDoubleToMMKV(key: String, value: Double) {
    MMKV.defaultMMKV().encode(key, value)
}

fun getDoubleFromMMKV(key: String, def: Double = 0.0) =
    MMKV.defaultMMKV().decodeDouble(key, def)