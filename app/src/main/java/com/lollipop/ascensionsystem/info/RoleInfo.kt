package com.lollipop.ascensionsystem.info

import org.json.JSONObject

/**
 * @author lollipop
 * @date 2020/3/21 15:44
 * 角色信息
 */
class RoleInfo {

    companion object {
        /**
         * 性别
         */
        val isMale = RoleKey("isMale", false)
    }

    private val infoImpl = InfoImpl()

    fun parse(json: String) {
        infoImpl.parse(json)
    }

    override fun toString(): String {
        return infoImpl.toString()
    }

    private class InfoImpl {
        private var jsonInfo = JSONObject()

        @Suppress("UNCHECKED_CAST")
        fun <T> get(key: String, def: T): T {
            val obj = jsonInfo.opt(key) ?: return def
            return try {
                obj as T
            } catch (e: Throwable) {
                def
            }
        }

        fun set(key: String, value: Any) {
            jsonInfo.put(key, value)
        }

        fun parse(json: String) {
            try {
                val newInfo = JSONObject(json)
                jsonInfo = newInfo
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun toString(): String {
            return jsonInfo.toString()
        }
    }

    fun <T> get(key: RoleKey<T>): T {
        return infoImpl.get(key.name, key.defValue)
    }

    fun <T> put(keys: RoleKey<T>, value: Any) {
        infoImpl.set(keys.name, value)
    }

    class RoleKey<T> (val name: String, val defValue: T)

}

