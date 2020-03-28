package com.lollipop.ascensionsystem.util

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import com.lollipop.ascensionsystem.R
import kotlin.math.abs
import kotlin.math.max

/**
 * @author lollipop
 * @date 2020/3/28 18:24
 * 全屏加载提示框的辅助器
 */
class FullLoadingHelper private constructor(private val group: ViewGroup) {

    companion object {
        fun showIn(group: ViewGroup): FullLoadingHelper {
            return FullLoadingHelper(group)
        }
        private const val DELAYED = 300L
        private const val ANIMATOR_DURATION = DELAYED
        private const val MIN_SHOWN = 800L
    }

    private val loadingGroup: View = LayoutInflater.from(group.context).inflate(
        R.layout.fragment_full_loading, group, false)
    private val loadingView: View = loadingGroup.findViewById(R.id.loadingView)
    private val loadingIcon: View = loadingGroup.findViewById(R.id.loadingIcon)

    private var animationProgress = 0F
    private var backgroundColor: Int = 0x88000000.toInt()

    private var isLoading = false
    private var startTime = 0L

    init {
        loadingGroup.visibility = View.INVISIBLE
        loadingGroup.setOnTouchListener { _, _ -> true }
        group.addView(loadingGroup)
        group.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener{
            override fun onViewDetachedFromWindow(v: View?) {
                loadingGroup.removeCallbacks(shownPendingTask)
                loadingGroup.removeCallbacks(hidePendingTask)
                animator.cancel()
                rotationAnimator.cancel()
            }
            override fun onViewAttachedToWindow(v: View?) {
            }
        })
    }

    fun backgroundColor(color: Int) {
        backgroundColor = color
    }

    private val animator: ValueAnimator by lazy {
        ValueAnimator().apply {
            addUpdateListener {
                onAnimationProgressChange(it.animatedValue as Float)
            }
            lifecycleBinding {
                onStart {
                    onAnimationStart()
                }
                onEnd {
                    onAnimationEnd()
                }
            }
        }
    }

    private val rotationAnimator: ValueAnimator by lazy {
        ValueAnimator().apply {
            addUpdateListener {
                loadingIcon.rotation = it.animatedValue as Float
                log("loadingIcon is update : ${loadingIcon.rotation}")
            }
            setFloatValues(0F, 360F)
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            duration = MIN_SHOWN
            interpolator = LinearInterpolator()
        }
    }

    private val shownPendingTask = Runnable {
        isLoading = true
        startTime = System.currentTimeMillis()
        animator.cancel()
        val endValue = 1F
        animator.setFloatValues(animationProgress, endValue)
        animator.duration = (ANIMATOR_DURATION * abs(animationProgress - endValue)).toLong()
        animator.interpolator = AccelerateInterpolator()
        animator.start()
    }

    private val hidePendingTask = Runnable {
        isLoading = false
        startTime = 0
        animator.cancel()
        val endValue = 0F
        animator.setFloatValues(animationProgress, endValue)
        animator.duration = (ANIMATOR_DURATION * abs(animationProgress - endValue)).toLong()
        animator.interpolator = AccelerateInterpolator()
        animator.start()
    }

    fun show() {
        if (isLoading) {
            return
        }
        loadingGroup.removeCallbacks(shownPendingTask)
        loadingGroup.removeCallbacks(hidePendingTask)
        loadingGroup.postDelayed(shownPendingTask, DELAYED)
    }

    fun hide() {
        loadingGroup.removeCallbacks(shownPendingTask)
        loadingGroup.removeCallbacks(hidePendingTask)
        if (isLoading) {
            val delay = max(MIN_SHOWN - System.currentTimeMillis() + startTime, 0)
            loadingGroup.postDelayed(hidePendingTask, delay)
        }
    }

    private fun onAnimationProgressChange(float: Float) {
        animationProgress = float
        loadingGroup.setBackgroundColor(backgroundColor.alpha(float))
        loadingView.scaleX = float
        loadingView.scaleY = float
    }

    private fun onAnimationStart() {
        if (isLoading) {
            rotationAnimator.start()
            loadingGroup.visibility = View.VISIBLE
        }
    }

    private fun onAnimationEnd() {
        if (!isLoading) {
            rotationAnimator.cancel()
            loadingGroup.visibility = View.INVISIBLE
        }
    }

}