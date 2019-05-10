package com.tjl.lazysdk

import android.app.AlertDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import com.lazy.customviews.bookflipview.PageFlipView

class BookFilpActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
     var mPageFlipView: PageFlipView? = null
     var mGestureDetector: GestureDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPageFlipView = PageFlipView(this)
        setContentView(mPageFlipView)
        mGestureDetector = GestureDetector(this, this)

        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        } else {
            mPageFlipView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }

    override fun onResume() {
        super.onResume()

//        LoadBitmapTask.get(this).start()
        mPageFlipView?.onResume()
    }

    override fun onPause() {
        super.onPause()

        mPageFlipView?.onPause()
//        LoadBitmapTask.get(this).stop()
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            mPageFlipView?.onFingerUp(event.x, event.y)
            return true
        }

        return mGestureDetector!!.onTouchEvent(event)
    }

    override fun onDown(e: MotionEvent): Boolean {
        mPageFlipView?.onFingerDown(e.x, e.y)
        return true
    }

    override fun onFling(
        e1: MotionEvent, e2: MotionEvent, velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }


    override fun onLongPress(e: MotionEvent) {}

    override fun onScroll(
        e1: MotionEvent, e2: MotionEvent, distanceX: Float,
        distanceY: Float
    ): Boolean {
        mPageFlipView?.onFingerMove(e2.x, e2.y)
        return true
    }

    override fun onShowPress(e: MotionEvent) {}

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }

}
