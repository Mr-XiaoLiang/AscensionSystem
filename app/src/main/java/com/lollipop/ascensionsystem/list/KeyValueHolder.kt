package com.lollipop.ascensionsystem.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.ascensionsystem.R
import com.lollipop.ascensionsystem.info.KeyValueHolderInfo
import com.lollipop.ascensionsystem.view.CapabilityBar

/**
 * @author lollipop
 * @date 2020/3/21 21:44
 * 键值对的Item
 */
class KeyValueHolder private constructor(view: View): RecyclerView.ViewHolder(view) {

    companion object {
        fun create(group: ViewGroup): KeyValueHolder {
            return KeyValueHolder(
                LayoutInflater.from(group.context)
                    .inflate(R.layout.item_key_value, group, false))
        }
    }

    private val titleView: TextView by lazy {
        itemView.findViewById(R.id.nameView)
    }

    private val valueView: TextView by lazy {
        itemView.findViewById(R.id.valueView)
    }

    private val barView: CapabilityBar by lazy {
        itemView.findViewById(R.id.barView)
    }

    fun bind(info: KeyValueHolderInfo) {
        titleView.text = info.key
        valueView.text = info.value
        if (info.showProgress) {
            barView.visibility = View.GONE
        } else {
            barView.visibility = View.VISIBLE
            barView.progress(info.progress)
            barView.color(info.barColor)
        }
    }

}