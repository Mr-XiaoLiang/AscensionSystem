package com.lollipop.ascensionsystem.info

import android.content.Context
import android.text.TextUtils
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
            .dependOn(FloatRoleKey("AllMana", 100F, R.string.mana, 0))

        /**
         * 元神
         * 思考能力，为0时死亡，决定功法学习，挂机修炼
         */
        @JvmStatic
        val SoulEntity = FloatRoleKey("SoulEntity", 0F, R.string.soul_entity, R.color.colorSoulEntity)
            .dependOn(FloatRoleKey("AllSoulEntity", 100F, R.string.soul_entity, 0))

        /**
         * 灵魂
         *
         */
        @JvmStatic
        val Soul = FloatRoleKey("Soul", 0F, R.string.soul, R.color.colorSoul)
            .dependOn(FloatRoleKey("AllSoul", 100F, R.string.soul, 0))

        /**
         * 寿命
         * 生命的长度
         */
        @JvmStatic
        val Life = FloatRoleKey("Life", 0F, R.string.life, R.color.colorLife)
            .dependOn(FloatRoleKey("AllLife", 100F, R.string.life, 0))

        /**
         * 灵根
         * 五行属性
         */
        @JvmStatic
        val FiveElements = EnumRoleKey("FiveElements", arrayOf(
            FiveElementsInfo.Earth, FiveElementsInfo.Fire,
            FiveElementsInfo.Gold, FiveElementsInfo.Water, FiveElementsInfo.Wood
        ), R.string.fiveElements) { info, context -> context.getString(info.value) }

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
            IsMale, Race, Power, Mana, SoulEntity, Soul, Life, FiveElements
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

    class EnumRoleKey<T: Enum<*>>(key: String, defValue: Array<T>, name: Int,
                                  private val getName: (T, Context) -> String): RoleKey<Array<T>>(key, defValue, name) {
        override fun createValue(value: Array<T>, context: Context): String {
            if (value.isEmpty()) {
                return ""
            }
            if (value.size == 1) {
                return getName.invoke(value[0], context)
            }
            val builder = StringBuilder()
            for (index in value.indices) {
                if (index != 0) {
                    builder.append(" ")
                } else {
                    builder.append(getName.invoke(value[index], context))
                }
            }
            return builder.toString()
        }
    }

    override fun customizeGetter(value: String, def: Any): GetterInfo? {
        when (def) {
            is Array<*> -> {
                val valueArray = value.split(ARRAY_DELIMITER)
                val defItem = def[0]?:return GetterInfo(def)
                if (defItem is FiveElementsInfo) {
                    return GetterInfo(fiveElementParse(valueArray))
                }
            }
        }
        return super.customizeGetter(value, def)
    }

    override fun customizeSetter(value: Any): SetterInfo? {
        when (value) {
            is Array<*> -> {
                if (value.isEmpty()) {
                    return SetterInfo("");
                }
                val fastValue = value[0]
                if (fastValue is FiveElementsInfo) {
                    return SetterInfo(fiveElementSerialization(value as Array<FiveElementsInfo>))
                }
            }
        }
        return super.customizeSetter(value)
    }

    private fun fiveElementParse(valueArray: List<String>): Array<FiveElementsInfo> {
        val result = ArrayList<FiveElementsInfo>()
        for (value in valueArray) {
            if (TextUtils.isEmpty(value)) {
                continue
            }
            result.add(FiveElementsInfo.valueOf(value))
        }
        return Array(result.size) { result[it] }
    }

    private fun fiveElementSerialization(valueArray: Array<FiveElementsInfo>): String {
        if (valueArray.isEmpty()) {
            return ""
        }
        if (valueArray.size == 1) {
            return valueArray[0].name
        }
        val builder = StringBuilder()
        for (index in valueArray.indices) {
            if (index != 0) {
                builder.append(ARRAY_DELIMITER)
            }
            builder.append(valueArray[index].name)

        }
        return builder.toString()
    }

}

