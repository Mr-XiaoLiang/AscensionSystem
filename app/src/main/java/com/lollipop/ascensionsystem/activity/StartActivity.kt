package com.lollipop.ascensionsystem.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import com.lollipop.ascensionsystem.R
import com.lollipop.ascensionsystem.info.SystemPreference
import com.lollipop.ascensionsystem.util.lifecycleBinding
import com.lollipop.ascensionsystem.util.onEnd
import kotlinx.android.synthetic.main.activity_start.*

/**
 * 启动的Activity
 * @author lollipop
 * @date 2020/3/22 16:50
 */
class StartActivity : AppCompatActivity() {

    companion object {
        private const val DELAY_START = 17 * 100L
        private const val ANIMATION_DURATION = DELAY_START + 10 * 100L
    }

    private val systemPreference: SystemPreference by lazy {
        SystemPreference.from(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        init()
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        versionView.text = "V" + packageManager.getPackageInfo(packageName, 0).versionName
        logoView.animate().let { animator ->
            animator.cancel()
            animator.duration = ANIMATION_DURATION
            animator.rotation(12 * 360F)
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.withLayer()
            animator.start()
        }
        logoView.postDelayed({
            start()
        }, DELAY_START)
    }

    override fun onStop() {
        super.onStop()
        logoView.animate().cancel()
    }

    private fun start() {
//        if (systemPreference.isInit) {
//            startActivity(Intent(this, MainActivity::class.java))
//        } else {
            startActivity(Intent(this, GuideActivity::class.java))
//        }
        finish()
    }

}
