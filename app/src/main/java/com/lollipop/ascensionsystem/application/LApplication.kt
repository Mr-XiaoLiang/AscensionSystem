package com.lollipop.ascensionsystem.application

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.lollipop.ascensionsystem.R
import com.lollipop.ascensionsystem.info.SystemPreference
import com.lollipop.ascensionsystem.util.doAsync
import com.lollipop.ascensionsystem.util.logger
import com.lollipop.ascensionsystem.util.mainHandler
import com.lollipop.ascensionsystem.util.onUI
import java.net.HttpURLConnection
import java.net.URL
import kotlin.system.exitProcess

/**
 * @author lollipop
 * @date 2020/3/15 15:09
 * 应用上下文
 */
class LApplication: Application(), Application.ActivityLifecycleCallbacks {

    private val synchronizeTask = Runnable {
        updateTime()
    }
    private val systemPreference: SystemPreference by lazy {
        SystemPreference.from(this)
    }
    private val activityList = ArrayList<Activity>()
    private var topActivity: Activity? = null

    private val log = logger()

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
    }

    private fun synchronize() {
        topActivity?:return
        mainHandler.removeCallbacks(synchronizeTask)
        mainHandler.postDelayed(synchronizeTask, 1000L * 10)
    }

    private fun updateTime() {
        doAsync ({
            synchronizeError()
        }) {
            val url = URL("https://www.baidu.com/") //取得资源对象
            val connection = url.openConnection() //生成连接对象
            connection.connect() //发出连接
            val onlineTime = connection.date //取得网站日期时间
            if (connection is HttpURLConnection) {
                connection.disconnect()
            }
            systemPreference.setTimeOffset(onlineTime - System.currentTimeMillis())
            synchronize()
        }
    }

    private fun exit() {
        exitProcess(0)
    }

    private fun synchronizeError() {
        val activity = topActivity
        log("synchronizeError -> $activity")
        if (activity != null) {
            onUI {
                AlertDialog.Builder(activity)
                    .setTitle(R.string.title_synchronize_error)
                    .setMessage(R.string.message_synchronize_error)
                    .setPositiveButton(R.string.btn_retry) { dialog, _ ->
                        synchronize()
                        dialog.dismiss()
                    }
                    .setNegativeButton(R.string.btn_exit) { dialog, _ ->
                        dialog.dismiss()
                        exit()
                    }
                    .setOnDismissListener {
                        synchronize()
                    }
                    .show()
            }
        }
    }

    override fun onActivityPaused(activity: Activity) {
        log("onActivityPaused -> $activity")
    }

    override fun onActivityStarted(activity: Activity) {
        log("onActivityStarted -> $activity")
        topActivity = activity
        // 启动Activity就开始同步
        synchronize()
    }

    override fun onActivityDestroyed(activity: Activity) {
        log("onActivityDestroyed -> $activity")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        log("onActivitySaveInstanceState -> $activity")
    }

    override fun onActivityStopped(activity: Activity) {
        log("onActivityStopped -> $activity")
        if (topActivity == activity) {
            topActivity = null
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        log("onActivityCreated -> $activity")
        activityList.add(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        log("onActivityResumed -> $activity")
    }

}