package com.lollipop.ascensionsystem.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.lollipop.ascensionsystem.R
import com.lollipop.ascensionsystem.info.RoleInfo
import com.lollipop.ascensionsystem.util.*
import com.lollipop.ascensionsystem.view.CheckedButton
import kotlinx.android.synthetic.main.activity_guide.*

/**
 * 引导的页面
 */
class GuideActivity : AppCompatActivity() {

    companion object {
        private const val DURATION = 1500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)
        initView()
    }

    private fun initView() {
        showButton(manBtn)
        showButton(womanBtn)
        nextBtn.hide()
        nextBtn.postDelayed({
            nextBtn.visibility = View.VISIBLE
            nextBtn.show()
        }, DURATION)
        CheckedButton.CheckedGroup()
            .bind(womanBtn)
            .checked(manBtn)
            .borderColor(R.color.colorPrimary)
            .borderWidth(5F)
            .onCheckedChange {
        }
        nextBtn.setOnClickListener {
            FullLoadingHelper.showIn(window.decorView as ViewGroup).show()
            doAsync {
                ComputingCore.initRole(this@GuideActivity)
                startActivity(Intent(this@GuideActivity, MainActivity::class.java))
            }
        }
    }

    private fun showButton(button: View) {
        button.post {
            button.translationY = button.height * 1F
            button.alpha = 0F
            button.animate().let {
                it.cancel()
                it.translationY(0F)
                it.alpha(1F)
                it.duration = DURATION
                it.lifecycleBinding {
                    onStart { anim ->
                        removeThis(anim)
                        if (button.visibility != View.VISIBLE) {
                            button.visibility = View.VISIBLE
                        }
                    }
                }
                it.start()
            }
        }
    }

}
