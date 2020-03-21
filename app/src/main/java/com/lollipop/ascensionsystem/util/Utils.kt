package com.lollipop.ascensionsystem.util

/**
 * @author lollipop
 * @date 2020/3/21 21:33
 * 通用工具
 */

fun Float.range(min: Float, max: Float): Float {
    if (this < min) {
        return min
    }
    if (this > max) {
        return max
    }
    return this
}

fun Int.range(min: Int, max: Int): Int {
    if (this < min) {
        return min
    }
    if (this > max) {
        return max
    }
    return this
}
