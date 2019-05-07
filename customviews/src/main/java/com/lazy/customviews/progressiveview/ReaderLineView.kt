package com.lazy.customviews.progressiveview

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

class ReaderLineView(context: Context?) : LinearLayout(context) {
    var textView: TextView = TextView(context)
    var baseLine: View = View(context)
    var textColor: Int = Color.BLACK

    var textContent: String? = null
    var lineNumber: Int = 0

    //    var textFont
    init {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.gravity = Gravity.CENTER_VERTICAL
        addView(textView, layoutParams)

        baseLine.setBackgroundColor(Color.DKGRAY)
        val baseLienLp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 1
        )
        baseLienLp.gravity = Gravity.BOTTOM
        baseLienLp.bottomMargin = 5
        addView(baseLine, baseLienLp)

        orientation = VERTICAL
    }

    fun setContent(text: String?) {
        textView.text = text ?: ""
    }

    fun setContentColor(color: Int) {
        textView.setTextColor(color)
    }

    fun setBaseLineColor(color: Int) {
        baseLine.setBackgroundColor(color)
    }

}