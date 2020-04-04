package com.lollipop.ascensionsystem.activity

import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lollipop.ascensionsystem.R
import com.lollipop.ascensionsystem.util.range
import kotlinx.android.synthetic.main.activity_practice.*
import kotlin.math.min

/**
 * 练功的Activity
 * @author Lollipop
 */
class PracticeActivity: AppCompatActivity() {

    companion object {
        private const val ROTATION_DURATION = 10 * 1000L

        private const val SMALL_LOOP = 15 * 60 * 1000L
        private const val BIG_CYCLE = 4 * SMALL_LOOP

        private const val PROGRESS_UPDATE_DELAYED = 500L

        private const val ARG_TIME_LENGTH = "ARG_TIME_LENGTH"
    }

    private val progressDrawable = ProgressDrawable()
    private var isStop = false
    private var rotationValue = 0F

    private val rotationAnimator = ValueAnimator().apply {
        setFloatValues(0F, 360F)
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.RESTART
        interpolator = LinearInterpolator()
        duration = ROTATION_DURATION
        addUpdateListener {
            rotationValue = it.animatedValue as Float
            logoView.rotation = rotationValue
            progressView.rotation = -rotationValue
        }
    }

    private var progress = 0F

    private var step = 0F

    private val progressUpdateTask = Runnable {
        // 消退速度是增长速度的两倍
        if (rotationAnimator.isRunning && !rotationAnimator.isPaused) {
            progress += step
        } else {
            progress -= (step * 2)
        }
        progressDrawable.progress = progress
        postProgressUpdate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)

        progressView.background = progressDrawable
        progressDrawable.strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 2F, resources.displayMetrics)
        progressDrawable.color = ContextCompat.getColor(this, R.color.colorPrimary)
        progressDrawable.progress = progress

        val allLength = intent.getLongExtra(ARG_TIME_LENGTH, SMALL_LOOP)
        step = 1F * PROGRESS_UPDATE_DELAYED / allLength

        progressView.setOnClickListener {
            BottomSheetDialog(this).apply {
                setContentView(R.layout.activity_guide)
            }.show()
        }
    }

    override fun onStart() {
        super.onStart()
        isStop = false
        postProgressUpdate()
    }

    override fun onResume() {
        super.onResume()
        changeRotationStatus(true)
    }

    override fun onPause() {
        super.onPause()
        changeRotationStatus(false)
    }

    override fun onTopResumedActivityChanged(isTopResumedActivity: Boolean) {
        super.onTopResumedActivityChanged(isTopResumedActivity)
        changeRotationStatus(isTopResumedActivity)
    }

    private fun changeRotationStatus(isRun: Boolean) {
        if (isRun) {
            rotationAnimator.setFloatValues(rotationValue, 360F + rotationValue)
            rotationAnimator.start()
        } else {
            rotationAnimator.pause()
        }
    }

    override fun onStop() {
        super.onStop()
        isStop = true
        rotationAnimator.cancel()
        logoView.removeCallbacks(progressUpdateTask)
        progress = 0F
    }

    private fun postProgressUpdate() {
        if (isStop) {
            return
        }
        logoView.postDelayed(progressUpdateTask, PROGRESS_UPDATE_DELAYED)
    }

    private class ProgressDrawable : Drawable() {

        private val paint = Paint().apply {
            isAntiAlias = true
            isDither = true
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.STROKE
        }

        var progress = 0F
            set(value) {
                field = value.range(0F, 1F)
                invalidateSelf()
            }

        var color: Int
            get() {
                return paint.color
            }
            set(value) {
                paint.color = value
            }

        var strokeWidth: Float
            get() {
                return paint.strokeWidth
            }
            set(value) {
                paint.strokeWidth = value
            }

        private val boundsF = RectF()

        override fun draw(canvas: Canvas) {
            canvas.drawArc(boundsF, 90F, 360F * progress, false, paint)
        }

        override fun onBoundsChange(b: Rect?) {
            super.onBoundsChange(b)
            val radius = min(bounds.width(), bounds.height()) / 2 - strokeWidth
            boundsF.set(bounds.exactCenterX() - radius, bounds.exactCenterY() - radius,
                bounds.exactCenterX() + radius, bounds.exactCenterY() + radius)
            invalidateSelf()
        }

        override fun setAlpha(alpha: Int) {
            paint.alpha = alpha
        }

        override fun getOpacity(): Int {
            return PixelFormat.TRANSPARENT
        }

        override fun setColorFilter(colorFilter: ColorFilter?) {
            paint.colorFilter = colorFilter
        }

    }

}
