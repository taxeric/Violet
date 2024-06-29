package com.lanier.violet.feature.picturebook

sealed class PictureBookType(typeId: Int) {

    companion object {

        const val TYPE_ID_SPIRIT = 1
        const val TYPE_ID_SKILL = 2
        const val TYPE_ID_PROP = 3
    }

    data object Spirit : PictureBookType(TYPE_ID_SPIRIT)
    data object Skill : PictureBookType(TYPE_ID_SKILL)
    data object Prop : PictureBookType(TYPE_ID_PROP)
}