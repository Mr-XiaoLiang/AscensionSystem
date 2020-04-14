package com.lollipop.ascensionsystem.util

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

    /**
     * 计算资质的token凭证
     */
    fun qualificationToken(): String {
        // https://blog.csdn.net/gjy211/article/details/52015198
        val tokenBuilder = StringBuilder()
        // 版本号
        tokenBuilder.append(Build.VERSION.SDK_INT.token())
        return tokenBuilder.toString()
    }

    private fun Int.token(): String {
        val intValue = this.toString(RADIX)
        return when {
            intValue.length < 2 -> {
                "0$intValue"
            }
            intValue.length > 2 -> {
                intValue.substring(intValue.length - 2, intValue.length)
            }
            else -> {
                intValue
            }
        }
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