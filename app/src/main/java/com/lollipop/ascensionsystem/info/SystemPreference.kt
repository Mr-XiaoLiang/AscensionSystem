package com.lollipop.ascensionsystem.info

import android.content.Context
import com.lollipop.ascensionsystem.util.log

/**
 * @author lollipop
 * @date 2020/3/22 18:38
 * 修炼系统的偏好设置
 */
class SystemPreference private constructor(context: Context): BaseInfo<String>("SystemPreference", context) {

    companion object {

        /**
         * 小循环15分钟
         */
        private const val SMALL_CYCLE = 1000L * 60 * 15

        /**
         * 大循环60分钟
         */
        private const val BIG_CYCLE = SMALL_CYCLE * 4

        private const val INIT = "INIT"
        private const val OFFSET_TIME = "OFFSET_TIME"

        fun from(context: Context): SystemPreference {
            return SystemPreference(context)
        }

    }

    val isInit: Boolean
        get() {
            return get(INIT, false)
        }

    val timeOffset: Long
        get() {
            return get(OFFSET_TIME, 0)
        }

    fun callInit() {
        put(INIT, true)
    }

    fun setTimeOffset(offset: Long) {
        put(OFFSET_TIME, offset)
        log("setTimeOffset:$offset")
    }

    override fun keyToKey(key: String): String {
        return key
    }
}