package com.lollipop.ascensionsystem.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.ascensionsystem.R
import com.lollipop.ascensionsystem.info.KeyValueHolderInfo
import com.lollipop.ascensionsystem.util.range
import com.lollipop.ascensionsystem.view.CapabilityBar

/**
 * @author lollipop
 * @date 2020/3/21 21:44
 * 键值对的Item
 */
class KeyValueHolder private constructor(view: View, private val onClick: (KeyValueHolder) -> Unit):
    RecyclerView.ViewHolder(view) {

    companion object {
        fun create(group: ViewGroup, onClick: (KeyValueHolder) -> Unit): KeyValueHolder {
            return KeyValueHolder(
                LayoutInflater.from(group.context)
                    .inflate(R.layout.item_key_value, group, false), onClick)
        }
    }

    init {
        view.setOnClickListener {
            onClick.invoke(this)
        }
    }

    private val titleView = itemView.findViewById<TextView>(R.id.nameView)

    private val valueView = itemView.findViewById<TextView>(R.id.valueView)

    private val barView = itemView.findViewById<CapabilityBar>(R.id.barView)

    fun bind(info: KeyValueHolderInfo) {
        titleView.text = info.key
        valueView.text = info.value
        if (!info.showProgress) {
            barView.visibility = View.GONE
        } else {
            barView.visibility = View.VISIBLE
            val progress = (info.progress - info.progress.toInt()).range(0F, 1F)
            barView.progress(progress)
            barView.colorFromRes(info.barColor)
        }
    }

}