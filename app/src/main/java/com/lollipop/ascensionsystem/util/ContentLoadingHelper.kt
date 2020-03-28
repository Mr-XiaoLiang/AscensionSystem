package com.lollipop.ascensionsystem.util

import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import kotlin.math.abs
import kotlin.math.max

/**
 * @author lollipop
 * @date 2020/3/28 18:24
 * 全屏加载提示框的辅助器
 */
class ContentLoadingHelper private constructor(private val loadingIcon: View) {

    companion object {
        fun bind(view: View): ContentLoadingHelper {
            return ContentLoadingHelper(view)
        }
        private const val DELAYED = 300L
        private const val ANIMATOR_DURATION = DELAYED
        private const val MIN_SHOWN = 800L
    }

    private var animationProgress = 0F

    private var isLoading = false
    private var startTime = 0L

    init {
        loadingIcon.visibility = View.INVISIBLE
        loadingIcon.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener{
            override fun onViewDetachedFromWindow(v: View?) {
                loadingIcon.removeCallbacks(shownPendingTask)
                loadingIcon.removeCallbacks(hidePendingTask)
                animator.cancel()
                rotationAnimator.cancel()
            }
            override fun onViewAttachedToWindow(v: View?) {
            }
        })
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
        loadingIcon.removeCallbacks(shownPendingTask)
        loadingIcon.removeCallbacks(hidePendingTask)
        loadingIcon.postDelayed(shownPendingTask, DELAYED)
    }

    fun hide() {
        loadingIcon.removeCallbacks(shownPendingTask)
        loadingIcon.removeCallbacks(hidePendingTask)
        if (isLoading) {
            val delay = max(MIN_SHOWN - System.currentTimeMillis() + startTime, 0)
            loadingIcon.postDelayed(hidePendingTask, delay)
        }
    }

    private fun onAnimationProgressChange(float: Float) {
        animationProgress = float
        loadingIcon.scaleX = float
        loadingIcon.scaleY = float
    }

    private fun onAnimationStart() {
        if (isLoading) {
            rotationAnimator.start()
            loadingIcon.visibility = View.VISIBLE
        }
    }

    private fun onAnimationEnd() {
        if (!isLoading) {
            rotationAnimator.cancel()
            loadingIcon.visibility = View.INVISIBLE
        }
    }

}