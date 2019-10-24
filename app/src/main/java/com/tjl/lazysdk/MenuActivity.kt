package com.tjl.lazysdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.baidu.ocr.demo.OcrActivity
import com.tjl.lazysdk.adapter.SimpleMenuAdapter
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val menus = arrayListOf("自定义Button", "HuaWei HMS", "百度扫描", "ReaderView")
        val adapter = SimpleMenuAdapter(this, menus)
        adapter.setOnItemClickListener(object : SimpleMenuAdapter.OnItemClickListener {
            override fun onItemClick(title: String, position: Int) {
                when (title) {
                    "自定义Button" -> {
                        startActivity(Intent(this@MenuActivity,CustomButtonActivity::class.java))
                    }
                    "HuaWei HMS" -> {
                        startActivity(Intent(this@MenuActivity,HMSActivity::class.java))
                    }
                    "百度扫描" -> {
                        startActivity(Intent(this@MenuActivity, OcrActivity::class.java))
                    }
                    "ReaderView" -> {
                        startActivity(Intent(this@MenuActivity, ProgressiveReadActivity::class.java))
                    }
                }
            }

        })
        menuGrid.adapter = adapter
        menuGrid.layoutManager = GridLayoutManager(this,2)
    }
}


