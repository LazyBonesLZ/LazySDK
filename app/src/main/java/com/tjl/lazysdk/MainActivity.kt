package com.tjl.lazysdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.lazy.customviews.progressiveview.ProgressiveReadListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

        goProgressiveReader.setOnClickListener {
            startActivity(Intent(this,ProgressiveReadActivity::class.java))
        }

        goBookFliper.setOnClickListener {
            startActivity(Intent(this,BookFilpActivity::class.java))
        }

    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}
