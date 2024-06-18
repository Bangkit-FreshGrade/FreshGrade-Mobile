package com.example.freshgrade.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.freshgrade.R

@SuppressLint("UseCompatLoadingForDrawables")
class PercentTextLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val textView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_percent_text_layout, this, true)
        textView = findViewById(R.id.value_tv)
        background = resources.getDrawable(R.drawable.percent_circle_red_bg, context.theme)

        updateBackgroundColor()
    }

    fun setText(percentage: String) {
        textView.text = percentage
        updateBackgroundColor()
    }

    private fun updateBackgroundColor() {
        val percentage = textView.text.toString().replace("%", "").toIntOrNull() ?: 0
        if (percentage < 50) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setBackgroundColor(resources.getColor(R.color.red, context.theme))
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setBackgroundColor(resources.getColor(R.color.green, context.theme))
            }
        }
    }
}