package com.lanier.violet.views.rv

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.Constructor
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType

/**
 * Created by 幻弦让叶
 * Date 2024/6/1 14:05
 */
abstract class BaseRVAdapter<Holder : BaseViewHolder, T> : RecyclerView.Adapter<Holder>() {

    private val _data = mutableListOf<T>()
    var data : List<T>
        set(value) {
            _data.clear()
            _data.addAll(value)
            onResetData()
            notifyDataSetChanged()
        }
        get() = _data

    var onItemClickListener: OnItemClickListener? = null

    internal var mRecyclerView: RecyclerView? = null
    var recyclerView: RecyclerView
        private set (value) {
            mRecyclerView = value
        }
        get() = mRecyclerView!!

    val context: Context
        get() = recyclerView.context

    open fun getItem(position: Int) = _data[position]

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        mRecyclerView = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return createVH(parent)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        onItemClickListener?.let { click ->
            holder.itemView.setOnClickListener {
                click.onItemClick(this, position)
            }
        }
        onBind(holder, _data[position], position)
    }

    override fun getItemCount() = _data.size

    abstract fun onBind(holder: Holder, data: T, position: Int)

    abstract val layoutId: Int

    protected open fun onResetData() {}

    protected open fun createVH(parent: ViewGroup) : Holder {
        return createVH(
            LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        )
    }

    protected open fun createVH(view: View) : Holder {
        var temp: Class<*>? = javaClass
        var z: Class<*>? = null
        while (z == null && null != temp) {
            z = generateVHClass(temp)
            temp = temp.superclass
        }
        val vh: Holder? = if (z == null) {
            BaseViewHolder(view) as Holder
        } else {
            createBaseGenericKInstance(z, view)
        }
        return vh ?: BaseViewHolder(view) as Holder
    }

    private fun createBaseGenericKInstance(z: Class<*>, view: View) : Holder? {
        try {
            val constructor: Constructor<*>
            return if (z.isMemberClass && !Modifier.isStatic(z.modifiers)) {
                constructor = z.getDeclaredConstructor(javaClass, View::class.java)
                constructor.isAccessible = true
                constructor.newInstance(this, view) as Holder
            } else {
                constructor = z.getDeclaredConstructor(View::class.java)
                constructor.isAccessible = true
                constructor.newInstance(view) as Holder
            }
        } catch (e: Exception) {
            return null
        }
    }

    private fun generateVHClass(z: Class<*>) : Class<*>? {
        try {
            val type = z.genericSuperclass
            if (type is ParameterizedType) {
                val types = type.actualTypeArguments
                for (temp in types) {
                    if (temp is Class<*>) {
                        if (BaseViewHolder::class.java.isAssignableFrom(temp)) {
                            return temp
                        }
                    } else if (temp is ParameterizedType) {
                        val rawType = temp.rawType
                        if (rawType is Class<*> && BaseViewHolder::class.java.isAssignableFrom(rawType)) {
                            return rawType
                        }
                    }
                }
            }
        } catch (e: Exception) {
            return null
        }
        return null
    }
}
