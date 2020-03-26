package com.lollipop.ascensionsystem.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.Checkable
import androidx.cardview.widget.CardView
import kotlin.math.max
import kotlin.math.min

/**
 * @author lollipop
 * @date 2020/3/26 01:13
 * 可以选中的按钮
 */
class CheckedButton(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int):
    CardView(context, attributeSet, defStyleAttr), Checkable {

    constructor(context: Context, attributeSet: AttributeSet?): this(context, attributeSet, 0)
    constructor(context: Context): this(context, null)

    private var isChecked = false

    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun toggle() {
        isChecked = !isChecked
        inStatusChange()
    }

    override fun setChecked(checked: Boolean) {
        isChecked = checked
        inStatusChange()
    }

    private fun inStatusChange() {
        // TODO
    }

    private class BorderDrawable: Drawable() {

        private val paint = Paint().apply {
            isAntiAlias = true
            isDither = true
        }

        var color: Int
            get() {
                return paint.color
            }
            set(value) {
                paint.color = color
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
                field = max(1F, value)
                updateBorder()
            }

        private val boundsF = RectF()
        private val borderPath = Path()
        private val drawingPath = Path()
        private val pathMeasure = PathMeasure()

        override fun onBoundsChange(b: Rect?) {
            super.onBoundsChange(b)
            val padding = borderWidth / 2
            boundsF.set(bounds.left + padding, bounds.top + padding,
                bounds.right - padding, bounds.bottom - padding)
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

}