package com.lollipop.ascensionsystem.info

/**
 * @author lollipop
 * @date 2020/3/29 20:44
 * 十八般兵器
 */
enum class ArmsType(val isLongSoldier: Boolean, val distance: Int, val power: Int, val speed: Int) {

    /**
     * 枪
     */
    Spear(true, 10, 5, 0),

    /**
     * 戟
     */
    Halberd(true, 10, 7, 0),

    /**
     * 棍
     */
    Stick(true, 10, 4, 0),

    /**
     * 钺
     */
    Yue(true, 10, 5, 0),

    /**
     * 叉
     */
    Fork(true, 10, 6, 0),

    /**
     * 镗
     */
    Boring(true, 10, 4, 0),

    /**
     * 钩
     */
    Hook(true, 10, 1, 3),

    /**
     * 槊
     */
    Shuo(true, 10, 3, 0),

    /**
     * 环
     */
    Ring(true, 10, 2, 0),

    /**
     * 刀
     */
    Knife(false, 5, 3, 3),

    /**
     * 剑
     */
    Sword(false, 5, 2, 10),

    /**
     * 拐
     */
    Turning(false, 5, 4, 3),

    /**
     * 斧
     */
    Axe(false, 5, 8, 3),

    /**
     * 鞭
     */
    Whip(false, 5, 3, 7),

    /**
     * 锏
     */
    Jian(false, 5, 4, 2),

    /**
     * 锤
     */
    Hammer(false, 5, 10, 0),

    /**
     * 棒
     */
    ShortStick(false, 5, 4, 3),

    /**
     * 杵
     */
    Pestle(false, 5, 3, 4);

}