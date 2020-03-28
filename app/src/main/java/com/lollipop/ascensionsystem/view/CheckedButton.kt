package com.lollipop.ascensionsystem.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * @author lollipop
 * @date 2020/3/26 01:13
 * 可以选中的按钮
 */
class CheckedButton(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int):
    MaterialCardView(context, attributeSet, defStyleAttr) {

    private val borderDrawable = BorderDrawable(this)

    constructor(context: Context, attributeSet: AttributeSet?): this(context, attributeSet, 0)
    constructor(context: Context): this(context, null)

    companion object {
        private const val ANIMATOR_DURATION = 800L
    }

    private val animator: ValueAnimator by lazy {
        ValueAnimator().apply {
            addUpdateListener {
                onAnimationProgressChange(it.animatedValue as Float)
            }
        }
    }

    private var animationProgress = 0F

    init {
        borderDrawable.corner = radius
        isCheckable = true
        checkedIcon = null
    }

    override fun toggle() {
        super.toggle()
        inStatusChange()
    }

    override fun setChecked(checked: Boolean) {
        super.setChecked(checked)
        inStatusChange()
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        canvas?:return
        borderDrawable.draw(canvas)
    }

    private fun inStatusChange() {
        val endValue = if (isChecked) { 1F } else { 0F }
        animator.cancel()
        animator.setFloatValues(animationProgress, endValue)
        animator.duration = (ANIMATOR_DURATION * abs(animationProgress - endValue)).toLong()
        animator.interpolator = AccelerateInterpolator()
        animator.start()
    }

    private fun onAnimationProgressChange(progress: Float) {
        animationProgress = progress
        borderDrawable.progress = animationProgress
    }

    fun borderColor(resId: Int) {
        borderDrawable.color = ContextCompat.getColor(context, resId)
    }

    fun borderWidth(dp: Float) {
        borderDrawable.borderWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

    override fun setRadius(radius: Float) {
        super.setRadius(radius)
        borderDrawable.corner = radius
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        borderDrawable.setBounds(0, 0, width, height)
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || borderDrawable == who
    }

    private class BorderDrawable(): Drawable() {

        constructor(drawableCallback: Callback): this() {
            super.setCallback(drawableCallback)
        }

        private val paint = Paint().apply {
            isAntiAlias = true
            isDither = true
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }

        var color: Int
            get() {
                return paint.color
            }
            set(value) {
                paint.color = value
            }

        var progress = 0F
            set(value) {
                field = min(1F, max(0F, value))
                updatePath()
            }

        var corner = 0F
            set(value) {
                field = value
                updateBorder()
            }

        var pathCount = 4
            set(value) {
                field = max(1, value)
                updatePath()
            }

        var borderWidth = 2F
            set(value) {
                val v = max(1F, value * 2)
                field = v
                paint.strokeWidth = v
                updateBorder()
            }

        private val boundsF = RectF()
        private val borderPath = Path()
        private val drawingPath = Path()
        private val pathMeasure = PathMeasure()

        override fun onBoundsChange(b: Rect?) {
            super.onBoundsChange(b)
//            val padding = borderWidth / 2
//            boundsF.set(bounds.left + padding, bounds.top + padding,
//                bounds.right - padding, bounds.bottom - padding)
            boundsF.set(bounds)
            updateBorder()
        }

        private fun updateBorder() {
            if (boundsF.isEmpty) {
                return
            }
            borderPath.reset()
            borderPath.addRoundRect(boundsF, corner, corner, Path.Direction.CW)
            pathMeasure.setPath(borderPath, false)
            updatePath()
        }

        private fun updatePath() {
            drawingPath.reset()
            val allLength = pathMeasure.length
            val stepLength = allLength / pathCount
            val progressLength = stepLength * progress
            for (index in 0 until pathCount) {
                val start = index * stepLength
                pathMeasure.getSegment(start, start + progressLength, drawingPath, true)
            }
            invalidateSelf()
        }

        override fun draw(canvas: Canvas) {
            if (boundsF.isEmpty) {
                return
            }
            canvas.drawPath(drawingPath, paint)
        }

        override fun setAlpha(alpha: Int) {
            paint.alpha = alpha
        }

        override fun getOpacity(): Int = PixelFormat.TRANSPARENT

        override fun setColorFilter(colorFilter: ColorFilter?) {
            paint.colorFilter = colorFilter
        }

    }

    class CheckedGroup : OnClickListener {
        private var checkedView: CheckedButton? = null

        private var onCheckedChangeListener: ((CheckedButton) -> Unit)? = null

        private val viewList = ArrayList<CheckedButton>()

        fun bind(view: CheckedButton): CheckedGroup {
            view.setOnClickListener(this)
            viewList.add(view)
            return this
        }

        override fun onClick(v: View?) {
            if (v is CheckedButton) {
                if (v != checkedView) {
                    checkedView?.isChecked = false
                    checkedView = v
                    v.isChecked = true
                    onCheckedChangeListener?.invoke(v)
                }
            }
        }

        fun checked(view: CheckedButton): CheckedGroup {
            bind(view)
            onClick(view)
            return this
        }

        fun onCheckedChange(listener: (CheckedButton) -> Unit) {
            onCheckedChangeListener = listener
        }

        fun borderColor(resId: Int): CheckedGroup {
            viewList.forEach {
                it.borderColor(resId)
            }
            return this
        }

        fun borderWidth(dp: Float): CheckedGroup {
            viewList.forEach {
                it.borderWidth(dp)
            }
            return this
        }

    }

}