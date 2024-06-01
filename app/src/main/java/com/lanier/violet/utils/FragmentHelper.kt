package com.lanier.violet.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * Created by 幻弦让叶
 * Date 2024/6/1 18:19
 */
class FragmentHelper(
    private val resId: Int,
    private val fragmentManager: FragmentManager
) {

    private var showIndex = -1

    private val _fragments = mutableListOf<SwitchFragment>()

    fun setFragments(
        fragments: List<SwitchFragment>,
    ) {
        _fragments.clear()
        _fragments.addAll(fragments)
    }

    fun addFragment(
        fragment: SwitchFragment
    ) {
        _fragments.add(fragment)
    }

    fun switchFra(index: Int) {
        if (_fragments.isEmpty()) {
            return
        }
        if (index < 0 || index > _fragments.size - 1) {
            return
        }
        fragmentManager.commit {
            if (showIndex != index) {
                if (showIndex != -1) {
                    hide(getFraByIndex(showIndex))
                }
                val needShowFragment = getFraByIndex(index)
                if (needShowFragment.isAdded) {
                    show(needShowFragment)
                } else {
                    val tag = getTagByIndex(index)
                    add(resId, needShowFragment, tag)
                    show(needShowFragment)
                }
                showIndex = index
            }
        }
    }

    private fun getFraByIndex(index: Int) = _fragments[index].fragment

    private fun getTagByIndex(index: Int) = _fragments[index].tag

    private fun FragmentManager.commit(
        action: FragmentTransaction.() -> Unit
    ) {
        beginTransaction()
            .apply { action.invoke(this) }
            .commit()
    }

    data class SwitchFragment(
        val fragment: Fragment,
        val tag: String = fragment.javaClass.simpleName,
        val title: String = "",
    )
}