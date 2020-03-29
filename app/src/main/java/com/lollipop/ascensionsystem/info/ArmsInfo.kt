package com.lollipop.ascensionsystem.info

/**
 * @author lollipop
 * @date 2020/3/29 20:43
 * 武器属性
 */
class ArmsInfo(
    /**
     * 武器名称
     */
    val name: String,
    /**
     * 武器类型
     */
    val type: ArmsType,
    /**
     * 攻击加成
     */
    val attackBonus: Int,
    /**
     * 攻击属性加成
     */
    val attackAttributeBonus: Array<FiveElementsInfo>,
    /**
     * 速度加成
     */
    val flexibleBonus: Int,
    /**
     * 强度（当碰撞力大于强度时崩溃）
     */
    val intensity: Int,
    /**
     * 品质（对基本属性加成）
     */
    val quality: Int,
    /**
     * 特性(描述性内容，属性已完成叠加）
     */
    val features: String,
    /**
     * 耐久（战斗后降低耐久，随时间降低耐久）
     */
    val durability: Int)