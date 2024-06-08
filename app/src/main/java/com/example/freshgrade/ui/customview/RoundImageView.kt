package com.example.freshgrade.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class RoundImageView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {

    private val path = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updatePath(w, h)
    }

    private fun updatePath(width: Int, height: Int) {
        val radius = width.coerceAtMost(height) / 2f
        path.reset()
        path.addCircle(width / 2f, height / 2f, radius, Path.Direction.CCW)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.clipPath(path)
        super.onDraw(canvas)
    }
}