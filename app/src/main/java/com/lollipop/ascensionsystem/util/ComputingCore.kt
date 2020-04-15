package com.lollipop.ascensionsystem.util

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import com.lollipop.ascensionsystem.info.RoleInfo
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
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
     */
    fun qualificationToken(context: Context): String {
        val tokenBuilder = StringBuilder()
        // 版本号
        tokenBuilder.append(Build.VERSION.SDK_INT.token())
        // 屏幕宽度
        tokenBuilder.append(context.resources.displayMetrics.widthPixels.token())
        // 屏幕高度
        tokenBuilder.append(context.resources.displayMetrics.heightPixels.token())
        // 显示
        tokenBuilder.append(Build.DISPLAY.token())
        // RAM
        tokenBuilder.append(getRAM(context))
        // bootloader
        tokenBuilder.append(Build.BOOTLOADER.token())
        // 获取厂商名
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
        return this.fixLength(TOKEN_LENGTH)
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

    /**
     * 初始化信息
     */
    fun initRole(info: RoleInfo) {
        // TODO
    }

}