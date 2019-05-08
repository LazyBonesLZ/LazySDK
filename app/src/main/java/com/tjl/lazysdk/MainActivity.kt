package com.tjl.lazysdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.lazy.customviews.progressiveview.BookAdapter
import com.lazy.customviews.progressiveview.model.Line
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        btnRoundRect.setOnClickListener {
//            val title = btnRoundRect.getTitle()
//            showToast(title)
//        }
//
//        btnCircle.setOnClickListener {
//            val title = btnCircle.getTitle()
//            showToast(title)
//        }
//
//        btnRect.setOnClickListener {
//            val title = btnRect.getTitle()
//            showToast(title)
//        }
//
//        var index = 0
//        var title = btnRoundRectWithoutIcon.getTitle()
//        btnRoundRectWithoutIcon.setOnClickListener {
//            index++
//            btnRoundRectWithoutIcon.setTitle("$title $index")
//            val tmpTitle = btnRoundRectWithoutIcon.getTitle()
//            showToast(tmpTitle)
//        }

        resume.setOnClickListener {
            reader.onResume()
        }

        pause.setOnClickListener {
            reader.onPause()
        }

    }

    fun showToast(msg:String){
        Toast.makeText(this ,msg, Toast.LENGTH_SHORT).show()
    }

}
