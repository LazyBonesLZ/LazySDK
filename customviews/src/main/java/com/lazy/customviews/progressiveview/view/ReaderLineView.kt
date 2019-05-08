package com.lazy.customviews.progressiveview.view

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.marginLeft

class ReaderLineView(context: Context?) : RelativeLayout(context) {
    var textView: TextView = TextView(context)
    var baseLine: View = View(context)
    var textColor: Int = Color.BLACK

    var textContent: String? = null
    var lineNumber: Int = 0

    //    var textFont
    init {

        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        layoutParams.bottomMargin = 10
//        textView.gravity = Gravity.CENTER
        addView(textView, layoutParams)
        baseLine.setBackgroundColor(Color.RED)

        val baseLienLp = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, 1
        )
        baseLienLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        addView(baseLine, baseLienLp)
        setLayoutParams(ViewGroup.LayoutParams(-1,-1))


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