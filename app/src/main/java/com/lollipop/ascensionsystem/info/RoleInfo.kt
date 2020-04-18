package com.lollipop.ascensionsystem.info

import android.content.Context
import com.lollipop.ascensionsystem.R
import com.lollipop.ascensionsystem.util.range

/**
 * @author lollipop
 * @date 2020/3/21 15:44
 * 角色信息
 */
class RoleInfo(context: Context) : BaseInfo<RoleInfo.RoleKey<*>>("RoleInfo", context) {

    companion object {

        /**
         * 是否已经初始化
         */
        @JvmStatic
        val IsInit = BooleanRoleKey("isInit", false, 0, 0, 0)

        /**
         * 性别
         */
        @JvmStatic
        val IsMale = BooleanRoleKey(
            "isMale", true,
            R.string.gender, R.string.male, R.string.female
        )

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
        val Power = PowerRoleKey()

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
        val SoulEntity =
            FloatRoleKey("SoulEntity", 0F, R.string.soul_entity, R.color.colorSoulEntity)
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
        val FiveElements = EnumArrayRoleKey(
            "FiveElements", arrayOf(
                FiveElementsInfo.Earth, FiveElementsInfo.Fire,
                FiveElementsInfo.Gold, FiveElementsInfo.Water, FiveElementsInfo.Wood
            ), R.string.fiveElements, { info, context -> context.getString(info.value) },
            {
                try {
                    FiveElementsInfo.valueOf(it)
                } catch (e: Exception) {
                    FiveElementsInfo.Earth
                }
            })

        /**
         * 幸运
         * 甲>乙>丙>丁>戊
         */
        @JvmStatic
        val Lucky = EnumRoleKey("Lucky", LuckyLevel.C, R.string.lucky,
            {info, context -> context.getString(info.value)},
            {
                try {
                    LuckyLevel.valueOf(it)
                } catch (e: Exception) {
                    LuckyLevel.C
                }
            })

        /**
         * 力量
         * 肉体的力量基础
         * 决定武器，攻击力
         */
        @JvmStatic
        val Muscle = FloatRoleKey("Muscle", 10F, R.string.muscle, 0)

        /**
         * 速度
         * 攻击速度，移动速度
         */
        @JvmStatic
        val Speed = FloatRoleKey("Speed", 10F, R.string.speed, 0)

        /**
         * 感知
         * 神识，识别敌方属性距离
         * 本质上是以神识为基础叠加自身的倍数
         */
        @JvmStatic
        val Perception = FloatRoleKey("Perception", 10F, R.string.perception, 0)

        /**
         * 天赋
         * 决定悟性，提升功法创建成功率
         */
        @JvmStatic
        val Talent = FloatRoleKey("Talent", 10F, R.string.talent, 0)

//        val MajorPractice =

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
            IsMale, Race, Power, Mana, SoulEntity, Soul, Life, FiveElements, Lucky,
            Muscle, Speed, Perception, Talent
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
        return key.decode(super.get(key.key, key.encode(key.defValue as Any)))
    }

    fun <T> set(key: RoleKey<T>, value: T) {
        super.put(key.key, key.encode(value as Any))
    }

    override fun keyToKey(key: String): RoleKey<*> {
        for (roleKey in allKeys) {
            if (roleKey.key == key) {
                return roleKey
            }
        }
        return Empty
    }

    @Suppress("UNCHECKED_CAST")
    abstract class RoleKey<T>(val key: String, val defValue: T, val name: Int) {

        var depend: RoleKey<T>? = null
            private set

        fun dependOn(target: RoleKey<T>): RoleKey<T> {
            depend = target
            return this
        }

        open fun getValue(context: Context, info: RoleInfo): String {
            return createValue(info.get(this), context).let {
                if (depend != null) {
                    "$it(${depend?.getValue(context, info) ?: ""})"
                } else {
                    it
                }
            }
        }

        open fun encode(value: Any): Any {
            return value
        }

        open fun decode(value: Any): T {
            return value as T
        }

        abstract fun createValue(value: T, context: Context): String

    }

    class BooleanRoleKey(
        key: String, defValue: Boolean, name: Int,
        private val ofTrue: Int, private val ofFalse: Int
    ) :
        RoleKey<Boolean>(key, defValue, name) {

        override fun createValue(value: Boolean, context: Context): String {
            return context.getString(
                if (value) {
                    ofTrue
                } else {
                    ofFalse
                }
            )
        }

    }

    class StringRoleKey(key: String, defValue: String, name: Int) :
        RoleKey<String>(key, defValue, name) {

        override fun createValue(value: String, context: Context): String {
            return defValue
        }

    }

    open class FloatRoleKey(key: String, defValue: Float, name: Int, val barColor: Int) :
        RoleKey<Float>(key, defValue, name) {

        override fun createValue(value: Float, context: Context): String {
            return value.toString()
        }

    }

    class PowerRoleKey: FloatRoleKey("Power", 0F, R.string.power, R.color.colorPower) {

        override fun getValue(context: Context, info: RoleInfo): String {
            val power = info.get(this)
            return InfoName.powerName(context, power, info.get(Race))
        }

    }

    open class IntRoleKey(key: String, defValue: Int, name: Int) :
        RoleKey<Int>(key, defValue, name) {

        override fun createValue(value: Int, context: Context): String {
            return value.toString()
        }

    }

    class ArrayValueRoleKey(
        key: String, defValue: Int, name: Int,
        private val valueArray: IntArray
    ) :
        IntRoleKey(key, defValue, name) {
        override fun createValue(value: Int, context: Context): String {
            return context.getString(valueArray[value.range(0, valueArray.size)])
        }
    }

    @Suppress("UNCHECKED_CAST")
    class EnumArrayRoleKey<T : Enum<*>>(
        key: String, defValue: Array<T>, name: Int,
        private val getName: (T, Context) -> String,
        private val parse: (String) -> T
    ) : RoleKey<Array<T>>(key, defValue, name) {
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
                }
                builder.append(getName.invoke(value[index], context))
            }
            return builder.toString()
        }

        override fun encode(value: Any): Any {
            if (value is Array<*>) {
                val builder = StringBuilder()
                for (i in value.indices) {
                    if (i > 0) {
                        builder.append(",")
                    }
                    val v = value[i]
                    if (v is Enum<*>) {
                        builder.append(v.name)
                    } else {
                        builder.append(v.toString())
                    }
                }
                return builder.toString()
            }
            return super.encode(value)
        }

        override fun decode(value: Any): Array<T> {
            if (value is String) {
                val list = ArrayList<T>()
                val valueArray = value.split(",")
                for (v in valueArray) {
                    val enum = parse.invoke(v)
                    if (!list.contains(enum)) {
                        list.add(enum)
                    }
                }
                return Array<Enum<*>>(list.size) { list[it] } as Array<T>
            }
            return super.decode(value)
        }

    }

    class EnumRoleKey<T : Enum<*>>(
        key: String, defValue: T, name: Int,
        private val getName: (T, Context) -> String,
        private val parse: (String) -> T) : RoleKey<T>(key, defValue, name) {

        override fun createValue(value: T, context: Context): String {
            return getName.invoke(value, context)
        }

        override fun encode(value: Any): Any {
            if (value is Enum<*>) {
                return value.name
            }
            return super.encode(value)
        }

        override fun decode(value: Any): T {
            if (value is String) {
                return parse.invoke(value)
            }
            return super.decode(value)
        }

    }

}

