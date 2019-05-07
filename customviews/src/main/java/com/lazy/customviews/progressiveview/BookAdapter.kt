package com.lazy.customviews.progressiveview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lazy.customviews.progressiveview.model.Line

class BookAdapter : RecyclerView.Adapter<BookAdapter.ViewHolder>() {
    var list: ArrayList<Line?> = ArrayList(10)
    private var itemHeight = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookAdapter.ViewHolder {
        val rootView = ReaderLineView(parent.context)
        if (itemHeight != -1)
            rootView.minimumHeight = itemHeight
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
        }
    }

    class ViewHolder(itemView: ReaderLineView) : RecyclerView.ViewHolder(itemView) {
        val readerLineView = itemView
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
}