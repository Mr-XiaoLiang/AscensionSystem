package com.lollipop.ascensionsystem.info

import android.content.Context
import com.lollipop.ascensionsystem.R
import com.lollipop.ascensionsystem.util.range

/**
 * @author lollipop
 * @date 2020/3/21 15:44
 * 角色信息
 */
class RoleInfo(context: Context): BaseInfo<RoleInfo.RoleKey<*>>("RoleInfo", context) {

    companion object {

        val IsInit = BooleanRoleKey("isInit", false, 0, 0, 0)

        /**
         * 性别
         */
        @JvmStatic
        val IsMale = BooleanRoleKey("isMale", true,
            R.string.gender, R.string.male, R.string.female)

        /**
         * 种族
         */
        @JvmStatic
        val Race = ArrayValueRoleKey("Race", 0, R.string.race, intArrayOf(
            RoadInfo.Truth.value,
            RoadInfo.Buddha.value,
            RoadInfo.Devil.value,
            RoadInfo.Ghost.value
        ))

        /**
         * 修为
         */
        @JvmStatic
        val Power = FloatRoleKey("Power", 0F, R.string.power, R.color.colorPower)

        /**
         * 法力值
         * 可以用于升级修为，可以用于战斗
         */
        @JvmStatic
        val Mana = FloatRoleKey("Mana", 0F, R.string.mana, R.color.colorMana)

        /**
         * 元神
         * 思考能力，为0时死亡，决定功法学习，挂机修炼
         */
        @JvmStatic
        val SoulEntity = FloatRoleKey("SoulEntity", 0F, R.string.soul_entity, R.color.colorSoulEntity)

        /**
         * 灵魂
         *
         */
        @JvmStatic
        val Soul = FloatRoleKey("Soul", 0F, R.string.soul, R.color.colorSoul)

        /**
         * 寿命
         * 生命的长度
         */
        @JvmStatic
        val Life = FloatRoleKey("Life", 0F, R.string.soul, R.color.colorLife)

        /**
         * 空
         */
        @JvmStatic
        val Empty = StringRoleKey("", "", 0)

        /**
         * 属性集合
         */
        @JvmStatic
        val Attributes = arrayOf(
            IsMale, Race, Power, Mana, SoulEntity, Soul, Life
        )

        /**
         * 所有属性
         */
        @JvmStatic
        val allKeys = arrayOf(
            *Attributes, Empty
        )
    }

    fun <T> get(key: RoleKey<T>): T {
        return super.get(key.key, key.defValue)
    }

    fun <T> put(key: RoleKey<T>, value: Any) {
        super.put(key.key, value)
    }


    override fun keyToKey(key: String): RoleKey<*> {
        for (roleKey in allKeys) {
            if (roleKey.key == key) {
                return roleKey
            }
        }
        return Empty
    }

    abstract class RoleKey<T> (val key: String, val defValue: T, val name: Int) {

        var depend: RoleKey<*>? = null
            private set

        fun dependOn(target: RoleKey<*>): RoleKey<T> {
            depend = target
            return this
        }

        fun getValue(context: Context, info: RoleInfo): String {
            return createValue(info.get(this), context).let {
                if (depend != null) { "$it(${depend.getValue(context, info)})" } else { it }
            }
        }

        abstract fun createValue(value: T, context: Context): String

    }

    class BooleanRoleKey(key: String, defValue: Boolean, name: Int,
                         private val ofTrue: Int, private val ofFalse: Int):
        RoleKey<Boolean>(key, defValue, name) {

        override fun createValue(value: Boolean, context: Context): String {
            return context.getString(if (value) { ofTrue } else { ofFalse } )
        }

    }

    class StringRoleKey(key: String, defValue: String, name: Int):
        RoleKey<String>(key, defValue, name) {

        override fun createValue(value: String, context: Context): String {
            return defValue
        }

    }

    class FloatRoleKey(key: String, defValue: Float, name: Int, val barColor: Int):
        RoleKey<Float>(key, defValue, name) {

        override fun createValue(value: Float, context: Context): String {
            return value.toString()
        }

    }

    open class IntRoleKey(key: String, defValue: Int, name: Int):
        RoleKey<Int>(key, defValue, name) {

        override fun createValue(value: Int, context: Context): String {
            return value.toString()
        }

    }

    class ArrayValueRoleKey(key: String, defValue: Int, name: Int,
                            private val valueArray: IntArray):
        IntRoleKey(key, defValue, name) {
        override fun createValue(value: Int, context: Context): String {
            return context.getString(valueArray[value.range(0, valueArray.size)])
        }
    }

}

