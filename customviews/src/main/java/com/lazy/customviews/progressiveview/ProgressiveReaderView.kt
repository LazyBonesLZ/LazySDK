package com.lazy.customviews.progressiveview

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lazy.customviews.R
import com.lazy.customviews.progressiveview.model.Line
import kotlinx.android.synthetic.main.layout_progressvie_readerview.view.*

class ProgressiveReaderView : LinearLayout {
    private var mPageAdapter: BookAdapter? = null
    private var articleTitle:TextView? = null
    private var articleContent:RecyclerView? = null

    constructor(context: Context?, attrs: AttributeSet?)
            : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun setArticleTitle(title: String?) {
        articleTitle?.text = title ?: ""
    }

    fun parseArticle(page: Int, filePath: String) {

    }

    private fun init(context: Context?) {
        val root = LayoutInflater.from(context).inflate(R.layout.layout_progressvie_readerview,null,false)
        articleContent = root.articleContent
        articleTitle = root.articleTitle

        root.articleStatus.visibility = View.VISIBLE
        addView(root,LinearLayout.LayoutParams(-1,-1))

        relayout()


    }

    private fun relayout() {
        this.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= 16) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                } else {
                    viewTreeObserver.removeGlobalOnLayoutListener(this)
                }

                mPageAdapter = BookAdapter()
                articleContent?.adapter = mPageAdapter
                articleContent?.layoutManager = LinearLayoutManager(context)

                val itemH = height / 20
                mPageAdapter?.setItemHeight(itemH)

            }

        })

    }


}