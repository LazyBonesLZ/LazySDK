package com.tjl.lazysdk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tjl.lazysdk.R
import kotlinx.android.synthetic.main.layout_list_item.view.*


class SimpleRecycleViewAdapter(var context: Context, var mDataList:ArrayList<String>):RecyclerView.Adapter<SimpleRecycleViewAdapter.ViewHolder>() {
     private var onItemClickListener :OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root= LayoutInflater.from(context).inflate(R.layout.layout_list_item,parent,
            false)
        return ViewHolder(root)
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val title = mDataList[position]
        holder.tvTitle.text = title
        holder.tvTitle.setOnClickListener {
            onItemClickListener?.onItemClick(title,position)
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }

    class ViewHolder(root: View):RecyclerView.ViewHolder(root){
        val tvTitle = root.itemTitle
    }

    interface OnItemClickListener{
        fun onItemClick(title:String,position: Int)
    }
}