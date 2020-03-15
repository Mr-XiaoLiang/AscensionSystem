package com.lollipop.ascensionsystem.base

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.lollipop.ascensionsystem.R
import kotlinx.android.synthetic.main.activity_base.*

/**
 * @author lollipop
 * @date 2020/3/15 15:11
 * 基础的Activity
 */
@SuppressLint("Registered")
abstract class BaseActivity: AppCompatActivity() {

    abstract val layoutId: Int

    protected var isShowBack = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        initRootGroup(rootGroup)
        layoutInflater.inflate(layoutId, contentGroup, true)
    }

    private fun initRootGroup(group: View) {
        val attributes = window.attributes
        attributes.systemUiVisibility = (
                attributes.systemUiVisibility
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        group.fitsSystemWindows = true
        group.setOnApplyWindowInsetsListener { _, insets ->
            onInsetsChange(insets.systemWindowInsetLeft, insets.systemWindowInsetTop,
                insets.systemWindowInsetRight, insets.systemWindowInsetBottom)
            insets.consumeSystemWindowInsets()
        }
        group.setOnClickListener {
            hideSystemUI()
        }
        backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        backBtn.visibility = if (isShowBack) { View.VISIBLE } else { View.GONE }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (window.decorView.systemUiVisibility
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LOW_PROFILE)
    }

    protected open fun onInsetsChange(left: Int, top: Int, right: Int, bottom: Int) {
        rootGroup.setPadding(left, top, right, bottom)
    }

}