package com.tjl.lazysdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lazy.customviews.progressiveview.ProgressiveReadListener
import kotlinx.android.synthetic.main.activity_progressive_read.*

class ProgressiveReadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progressive_read)
        reader.setReadListener(object : ProgressiveReadListener {
            override fun onReadCompleted() {

            }

            override fun onChangePage(page: Int) {

            }

            override fun onReadPause() {
                reader.showStatusText("暂停中。。。。")
            }

            override fun onReadError(error: String) {
            }

        })

        resume.setOnClickListener {
            reader.onResume()
        }

        pause.setOnClickListener {
            reader.onPause()
        }

        restart.setOnClickListener {
            reader.restart()
        }

    }
}
