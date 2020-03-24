package com.lollipop.ascensionsystem.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.ascensionsystem.R
import com.lollipop.ascensionsystem.util.banner.LBannerLayoutManager
import com.lollipop.ascensionsystem.util.banner.Orientation
import com.lollipop.ascensionsystem.util.lifecycleBinding
import com.lollipop.ascensionsystem.util.onStart
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
    }

    private fun showButton(button: View) {
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
