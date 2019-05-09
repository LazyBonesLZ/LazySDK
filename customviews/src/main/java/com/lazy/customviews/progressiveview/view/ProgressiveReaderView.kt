package com.lazy.customviews.progressiveview.view

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lazy.customviews.R
import com.lazy.customviews.progressiveview.BookAdapter
import com.lazy.customviews.progressiveview.model.Line
import kotlinx.android.synthetic.main.layout_progressvie_readerview.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.schedule

class ProgressiveReaderView : LinearLayout {
    private var mPageAdapter: BookAdapter? = null
    private var articleTitle: TextView? = null
    private var articleContent: RecyclerView? = null
    private var articleStatus: TextView? = null
    private var articleProgressBar: ReadProgressBar? = null

    private var mThreadRunning = true
    private var mState = STATE_UNKNOWN

    var mSpeed = 5000 // 1000 chars per min
    var mTotalTime = 0 // ms
    private var mCurLineReadTime = 0L //ms
    private var mCurPage = 0
    private var mCurLine = 0
    private var mProgress = 0
    private var mTimer: Timer? = null

    private var mPagesMap = HashMap<Int, ArrayList<Line>>()



    constructor(context: Context?, attrs: AttributeSet?)
            : super(context, attrs) {
        init(context)
    }


    fun setArticleTitle(title: String?) {
        articleTitle?.text = title ?: ""
    }

    fun parseArticle(page: Int, filePath: String) {

    }

    private fun init(context: Context?) {
        val root = LayoutInflater.from(context).inflate(R.layout.layout_progressvie_readerview, this, true)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
//        addView(root,layoutParams)

        articleContent = root.articleContent
        articleTitle = root.articleTitle
//        articleContentArea = root.articleContentArea
        articleStatus = root.articleStatus
        articleProgressBar = root.articleReadProgress

//        articleStatus?.visibility = View.VISIBLE

        contentAreaRelayout()

//        initThread()


    }

    private fun getCurrentLineReadTime(line: Int, speed: Int): Int {
        val perChar = 60 * 1000 / speed
        val count = mPageAdapter!!.getLineChartCount(line)
        if (count > 0) {
            var time = perChar * count
            if (time < FRAME_RATE)
                time = FRAME_RATE
            return time
        }
        return 0

    }

    private fun initIndexs() {
        mCurLine = 0
        mCurPage = 0
        mProgress = 0
    }

    private fun contentAreaRelayout() {
        articleContent?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= 16) {
                    articleContent?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                } else {
                    articleContent?.viewTreeObserver?.removeGlobalOnLayoutListener(this)
                }

                mPageAdapter = BookAdapter()
                articleContent?.adapter = mPageAdapter
                articleContent?.layoutManager = LinearLayoutManager(context)

                mPagesMap = initTestData()
                mPageAdapter?.updateDataSource(mPagesMap[mCurPage]!!)

                val itemH = articleContent?.measuredHeight!! / mPageAdapter?.itemCount!!
                mPageAdapter?.setItemHeight(itemH)
                Log.e("tag", "${articleContent?.height},$itemH")

            }

        })

    }

    private fun initTestData(): HashMap<Int, ArrayList<Line>> {
        var map = HashMap<Int, ArrayList<Line>>()
        for (i in 0 until 3) {
            var lines = 20
            val list = ArrayList<Line>()
            if (i == 2) {
                lines = 10
            }
            for (j in 0 until lines) {
                val line = Line()
//                line.text = "文件和目录已经被提交到仓库，page:${i + 1},line: ${j + 1}"
                line.text = "page:${i + 1},line: ${j + 1}"
                list.add(line)
            }
            map[i] = list
        }
        return map
    }

    fun onPause() {
        if (mState == STATE_READ_PAUSE)
            return
        mState = STATE_READ_PAUSE
        cancelTimer()

    }

    fun onResume() {
        if (mState == STATE_READING)
            return
        mState = STATE_READING
        updateMask()

    }

    fun restart() {
        initIndexs()
        mPageAdapter?.updateDataSource(mPagesMap[mCurPage]!!)
        mState = STATE_READING
        updateMask()

    }

    fun onDestory() {
        mPagesMap?.clear()
        cancelTimer()
    }

    private fun cancelTimer() {
        if (mTimer != null) {
            mTimer?.cancel()
            mTimer = null
        }
        mCurLineReadTime = 0
    }

    private fun updateMask() {

        if (mState != STATE_READING)
            return

        val period = getCurrentLineReadTime(mCurLine, mSpeed) / (1000 / mPageAdapter?.itemCount!!).toLong()
        if (period != 0L) {
            //如果新获取的时间和上次的时间一致，就不新创建计时器，重复使用。否则，取消当前计时器，重新创建任务
            if (mCurLineReadTime != period) {
                cancelTimer()
                Log.e("tag","$period,new timer")
                mCurLineReadTime = period
                mTimer = Timer()
                mTimer?.scheduleAtFixedRate(object : TimerTask() {
                    override fun run() {
                        mHandler.sendEmptyMessage(MSG_REDAER_PROGRESS)
                    }

                }, period, period)
            }
        } else {
            //文章结束？
            cancelTimer()
            mState = STATE_READ_COMPLETE
        }
    }


    // 创建一个Handler
    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                MSG_REDAER_PROGRESS -> {
                    val step = 1000 / mPageAdapter?.itemCount!!
                    mProgress++
                    articleProgressBar?.setProgress(mProgress)

                    //换行
                    if (mProgress % step == 0) {
                        mCurLine++
                    }
                    //换页
                    if (mProgress >= 1000) {
                        mCurPage++
                        mProgress = 0
                        mCurLine = 0
                        mPageAdapter?.updateDataSource(mPagesMap[mCurPage]!!)

                    }

                    updateMask()
                }

            }
        }
    }


    companion object {
        const val MSG_REDAER_PROGRESS = 1

        const val STATE_UNKNOWN = -1
        const val STATE_READING = 100
        const val STATE_READ_PAUSE = 101
        const val STATE_READ_COMPLETE = 102

        const val FRAME_RATE = 17


    }


}