package com.tjl.lazysdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.tjl.hms.iap.*
import com.tjl.lazysdk.adapter.SimpleIapAdapter
import kotlinx.android.synthetic.main.activity_hms_iap.*

class HmsIapActivity : AppCompatActivity(), SimpleIapAdapter.OnItemClickListener {


    private var consumeAdapter: SimpleIapAdapter? = null
    private var nonConsumeAdapter: SimpleIapAdapter? = null
    private var subscritionAdapter: SimpleIapAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hms_iap)
        val nonConsumeSkus = arrayListOf("noads")
        val consumeSkus = arrayListOf("Coin")
        val subscriptionSkus = arrayListOf("content.subscription")
        HmsIapRequestHelper.initIap(this, object : HmsIapListener {
            override fun onHmsIapSupported(bSupported: Boolean) {
                if (bSupported) {
                    showLoading()
                    HmsIapRequestHelper.queryProductsInfo(
                        this@HmsIapActivity,
                        nonConsumeSkus,
                        consumeSkus,
                        subscriptionSkus
                    )
                } else {
                    hideLoading()
                }
            }

            override fun onUpdateListDataByType(data: ArrayList<Product>, type: Int) {
                when (type) {
                    Constants.PRODUCT_TYPE_CONSUMABLE -> {
                        consumeAdapter?.updateData(data)
                    }
                    Constants.PRODUCT_TYPE_NON_CONSUMABLE -> {
                        nonConsumeAdapter?.updateData(data)
                    }
                    Constants.PRODUCT_TYPE_RENEWABLE_SUBSCRIPTION -> {
                        subscritionAdapter?.updateData(data)
                    }
                }
                hideLoading()
            }

            override fun onUpdateItemData(data: Product) {
                when (data.type) {
                    Constants.PRODUCT_TYPE_CONSUMABLE -> {
                        consumeAdapter?.updateItemData(data)
                    }
                    Constants.PRODUCT_TYPE_NON_CONSUMABLE -> {
                        nonConsumeAdapter?.updateItemData(data)
                    }
                    Constants.PRODUCT_TYPE_RENEWABLE_SUBSCRIPTION -> {
                        subscritionAdapter?.updateItemData(data)
                    }
                }
                hideLoading()
            }

        })
        initListViews()

        manageSubscribe.setOnClickListener {
            HmsIapRequestHelper.goManageSubscription(this)
        }
    }

    private fun showLoading() {
        runOnUiThread {
            if (loading.visibility == View.GONE)
                loading.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        runOnUiThread {
            loading.visibility = View.GONE
        }
    }

    private fun isProductCanPurchase(product: Product): Boolean {
        when (product.type) {
            Constants.PRODUCT_TYPE_CONSUMABLE -> {
                return product.consumeState != Constants.STATE_UNCONSUMED
            }
            Constants.PRODUCT_TYPE_RENEWABLE_SUBSCRIPTION,
            Constants.PRODUCT_TYPE_NON_CONSUMABLE -> {
                val inAppPurchaseData = ProductUitl.covertInAppPurchaseData(product)
                if (inAppPurchaseData != null)
                    return inAppPurchaseData.purchaseState != Constants.STATE_PURCHASED
            }

        }
        return true
    }

    override fun onItemClick(product: Product, position: Int, action: Int) {
        when (action) {
            Constants.ACTION_BUY -> {
                if(isProductCanPurchase(product)){
                    showLoading()
                    HmsIapRequestHelper.getBuyIntent(this, product)
                }else{
                    Toast.makeText(this@HmsIapActivity,
                        "当前商品已购买或可能需要消耗后才能再购买",Toast.LENGTH_SHORT).show()
                }

            }

            Constants.ACTION_CONSUME -> {
                HmsIapRequestHelper.consumePurchase(this, product)
            }
        }
    }

    private fun initListViews() {
        consumeAdapter = SimpleIapAdapter(this, null)
        consumeAdapter!!.setOnItemClickListener(this)
        consumeListView.layoutManager = LinearLayoutManager(this)
        consumeListView.adapter = consumeAdapter

        nonConsumeAdapter = SimpleIapAdapter(this, null)
        nonConsumeAdapter!!.setOnItemClickListener(this)
        unConsumeListView.layoutManager = LinearLayoutManager(this)
        unConsumeListView.adapter = nonConsumeAdapter

        subscritionAdapter = SimpleIapAdapter(this, null)
        subscritionAdapter!!.setOnItemClickListener(this)
        subscribeListView.layoutManager = LinearLayoutManager(this)
        subscribeListView.adapter = subscritionAdapter

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        HmsIapRequestHelper.onActivityResult(this, requestCode, resultCode, data)
        hideLoading()
    }
}
