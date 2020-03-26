package com.lollipop.ascensionsystem.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.Checkable
import androidx.cardview.widget.CardView

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
                field = value
                updatePath()
            }

        var corner = 0F
            set(value) {
                field = value
                updateBorder()
            }

        private val boundsF = RectF()
        private val borderPath = Path()
        private val drawingPath = Path()
        private val pathMeasure = PathMeasure()

        override fun onBoundsChange(bounds: Rect?) {
            super.onBoundsChange(bounds)
            updateBorder()
        }

        private fun updateBorder() {
            boundsF.set(bounds)
            borderPath.reset()
            borderPath.addRoundRect(boundsF, corner, corner, Path.Direction.CW)
            pathMeasure.setPath(borderPath, false)
            updatePath()
        }

        private fun updatePath() {

            invalidateSelf()
        }

        override fun draw(canvas: Canvas) {
            PathMeasure(borderPath, false)

            TODO("Not yet implemented")
        }

        override fun setAlpha(alpha: Int) {
            TODO("Not yet implemented")
        }

        override fun getOpacity(): Int {
            TODO("Not yet implemented")
        }

        override fun setColorFilter(colorFilter: ColorFilter?) {
            TODO("Not yet implemented")
        }

    }

}