package com.lollipop.ascensionsystem.info

import android.content.Context
import android.content.SharedPreferences
import androidx.collection.ArraySet

/**
 * @author lollipop
 * @date 2020/3/21 22:57
 * 基础的描述信息实现类
 */
abstract class BaseInfo<T>(private val infoKey: String, private val context: Context){

    private val spCallback = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key != null) {
                observerList.forEach {
                    it.onRoleChange(keyToKey(key))
                }
            }
        }

    private val infoImpl = InfoImpl(
        context.getSharedPreferences(infoKey, Context.MODE_PRIVATE)).apply { registered(spCallback) }

    /**
     * 内容变更监听器
     */
    private val observerList = ArrayList<OnInfoChangeCallback<T>>()

    fun registered(callback: OnInfoChangeCallback<T>) {
        if (observerList.isEmpty()) {
            infoImpl.registered(spCallback)
        }
        observerList.add(callback)
    }

    fun unregistered(callback: OnInfoChangeCallback<T>) {
        observerList.remove(callback)
        if (observerList.isEmpty()) {
            infoImpl.unregistered(spCallback)
        }
    }

    interface OnInfoChangeCallback<T> {
        fun onRoleChange(key: T)
    }

    protected fun <A> get(key: String, def: A): A {
        return infoImpl.get(key, def)
    }

    protected fun put(key: String, value: Any) {
        infoImpl.set(key, value)
    }

    protected abstract fun keyToKey(key: String): T

    private class InfoImpl(private val preference: SharedPreferences) {

        fun registered(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
            preference.registerOnSharedPreferenceChangeListener(listener)
        }

        fun unregistered(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
            preference.unregisterOnSharedPreferenceChangeListener(listener)
        }

        @Suppress("UNCHECKED_CAST")
        fun <T> get(key: String, def: T): T {
            val value = when (def) {
                is String -> {
                    preference.getString(key, def)
                }
                is Int -> {
                    preference.getInt(key, def)
                }
                is Boolean -> {
                    preference.getBoolean(key, def)
                }
                is Float -> {
                    preference.getFloat(key, def)
                }
                is Long -> {
                    preference.getLong(key, def)
                }
                is Set<*> -> {
                    preference.getStringSet(key, def as Set<String>)
                }
                else -> def
            }?:def
            return value as T
        }

        fun set(key: String, value: Any) {
            val edit = preference.edit()
            when (value) {
                is String -> {
                    edit.putString(key, value)
                }
                is Int -> {
                    edit.putInt(key, value)
                }
                is Boolean -> {
                    edit.putBoolean(key, value)
                }
                is Float -> {
                    edit.putFloat(key, value)
                }
                is Double -> {
                    edit.putFloat(key, value.toFloat())
                }
                is Long -> {
                    edit.putLong(key, value)
                }
                is Set<*> -> {
                    if (value.isEmpty()) {
                        edit.putStringSet(key, ArraySet<String>())
                    } else {
                        val set = ArraySet<String>()
                        for (v in value) {
                            when (v) {
                                null -> {
                                    set.add("")
                                }
                                is String -> {
                                    set.add(v)
                                }
                                else -> {
                                    set.add(v.toString())
                                }
                            }
                        }
                    }
                }
                else -> {
                    edit.putString(key, value.toString())
                }
            }
            edit.apply()
        }

    }

    fun destroy() {
        infoImpl.unregistered(spCallback)
    }

}