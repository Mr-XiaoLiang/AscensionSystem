package com.lollipop.ascensionsystem.info

/**
 * @author lollipop
 * @date 2020/3/21 21:44
 * 键值对的描述信息
 */
class KeyValueHolderInfo(
    val id: Int,
    val key: String, val value: String,
    val showProgress: Boolean, val progress: Float, val barColor: Int) {
    constructor(id: Int, key: String, value: String): this(id, key, value, false, 0F, 0)
    constructor(key: String, value: String): this(0, key, value)
}