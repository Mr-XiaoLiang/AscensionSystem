package com.lollipop.ascensionsystem.util

import android.os.Build
import com.lollipop.ascensionsystem.info.RoleInfo

/**
 * @author lollipop
 * @date 2020/4/13 00:23
 * 计算核心
 */
object ComputingCore {

    private const val RADIX = 36

    /**
     * 计算资质的token凭证
     */
    fun qualificationToken(): String {
        // https://blog.csdn.net/gjy211/article/details/52015198
        val tokenBuilder = StringBuilder()
        tokenBuilder.append(Build.VERSION.SDK_INT.token())
        return tokenBuilder.toString()
    }

    private fun Int.token(): String {
        val intValue = this.toString(RADIX)
        return when {
            intValue.length < 2 -> {
                "0$intValue"
            }
            intValue.length > 2 -> {
                intValue.substring(intValue.length - 2, intValue.length)
            }
            else -> {
                intValue
            }
        }
    }

    /**
     * 初始化信息
     */
    fun initRole(info: RoleInfo) {
        // TODO
    }

}