package com.lollipop.ascensionsystem.util

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.lollipop.ascensionsystem.BuildConfig
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * @author lollipop
 * @date 2020/3/21 21:33
 * 通用工具
 */

fun Float.range(min: Float, max: Float): Float {
    if (this < min) {
        return min
    }
    if (this > max) {
        return max
    }
    return this
}

fun Int.range(min: Int, max: Int): Int {
    if (this < min) {
        return min
    }
    if (this > max) {
        return max
    }
    return this
}

val threadPool: Executor by lazy {
    Executors.newScheduledThreadPool(5)
}

val mainHandler: Handler by lazy {
    Handler(Looper.getMainLooper())
}

inline fun <reified T> T.doAsync(noinline err: (Asynchronous<T>.(Throwable) -> Unit)? = null, noinline runTask: Asynchronous<T>.() -> Unit) {
    threadPool.execute(Asynchronous(this, err, runTask))
}

inline fun <reified T> T.onUI(
    noinline err: (T.(Throwable) -> Unit)? = null,
    noinline runTask: T.() -> Unit) {
    mainHandler.post{
        try {
            runTask.invoke(this)
        } catch (e: Throwable) {
            err?.invoke(this, e)
        }
    }
}

class Asynchronous<T>(val target: T,
                      private val err: (Asynchronous<T>.(Throwable) -> Unit)? = null,
                      private val runTask: Asynchronous<T>.() -> Unit): Runnable {

    override fun run() {
        try {
            runTask.invoke(this)
        } catch (e: Throwable) {
            err?.invoke(this, e)
        }
    }

}

inline fun <reified T: Any> T.log(value: String) {
    return logger()(value)
}

inline fun <reified T: Any> T.logger(tag: String = ""): ((value: String) -> Unit) {
    return {value ->
        if (BuildConfig.DEBUG) {
            Log.d("Lollipop", "${this.javaClass.name}-$tag: $value")
        }
    }
}
