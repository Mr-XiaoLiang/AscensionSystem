package com.lollipop.ascensionsystem.info

import android.content.Context
import com.lollipop.ascensionsystem.R
import com.lollipop.ascensionsystem.util.range

/**
 * @author lollipop
 * @date 2020/3/21 18:38
 * 参数的名称
 */
object InfoName {

    fun powerName(context: Context, power: Float, road: Int): String {
        val levelId = when(road) {
            RoadInfo.Truth.value -> R.array.level_truth
            RoadInfo.Buddha.value -> R.array.level_buddha
            RoadInfo.Devil.value -> R.array.level_devil
            RoadInfo.Ghost.value -> R.array.level_ghost
            else -> R.array.level_truth
        }
        val levelArray = context.resources.getStringArray(levelId)
        val level = power.toInt()
        val levelName = levelArray[level.range(0, levelArray.size)]
        return "V$level($levelName)"
    }

    fun powerProgress(power: Float): Float {
        return (power - (power.toInt())).range(0F, 1F)
    }

}


