package com.lollipop.ascensionsystem.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.min

/**
 * @author lollipop
 * @date 2020/3/19 01:21
 * 能力条
 */
class CapabilityBar(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int):
    AppCompatImageView(context, attributeSet, defStyleAttr) {

    constructor(context: Context, attributeSet: AttributeSet?): this(context, attributeSet, 0)
    constructor(context: Context): this(context, null)

    private val barDrawable = BarDrawable()

    init {
        val bg = background
        if (bg is ColorDrawable) {
            barDrawable.color = bg.color
        }
        background = barDrawable
        if (isInEditMode) {
            barDrawable.progress = 0.5F
        }
    }

    private class BarDrawable: Drawable() {

        private val paint = Paint().apply {
            isAntiAlias = true
            isDither = true
            style = Paint.Style.FILL
        }
        private val boundsRectF = RectF()
        private val backgroundAlpha = 0.3F

        var color: Int
            get() = paint.color
            set(value) {
                paint.color = value
            }

        var progress = 0F
            set(value) {
                field = value
                invalidateSelf()
            }


        override fun draw(canvas: Canvas) {
            val r = min(boundsRectF.height(), boundsRectF.width()) / 2
            val oldAlpha = paint.alpha
            paint.alpha = (oldAlpha * backgroundAlpha).toInt()
            canvas.drawRoundRect(boundsRectF, r, r, paint)
            paint.alpha = oldAlpha
            val right = boundsRectF.width() * progress + boundsRectF.left
            canvas.drawRoundRect(boundsRectF.left, boundsRectF.top,
                right, boundsRectF.bottom, r, r, paint)
        }

        override fun setAlpha(alpha: Int) {
            paint.alpha = alpha
            invalidateSelf()
        }

        override fun onBoundsChange(bounds: Rect?) {
            super.onBoundsChange(bounds)
            boundsRectF.set(getBounds())
            invalidateSelf()
        }

        override fun getOpacity() = PixelFormat.TRANSPARENT

        override fun setColorFilter(colorFilter: ColorFilter?) {
            paint.colorFilter = colorFilter
        }

    }

}