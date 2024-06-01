package com.lanier.violet.data

import com.lanier.violet.feature.pet.data.PetData

/**
 * Created by 幻弦让叶
 * Date 2024/6/1 0:36
 *
 * 背包精灵数据
 */
object PetBackpackData {

    private val pets = mutableListOf<PetData>()

    fun reset(petData: List<PetData>) {
        pets.clear()
        pets.addAll(petData)
    }

    fun petData() = pets

    fun isEmpty() = pets.isEmpty()
}