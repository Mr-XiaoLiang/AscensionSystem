package com.lollipop.ascensionsystem.util

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import com.lollipop.ascensionsystem.info.RoadInfo
import com.lollipop.ascensionsystem.info.RoleInfo
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.regex.Pattern
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream


/**
 * @author lollipop
 * @date 2020/4/13 00:23
 * 计算核心
 */
object ComputingCore {

    private const val RADIX = 36
    private const val TOKEN_LENGTH = 4

    /**
     * 计算资质的token凭证
     * 获得一个64位长度的字符串
     * 并且每4位表示一个设备参数值，表示一项指标
     */
    private fun qualificationToken(context: Context): String {
        val tokenBuilder = StringBuilder()

        // 版本号(元神强度)
        tokenBuilder.append(Build.VERSION.SDK_INT.token())
        // 屏幕宽度
        tokenBuilder.append(context.resources.displayMetrics.widthPixels.token())
        // 屏幕高度
        tokenBuilder.append(context.resources.displayMetrics.heightPixels.token())
        // 显示
        tokenBuilder.append(Build.DISPLAY.token())
        // RAM(灵魂强度)
        tokenBuilder.append(getRAM(context))
        // bootloader
        tokenBuilder.append(Build.BOOTLOADER.token())
        // 获取厂商名(寿命)
        tokenBuilder.append(Build.MANUFACTURER.token())
        // 获取产品名
        tokenBuilder.append(Build.PRODUCT.token())
        // 获取手机品牌
        tokenBuilder.append(Build.BRAND.token())
        // 获取手机型号
        tokenBuilder.append(Build.MODEL.token())
        // 获取手机主板名
        tokenBuilder.append(Build.BOARD.token())
        // 设备名
        tokenBuilder.append(Build.DEVICE.token())
        // 硬件名
        tokenBuilder.append(Build.HARDWARE.token())
        // 主机
        tokenBuilder.append(Build.HOST.token())
        // ID
        tokenBuilder.append(Build.ID.token())
        // 用户
        tokenBuilder.append(Build.USER.token())

        return tokenBuilder.toString()
    }

    private fun getRAM(context: Context): String {
        val activityManager = context
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.totalMem.toString(RADIX).token()
    }

    private fun Int.token(): String {
        return this.toString(RADIX).token()
    }

    private fun String.token(): String {
        return this.clear().fixLength(TOKEN_LENGTH)
    }

    private fun String.fixLength(l: Int): String {
        if (this.length == l) {
            return this
        }
        if (this.length > l) {
            return this.substring(this.length - l, this.length)
        }
        if (this.length == l - 1) {
            return "0$this"
        }
        if (this.length == l - 2) {
            return "00$this"
        }
        val builder = StringBuilder(this)
        while (builder.length < l) {
            builder.insert(0, "0")
        }
        return builder.toString()
    }

    private fun String.zipTo(): String {
        val outputStream = ByteArrayOutputStream()
        val zipOutputStream = GZIPOutputStream(outputStream)
        val inputStream = ByteArrayInputStream(this.toByteArray(Charsets.UTF_8))
        val buffer = ByteArray(1024)
        var length = inputStream.read(buffer)
        while(length >= 0) {
            zipOutputStream.write(buffer, 0, length)
            length = inputStream.read(buffer)
        }
        inputStream.close()
        zipOutputStream.flush()
        zipOutputStream.close()
        return String(outputStream.toByteArray(), Charsets.ISO_8859_1)
    }

    private fun String.unzipTo(): String {
        val outputStream = ByteArrayOutputStream()
        val inputStream = GZIPInputStream(ByteArrayInputStream(this.toByteArray(Charsets.ISO_8859_1)))
        val buffer = ByteArray(1024)
        var length = inputStream.read(buffer)
        while(length >= 0) {
            outputStream.write(buffer, 0, length)
            length = inputStream.read(buffer)
        }
        inputStream.close()
        outputStream.flush()
        outputStream.close()
        return String(outputStream.toByteArray(), Charsets.UTF_8)
    }

    private fun String.clear() : String {
        return Pattern.compile("[^a-zA-Z0-9]").matcher(this).replaceAll("")
    }

    /**
     * 初始化信息
     */
    fun initRole(context: Context, info: RoleInfo) {
        if (info.get(RoleInfo.IsInit)) {
            return
        }
        val tokenValue = qualificationToken(context)
        val random = Random()

        // 初始种族始终是修仙
        info.set(RoleInfo.Race, RoadInfo.Truth.value)

        // 修为是0
        setValue(info, RoleInfo.Power, 0F)

        // 初始法力为0, 最大值。默认为100
        setValue(info, RoleInfo.Mana, 0F, 100F)

        // 元神值 80 ~ 120
        setValue(info, RoleInfo.SoulEntity, tokenValue.tokenKey(0)
            .toInt(RADIX)
            .attrValue(random, 80F, 120F))

        // 灵魂值 60 ~ 120
        setValue(info, RoleInfo.Soul, tokenValue.tokenKey(4)
            .toInt(RADIX)
            .attrValue(random, 60F, 120F))


        // 寿命 60 ~ 120
        setValue(info, RoleInfo.Life, tokenValue.tokenKey(6)
            .toInt(RADIX)
            .attrValue(random, 60F, 120F))

        // 年龄是0 (年龄与骨龄的计算标准为现实时间的50倍，约1周=1年)
        setValue(info, RoleInfo.Age, 0F)

        // 骨龄是0 (年龄与骨龄的计算标准为现实时间的50倍，约1周=1年)
        setValue(info, RoleInfo.BoneAge, 0F)

        // TODO
    }

    private fun String.tokenKey(index: Int): String {
        if (this.isEmpty()) {
            return this
        }
        val maxCount = this.length / TOKEN_LENGTH
        val subIndex = index % maxCount
        return this.substring(subIndex * TOKEN_LENGTH, (subIndex + 1) * TOKEN_LENGTH)
    }

    private fun Int.attrValue(random: Random, minValue: Float, maxValue: Float): Float {
        val value = random.nextInt(this)
        // 以取值范围的中心作为标准
        val benchmark = this * 0.5F
        // 取值范围的中心点
        val benchmarkValue = maxValue - minValue
        // 真实的浮动长度（正负相对于原点）
        val floatingRange = benchmarkValue * 0.5F
        // 低于标准则为负数，最终叠加到原点的值上
        return ((benchmark - value) / benchmark * floatingRange) + benchmarkValue
    }

    private fun <T> setValue(info: RoleInfo, key: RoleInfo.RoleKey<T>, value: T, maxValue: T = value) {
        info.set(key, value)
        key.depend?.let { info.set(it, maxValue) }
    }

}