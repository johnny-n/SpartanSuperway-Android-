package com.engr195.spartansuperway.spartansuperway.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import android.widget.FrameLayout

class CircleFrameLayout : FrameLayout {

    val path = Path()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Compute circular path
        val halfWidth = w / 2f
        val halfHeight = h / 2f;
        val centerX = halfWidth;
        val centerY = halfHeight
        path.reset()
        path.addCircle(centerX, centerY, Math.min(halfWidth, halfHeight), Path.Direction.CW)

    }

    override fun dispatchDraw(canvas: Canvas?) {
        val save = canvas?.save()
        canvas?.clipPath(path)
        super.dispatchDraw(canvas)
        canvas?.restoreToCount(save!!)
    }
}