package com.lazy.customviews.progressiveview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lazy.customviews.progressiveview.model.Line
import com.lazy.customviews.progressiveview.view.ReaderLineView

class BookAdapter : RecyclerView.Adapter<BookAdapter.ViewHolder>() {
    var list: ArrayList<Line?> = ArrayList(10)
    private var itemHeight = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookAdapter.ViewHolder {
        val rootView = ReaderLineView(parent.context)
        if (itemHeight != -1)
            rootView.layoutParams?.height = itemHeight
//            rootView.viewTreeObserver.addOnGlobalLayoutListener(object :
//                ViewTreeObserver.OnGlobalLayoutListener {
//                override fun onGlobalLayout() {
//                    if (Build.VERSION.SDK_INT >= 16) {
//                        rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                    } else {
//                        rootView.viewTreeObserver.removeGlobalOnLayoutListener(this)
//                    }
//
//                }
//
//            })
//        val rootView = LayoutInflater.from(parent.context)
//            .inflate(R.layout.layout_lineview,null, false)
        return ViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun onBindViewHolder(holder: BookAdapter.ViewHolder, position: Int) {
        if (position <= list.size - 1) {
            val line = list[position]
            if (line != null) {
                holder.readerLineView.lineNumber = line.line
                holder.readerLineView.setContent(line.text)
                holder.readerLineView.setContentColor(line.textColor)
            }

//            if (line != null) {
//                holder.textView.text = line.text
//            }
        }
//        if (position % 2 == 0){
//            holder.readerLineView.setBackgroundColor(Color.BLUE)
//        }
    }

    class ViewHolder(itemView: ReaderLineView) : RecyclerView.ViewHolder(itemView) {
        val readerLineView = itemView
//        var textView:TextView = itemView.findViewById(R.id.lineContent)
    }

    fun updateDataSource(list: ArrayList<Line>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun setItemHeight(height: Int) {
        itemHeight = height
        notifyDataSetChanged()
    }

    fun getLineChartCount(lineIdx:Int):Int{
        if (lineIdx <= list.size -1){
            val line = list[lineIdx]
            if (line != null){
                return line.text.length
            }
        }
        return 0
    }
}