package com.example.freshgrade.ui.decoration

import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.max



class CarouselItemDecoration : RecyclerView.ItemDecoration() {
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val centerX = parent.width / 2f
        val d0 = 0.0f
        val d1 = 0.6f * parent.width
        val s0 = 1.0f
        val s1 = 0.4f

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val childCenterX = (child.left + child.right) / 2f
            val d = minOf(d1, abs(centerX - childCenterX))
            val scaleFactor = s0 + (s1 - s0) * (d - d0) / (d1 - d0)
            val translationFactor = centerX - childCenterX
            val alphaFactor = 1 - (d / d1)

            child.scaleX = scaleFactor
            child.scaleY = scaleFactor
            child.translationX = 0.3f * translationFactor
            child.alpha = max(0.5f, alphaFactor)
        }
    }
}