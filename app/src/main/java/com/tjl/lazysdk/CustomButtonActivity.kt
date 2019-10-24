package com.tjl.lazysdk

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_custombutton.*


class CustomButtonActivity : AppCompatActivity() {
    companion object {
        const val TAG: String = "CustomButtonActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custombutton)
        btnRoundRect.setOnClickListener {
            val title = btnRoundRect.getTitle()
            showToast(title)
        }

        btnCircle.setOnClickListener {
            val title = btnCircle.getTitle()
            showToast(title)
        }

        btnRect.setOnClickListener {
            val title = btnRect.getTitle()
            showToast(title)
        }

        var index = 0
        var title = btnRoundRectWithoutIcon.getTitle()
        btnRoundRectWithoutIcon.setOnClickListener {
            index++
            btnRoundRectWithoutIcon.setTitle("$title $index")
            val tmpTitle = btnRoundRectWithoutIcon.getTitle()
            showToast(tmpTitle)
        }

    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


}
