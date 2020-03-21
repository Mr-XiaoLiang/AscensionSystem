package com.lollipop.ascensionsystem.activity

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.ascensionsystem.R
import com.lollipop.ascensionsystem.base.BaseActivity
import com.lollipop.ascensionsystem.info.KeyValueHolderInfo
import com.lollipop.ascensionsystem.info.RoleInfo
import com.lollipop.ascensionsystem.list.KeyValueHolder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override val layoutId: Int
        get() = R.layout.activity_main

    private val attributesAdapter = AttributesAdapter()
    private val roleInfo: RoleInfo by lazy {
        RoleInfo(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAttributesList()
    }

    private fun initAttributesList() {
        identityView.adapter = attributesAdapter
        identityView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        LinearSnapHelper().attachToRecyclerView(identityView)
    }

    override fun onStart() {
        super.onStart()
        updateAttributes()
    }

    private fun updateAttributes() {
        attributesAdapter.clear()
        for (attr in RoleInfo.Attributes) {
            if (attr == RoleInfo.Race) {

            }
            attributesAdapter.add(
                KeyValueHolderInfo(0,
                    getString(attr.name), attr.getValue(this, roleInfo)))
        }
    }

    private class AttributesAdapter(val onClick: (KeyValueHolder) -> Unit):
        RecyclerView.Adapter<KeyValueHolder>() {

        constructor(): this({})

        private val data = ArrayList<KeyValueHolderInfo>()

        fun reset(d: List<KeyValueHolderInfo>) {
            data.clear()
            data.addAll(d)
            notifyDataSetChanged()
        }

        fun clear() {
            data.clear()
        }

        fun add(info: KeyValueHolderInfo) {
            data.add(info)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeyValueHolder {
            return KeyValueHolder.create(parent, onClick)
        }

        override fun getItemCount() = data.size

        override fun onBindViewHolder(holder: KeyValueHolder, position: Int) {
            holder.bind(data[position])
        }

    }

}
