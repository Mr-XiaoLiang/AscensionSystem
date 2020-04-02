package com.lollipop.ascensionsystem.activity

import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

    private val rotationAnimator = ValueAnimator().apply {
        setFloatValues(0F, 360F)
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.RESTART
        duration = ROTATION_DURATION
        addUpdateListener {
            logoView.rotation = it.animatedValue as Float
        }
    }

    private var pauseTime = 0L

    private var progress = 0F

    private var step = 0F

    private val progressUpdateTask = Runnable {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)

        logoView.background = progressDrawable
        progressDrawable.strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 2F, resources.displayMetrics)
        progressDrawable.color = ContextCompat.getColor(this, R.color.colorPrimary)
        progressDrawable.progress = 0F

        val allLength = intent.getLongExtra(ARG_TIME_LENGTH, SMALL_LOOP)

    }

    override fun onStart() {
        super.onStart()
        postProgressUpdate()
    }

    override fun onResume() {
        super.onResume()
        rotationAnimator.start()
        pauseTime = 0L
    }

    override fun onPause() {
        super.onPause()
        rotationAnimator.pause()
        pauseTime = System.currentTimeMillis()
    }

    override fun onStop() {
        super.onStop()
        rotationAnimator.cancel()
        logoView.removeCallbacks(progressUpdateTask)
    }

    private fun postProgressUpdate() {
        logoView.postDelayed(progressUpdateTask, PROGRESS_UPDATE_DELAYED)
    }

    private class ProgressDrawable : Drawable() {

        private val paint = Paint().apply {
            isAntiAlias = true
            isDither = true
            strokeCap = Paint.Cap.ROUND
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
            val radius = min(bounds.width(), bounds.height())
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
