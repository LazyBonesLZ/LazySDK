package com.tjl.hms.iap

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.huawei.hms.support.api.entity.iap.OrderServiceNaming.skuDetail
import com.huawei.hms.support.api.entity.iap.SkuDetail

class Product {
    var price = ""
    var productId = ""
    var productName = ""
    var type = Constants.PRODUCT_TYPE_CONSUMABLE
    var inAppPurchaseDataJson: String? = null
    var inAppSignature: String? = null
    var consumeState = Constants.STATE_UNCONSUMED

}

object ProductUitl {
    fun createFromSkuDetail(skuDetail: SkuDetail): Product {
        val product = Product()
        product.type = skuDetail.priceType
        product.productId = skuDetail.productId
        product.productName = skuDetail.productName
        product.price = skuDetail.price
        return product
    }

//    fun  createFromInAppPurchaseData(json:String,type:Int):Product{
//        val product = Gson().fromJson(json,Product::class.java)
//        product.type = type
//        return product
//    }

    fun covertInAppPurchaseData(product: Product): InAppPurchaseData? {
        if (product.inAppPurchaseDataJson != null) {
            val inAppPurchaseData = Gson().fromJson(
                product.inAppPurchaseDataJson!!
                , InAppPurchaseData::class.java
            )
            return inAppPurchaseData
        }
        return null
    }



    fun covertInAppPurchaseData(json: String): InAppPurchaseData? {
        return Gson().fromJson(
            json
            , InAppPurchaseData::class.java
        )
    }
}