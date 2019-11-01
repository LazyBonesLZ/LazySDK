package com.tjl.lazysdk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tjl.hms.iap.Constants
import com.tjl.hms.iap.Product
import com.tjl.hms.iap.ProductUitl
import com.tjl.lazysdk.R
import kotlinx.android.synthetic.main.layout_iap_item.view.*


class SimpleIapAdapter(var context: Context, var mDataList: ArrayList<Product>?) :
    RecyclerView.Adapter<SimpleIapAdapter.ViewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(context).inflate(
            R.layout.layout_iap_item, parent,
            false
        )
        return ViewHolder(root)
    }

    override fun getItemCount(): Int {
        return if (mDataList == null) 0 else mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = mDataList!![position]
        holder.button.setTitle("购买")
        when (product.type) {
            Constants.PRODUCT_TYPE_RENEWABLE_SUBSCRIPTION -> {
                holder.button.setTitle("订阅")
            }
            Constants.PRODUCT_TYPE_CONSUMABLE -> {

                holder.clickArea.setOnClickListener {
                    onItemClickListener?.onItemClick(product, position, Constants.ACTION_CONSUME)
                }
            }

        }

        holder.button.setOnClickListener {
            onItemClickListener?.onItemClick(product, position, Constants.ACTION_BUY)
        }
        holder.title.text = product.productName
        holder.price.text = product.price

        val inAppPurchaseData = ProductUitl.covertInAppPurchaseData(product)
        if (inAppPurchaseData != null) {
            var status = ""
            when (inAppPurchaseData.purchaseState) {
                Constants.STATE_NON_BUY -> {
                    status = "未购买"
                }
                Constants.STATE_UNPURCHASE -> {
                    status = "未支付"
                    if (product.type == Constants.PRODUCT_TYPE_RENEWABLE_SUBSCRIPTION){
                        status = if (inAppPurchaseData.subIsvalid){
                            "已取消，剩余有效天数：${inAppPurchaseData.cancelledSubKeepDays}"
                        }else{
                            "已过期"
                        }
                    }
                }
                Constants.STATE_PURCHASED -> {
                    status = "已购买"
                    if (product.type == Constants.PRODUCT_TYPE_CONSUMABLE){
                        when(product.consumeState ){
                            Constants.STATE_UNCONSUMED ->{
                                status = "未消费"
                            }
                            Constants.STATE_CONSUMED ->{
                                status = "已消费"
                            }
                        }

                    }
                }
                Constants.STATE_REFUNDED -> {
                    status = "已退款"
                }
            }


            holder.status.text = status
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    fun updateData(data: ArrayList<Product>) {
        if (mDataList == null)
            mDataList = data
        else {
            for (tmp in data) {
                for (index in mDataList!!.indices) {
                    if (tmp.productId == mDataList!![index].productId)
                        mDataList!![index] = tmp
                }
            }

        }
        notifyDataSetChanged()
    }

    fun updateItemData(item: Product) {
        if (mDataList == null) {
            mDataList = ArrayList()
            mDataList!!.add(item)
        } else {
            for (index in mDataList!!.indices) {
                if (item.productId == mDataList!![index].productId)
                    mDataList!![index] = item
            }
        }
        notifyDataSetChanged()
    }

    class ViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val button = root.itemAction
        val title = root.itemTitle
        val price = root.itemPrice
        val status = root.itemStatus
        val clickArea = root.clickArea

    }

    interface OnItemClickListener {
        fun onItemClick(product: Product, position: Int, action: Int)
    }
}