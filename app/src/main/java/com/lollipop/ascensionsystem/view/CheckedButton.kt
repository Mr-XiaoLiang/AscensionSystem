package com.lollipop.ascensionsystem.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
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
        override fun draw(canvas: Canvas) {
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