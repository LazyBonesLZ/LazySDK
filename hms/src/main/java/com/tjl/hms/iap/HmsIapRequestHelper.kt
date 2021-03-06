package com.tjl.hms.iap

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.widget.Toast

import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hms.support.api.client.Status
import com.huawei.hms.support.api.entity.iap.ConsumePurchaseReq
import com.huawei.hms.support.api.entity.iap.GetBuyIntentReq
import com.huawei.hms.support.api.entity.iap.GetBuyIntentWithPriceReq
import com.huawei.hms.support.api.entity.iap.GetPurchaseReq
import com.huawei.hms.support.api.entity.iap.OrderStatusCode
import com.huawei.hms.support.api.entity.iap.SkuDetailReq
import com.huawei.hms.support.api.iap.json.Iap
import com.huawei.hms.support.api.iap.json.IapApiException
import com.tjl.ads.R

import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import androidx.core.content.ContextCompat.startActivity


/**
 * the tool class of Iap interface
 */
object HmsIapRequestHelper {
    private val TAG = "HmsIapRequestHelper"
    private var hmsIapListener: HmsIapListener? = null
    private var currentProduct: Product? = null

    fun setHmsIapUIListener(hmsIapListener: HmsIapListener) {
        this.hmsIapListener = hmsIapListener
    }

    /**
     * Create a GetBuyIntentWithPriceReq request
     * @return GetBuyIntentReq
     */
    fun createGetBuyIntentWithPriceReq(): GetBuyIntentWithPriceReq {
        val request = GetBuyIntentWithPriceReq()
        request.productId = "NoPmsProduct8888"
        request.priceType = Constants.PRODUCT_TYPE_CONSUMABLE
        request.currency = "CNY"
        request.developerPayload = "12345678"
        request.sdkChannel = "1"
        request.productName = "product"
        request.amount = "0.01"
        request.serviceCatalog = "X38"
        request.country = "CN"
        return request
    }

    /**
     * Create a GetBuyIntentReq request
     * @param type In-app product type.
     * @param skuId ID of the in-app product to be paid.
     * The in-app product ID is the product ID you set during in-app product configuration in AppGallery Connect.
     * @return GetBuyIntentReq
     */
    fun createGetBuyIntentReq(type: Int, skuId: String): GetBuyIntentReq {
        val request = GetBuyIntentReq()
        request.productId = skuId
        request.priceType = type
        request.developerPayload = "test"
        return request
    }

    /**
     * Create a ConsumePurchaseReq request
     * @param purchaseData which is generated by the Huawei payment server during product payment and returned to the app through InAppPurchaseData.
     * The app transfers this parameter for the Huawei payment server to update the order status and then deliver the in-app product.
     * @return ConsumePurchaseReq
     */
    fun createConsumePurchaseReq(purchaseData: String): ConsumePurchaseReq {
        val consumePurchaseRequest = ConsumePurchaseReq()
        var purchaseToken = ""
        try {
            val jsonObject = JSONObject(purchaseData)
            purchaseToken = jsonObject.optString("purchaseToken")
        } catch (e: JSONException) {

        }

        consumePurchaseRequest.purchaseToken = purchaseToken
        return consumePurchaseRequest
    }

    /**
     * Create a GetPurchaseReq request
     * @param type In-app product type.
     * @return GetPurchaseReq
     */
    fun createGetPurchaseReq(type: Int): GetPurchaseReq {
        val getPurchaseRequest = GetPurchaseReq()
        getPurchaseRequest.priceType = type
        return getPurchaseRequest
    }

    /**
     * Create a SkuDetailReq request
     * @param type In-app product type.
     * @param skuList ID list of products to be queried. Each product ID must exist and be unique in the current app.
     * @return
     */
    fun createGetSkuDetailReq(type: Int, skuList: ArrayList<String>): SkuDetailReq {
        val skuDetailRequest = SkuDetailReq()
        skuDetailRequest.priceType = type
        skuDetailRequest.skuIds = skuList
        return skuDetailRequest
    }

    /**
     * Initiating an isbillingsupported request when entering the app
     * @param activity indicates the activity object that initiates a request.
     */
    fun initIap(
        activity: Activity, hmsIapListener: HmsIapListener
    ) {
        setHmsIapUIListener(hmsIapListener)
        isBillingSupported(activity)
    }

    fun queryProductsInfo(
        activity: Activity, nonConsumeSkus: ArrayList<String>?,
        consumeSkus: ArrayList<String>?,
        subscriptionSkus: ArrayList<String>?
    ) {
        if (nonConsumeSkus != null) {
            getSkuDetails(activity, nonConsumeSkus, Constants.PRODUCT_TYPE_NON_CONSUMABLE)
        }
        if (consumeSkus != null) {
            getSkuDetails(activity, consumeSkus, Constants.PRODUCT_TYPE_CONSUMABLE)
        }

        if (subscriptionSkus != null) {
            getSkuDetails(
                activity,
                subscriptionSkus,
                Constants.PRODUCT_TYPE_RENEWABLE_SUBSCRIPTION
            )
        }
    }

    /**
     * to check whether the country or region of the logged in HUAWEI ID is included in the countries or regions supported by HUAWEI IAP.
     * @param activity indicates the activity object that initiates a request.
     */
    fun isBillingSupported(activity: Activity) {
        Log.i(TAG, "call isBillingSupported")
        val mClient = Iap.getIapClient(activity)
        val task = mClient.isBillingSupported
        task.addOnSuccessListener {
            //            //query the products that the user has purchased and deliver products
//            getPurchase(activity, Constants.PRODUCT_TYPE_CONSUMABLE)
//            // query products and show product information to user
//            queryProductInfo(activity)
            hmsIapListener?.onHmsIapSupported(true)
        }.addOnFailureListener { e ->
            Toast.makeText(
                activity,
                activity.getString(R.string.external_error),
                Toast.LENGTH_SHORT
            ).show()
            Log.d(TAG, "isBillingSupported fail")
            if (e is IapApiException) {
                val returnCode = e.statusCode
                Log.d("isBillingSupported", "returnCode: $returnCode")
                // handle error scenarios
                if (returnCode == OrderStatusCode.ORDER_HWID_NOT_LOGIN) {
                    //to login
                    val status = e.status
                    startResolutionForResult(activity, status, Constants.REQ_CODE_LOGIN)
                } else if (returnCode == OrderStatusCode.ORDER_ACCOUNT_AREA_NOT_SUPPORTED) {
                    Toast.makeText(
                        activity,
                        activity.getString(R.string.unsupported_country),
                        Toast.LENGTH_SHORT
                    ).show()
                    hmsIapListener?.onHmsIapSupported(false)
                }
            } else {
                Log.e(TAG, e.message)

            }
        }
    }

    /**
     * Initiating a getSkuDetail request
     * @param context indicates the context object that initiates a request.
     */
    fun queryProductInfo(
        context: Context, nonConsumeSkus: ArrayList<String>?,
        consumeSkus: ArrayList<String>?,
        subscriptionSkus: ArrayList<String>?
    ) {
        if (nonConsumeSkus != null) {
            getSkuDetails(context, nonConsumeSkus, Constants.PRODUCT_TYPE_NON_CONSUMABLE)
        }
        if (consumeSkus != null) {
            getSkuDetails(context, consumeSkus, Constants.PRODUCT_TYPE_CONSUMABLE)
        }

        if (subscriptionSkus != null) {
            getSkuDetails(
                context,
                subscriptionSkus,
                Constants.PRODUCT_TYPE_RENEWABLE_SUBSCRIPTION
            )
        }
    }

    /**
     * obtain in-app product details configured in AppGallery Connect
     * @param context indicates the context object that initiates a request.
     * @param skuList ID list of products to be queried.
     * Each product ID must exist and be unique in the current app.
     * @param type In-app product type.
     */
    fun getSkuDetails(context: Context, skuList: ArrayList<String>, type: Int) {
        Log.i(TAG, "query product")
        val mClient = Iap.getIapClient(context)

        val task = mClient.getSkuDetail(createGetSkuDetailReq(type, skuList))
        task.addOnSuccessListener(OnSuccessListener { result ->
            Log.i(TAG, "getSkuDetail, success")
            if (result == null) {
                return@OnSuccessListener
            }
            if (result.skuList != null) {
                // to show product information
                val data = ArrayList<Product>()
                for (skuDetail in result.skuList) {
                    data.add(ProductUitl.createFromSkuDetail(skuDetail))
                }
                hmsIapListener?.onUpdateListDataByType(data, type)
                getPurchase(context, type, data)
            }
        }).addOnFailureListener { e ->
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            if (e is IapApiException) {
                val returnCode = e.statusCode
                Log.i(TAG, "getSkuDetail, returnCode: $returnCode")
                // handle error scenarios
                if (returnCode == OrderStatusCode.ORDER_HWID_NOT_LOGIN) {
                    // Unlogged scene
                    Log.i(TAG, "you are not logged in")
                } else {
                    // handle other error scenarios
                }
            } else {
                // Other external errors
                Log.e(TAG, e.message)
            }
        }
    }

    /**
     * call this API to set the in-app product price and complete payment,
     * instead of obtaining the price from the PMS.
     * @param activity indicates the activity object that initiates a request.
     */
    fun buyWithPrice(activity: Activity) {
        Log.i(TAG, "call buyWithPrice")
        val mClient = Iap.getIapClient(activity)
        val task =
            mClient.getBuyIntentWithPrice(HmsIapRequestHelper.createGetBuyIntentWithPriceReq())
        task.addOnSuccessListener(OnSuccessListener { result ->
            if (result == null) {
                return@OnSuccessListener
            }
            val status = result.status ?: return@OnSuccessListener
            if (status.resolution == null) {
                return@OnSuccessListener
            }
            if (!TextUtils.isEmpty(result.paymentSignature) && !TextUtils.isEmpty(result.paymentData)) {
                // verify signature,and pull up cashier page
                val isSuccess = CipherUtil.doCheck(
                    result.paymentData,
                    result.paymentSignature,
                    Key.publicKey
                )
                if (isSuccess) {
                    startResolutionForResult(activity, status, Constants.REQ_CODE_BUYWITHPRICE)
                } else {
                    // verify signature fail
                    Toast.makeText(
                        activity,
                        activity.getString(R.string.verify_signature_fail),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }).addOnFailureListener { e ->
            if (e is IapApiException) {
                val returnCode = e.statusCode
                // handle error scenarios
                if (OrderStatusCode.ORDER_HWID_NOT_LOGIN == returnCode || OrderStatusCode.ORDER_NOT_ACCEPT_AGREEMENT == returnCode) {
                    // Account not logged in or user not accepted the IAP agreement
                    val status = e.status
                    startResolutionForResult(activity, status, Constants.REQ_CODE_CONTINUE)
                } else {
                    // handle other error scenarios
                }
            } else {
                // Other external errors
                Log.e(TAG, e.message)
            }
        }
    }

    /**
     * create orders for in-app products in the PMS
     * @param activity indicates the activity object that initiates a request.
     * @param skuId ID list of products to be queried. Each product ID must exist and be unique in the current app.
     * @param type  In-app product type.
     */
    fun getBuyIntent(activity: Activity, product: Product) {
        Log.d(TAG, "call getBuyIntent")
        val mClient = Iap.getIapClient(activity)
        val task = mClient.getBuyIntent(createGetBuyIntentReq(product.type, product.productId))
        task.addOnSuccessListener(OnSuccessListener { result ->
            Log.d(TAG, "getBuyIntent, onSuccess")
            if (result == null) {
                Log.d(TAG, "result is null")
                return@OnSuccessListener
            }
            val status = result.status
            if (status == null) {
                Log.d(TAG, "status is null")
                return@OnSuccessListener
            }
//            if (product.type == Constants.PRODUCT_TYPE_CONSUMABLE) {
//                product.consumeState = Constants.STATE_UNCONSUMED
//            }
            currentProduct = product
            // you should pull up the page to complete the payment process
            startResolutionForResult(activity, status, Constants.REQ_CODE_BUY)
        }).addOnFailureListener { e ->
            Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
            if (e is IapApiException) {
                val returnCode = e.statusCode
                Log.d(TAG, "getBuyIntent, returnCode: $returnCode")
                // handle error scenarios
            } else {
                // Other external errors
                Log.e(TAG, e.message)
            }
        }
    }

    /**
     * to start an activity.
     * @param activity the activity to launch a new page.
     * @param status This parameter contains the pendingIntent object of the payment page.
     * @param reqCode Result code.
     */
    private fun startResolutionForResult(activity: Activity, status: Status?, reqCode: Int) {
        if (status == null) {
            Log.e(TAG, "status is null")
            return
        }
        if (status.hasResolution()) {
            try {
                status.startResolutionForResult(activity, reqCode)
            } catch (exp: IntentSender.SendIntentException) {
                Log.e(TAG, exp.message)
            }

        } else {
            Log.e(TAG, "intent is null")
        }
    }

    private fun getProductById(srcProducts: ArrayList<Product>, id: String): Product? {
        for (tmp in srcProducts) {
            if (tmp.productId == id)
                return tmp
        }
        return null
    }


    /**
     * query information about all subscribed in-app products, including consumables, non-consumables, and auto-renewable subscriptions.
     * If consumables are returned, the system needs to deliver them and calls the consumePurchase API to consume the products.
     * If non-consumables are returned, the in-app products do not need to be consumed.
     * If subscriptions are returned, all existing subscription relationships of the user under the app are returned.
     *
     *
     */
    fun getPurchase(context: Context, type: Int, products: ArrayList<Product>) {
        Log.i(TAG, "call getPurchase")
        val mClient = Iap.getIapClient(context)
        val task = mClient.getPurchases(HmsIapRequestHelper.createGetPurchaseReq(type))
        task.addOnSuccessListener(OnSuccessListener { result ->
            if (result == null) {
                Log.i(TAG, "result is null")
                return@OnSuccessListener
            }
            Log.i(TAG, "getPurchases, success")
//            if (result.inAppPurchaseDataList != null && type == Constants.PRODUCT_TYPE_CONSUMABLE) {
//                // Need to consume all the products with type 0 and deliver products
//                val inAppPurchaseDataList = result.inAppPurchaseDataList
//                val inAppSignature = result.inAppSignature
//                for (i in inAppPurchaseDataList.indices) {
//                    consumePurchase(context, inAppPurchaseDataList[i], inAppSignature[i])
//                }
//
//            }

            if (result.inAppPurchaseDataList != null) {
                // Need to consume all the products with type 0 and deliver products
                val inAppPurchaseDataList = result.inAppPurchaseDataList
                val inAppSignature = result.inAppSignature
                val data = ArrayList<Product>()
                for (i in inAppPurchaseDataList.indices) {
                    val tmpInAppPurchaseData =
                        ProductUitl.covertInAppPurchaseData(inAppPurchaseDataList[i])
//                    consumePurchase(context, inAppPurchaseDataList[i], inAppSignature[i])
                    val product = getProductById(products, tmpInAppPurchaseData!!.productId)
                        ?: continue
                    product.inAppPurchaseDataJson = inAppPurchaseDataList[i]
                    product.inAppSignature = inAppSignature[i]
                    data.add(product)
                }
                if (data.size > 0)
                    hmsIapListener?.onUpdateListDataByType(data, type)

            }
        }).addOnFailureListener { e ->
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            if (e is IapApiException) {
                val returnCode = e.statusCode
                Log.i(TAG, "getPurchase, returnCode: $returnCode")
            } else {
                // Other external errors
                Log.e(TAG, e.message)
            }
        }

    }

    /**
     * obtain the historical consumption information about a consumable in-app product or all subscription receipts of a subscription.
     * @param context indicates the context object that initiates a request.
     */
    fun getPurchaseHistory(context: Context) {
        Log.i(TAG, "call getPurchaseHistory")
        val mClient = Iap.getIapClient(context)
        val task =
            mClient.getPurchaseHistory(createGetPurchaseReq(Constants.PRODUCT_TYPE_CONSUMABLE))
        task.addOnSuccessListener {
            Toast.makeText(context, "getPurchaseHistory, success", Toast.LENGTH_SHORT).show()
            Log.i(TAG, "getPurchaseHistory, success")
        }.addOnFailureListener { e ->
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            if (e is IapApiException) {
                val returnCode = e.statusCode
                Log.i(TAG, "getPurchaseHistory, returnCode: $returnCode")
                // handle error scenarios
            } else {
                // Other external errors
                Log.e(TAG, e.message)
            }
        }
    }

    /**
     * consume all the unconsumed purchases with type 0
     * @param inAppPurchaseData JSON string that contains purchase order details.
     * @param inAppSignature signature of inAppPurchaseData
     */
    fun consumePurchase(context: Context, product: Product) {
        Log.i(TAG, "call consumePurchase")
        if (product.inAppPurchaseDataJson == null || product.inAppSignature == null)
            return

        val mClient = Iap.getIapClient(context)
        // verify signature of inAppPurchaseDataList
        val success = CipherUtil.doCheck(
            product.inAppPurchaseDataJson!!,
            product.inAppSignature!!,
            Key.publicKey
        )
        if (success) {
            Log.i(TAG, "verify success")
            val task =
                mClient.consumePurchase(createConsumePurchaseReq(product.inAppPurchaseDataJson!!))
            task.addOnSuccessListener {
                // Consume success
                Log.i(TAG, "consumePurchase success")
                product.consumeState = Constants.STATE_CONSUMED
                hmsIapListener?.onUpdateItemData(product)
                Toast.makeText(
                    context,
                    "消耗成功！",
                    Toast.LENGTH_SHORT
                ).show()

            }.addOnFailureListener { e ->
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                if (e is IapApiException) {
                    val returnCode = e.statusCode
                    Log.i(TAG, "consumePurchase fail,returnCode: $returnCode")
                } else {
                    // Other external errors
                    Log.e(TAG, e.message)
                    Toast.makeText(
                        context,
                        context.getString(R.string.external_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

    fun goManageSubscription(activity: Activity) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data =
            Uri.parse("pay://com.huawei.hwid.external/subscriptions?package=com.huawei.demo&appid=101238653")
        activity.startActivityForResult(intent,Constants.REQ_CODE_MANAGE_SUBS)
    }

    fun onActivityResult(context: Context, requestCode: Int, resultCode: Int, intent: Intent?) {
        when (requestCode) {
            Constants.REQ_CODE_BUY -> {
                if (intent != null && currentProduct != null) {
                    val buyResultInfo = Iap.getIapClient(context).getBuyResultInfoFromIntent(intent)
                    when {
                        buyResultInfo.returnCode == OrderStatusCode.ORDER_STATE_CANCEL -> {
                            // 用户取消
                            Toast.makeText(context,"用户取消",Toast.LENGTH_SHORT).show()
                        }
                        buyResultInfo.returnCode == OrderStatusCode.ORDER_ITEM_ALREADY_OWNED -> {
                            // 已购买过该商品
                            Toast.makeText(context,"已购买过该商品",Toast.LENGTH_SHORT).show()
                        }
                        buyResultInfo.returnCode == OrderStatusCode.ORDER_STATE_SUCCESS -> {
                            // 支付成功
                            if (currentProduct?.type == Constants.PRODUCT_TYPE_CONSUMABLE) {
                                currentProduct?.consumeState = Constants.STATE_UNCONSUMED
                            }

                            Toast.makeText(context,"支付成功",Toast.LENGTH_SHORT).show()
                            //val inAppPurchaseData = buyResultInfo.inAppPurchaseData
                            currentProduct!!.inAppSignature = buyResultInfo.inAppDataSignature
                            currentProduct!!.inAppPurchaseDataJson = buyResultInfo.inAppPurchaseData
                            hmsIapListener?.onUpdateItemData(currentProduct!!)
                        }
                        else -> {
                            // 其他异常
                            Toast.makeText(context,"购买异常",Toast.LENGTH_SHORT).show()
                        }
                    }
                    currentProduct = null //恢复标记

                }

            }
            Constants.REQ_CODE_LOGIN ->{
                var returnCode = 1
                if (intent != null) {
                    returnCode = intent.getIntExtra("returnCode", -1)
                }
                when (returnCode) {
                    OrderStatusCode.ORDER_STATE_SUCCESS -> hmsIapListener?.onHmsIapSupported(true)
                    OrderStatusCode.ORDER_ACCOUNT_AREA_NOT_SUPPORTED -> {
                        Toast.makeText(
                            context,
                            "This is unavailable in your country/region.",
                            Toast.LENGTH_SHORT
                        ).show()
                        hmsIapListener?.onHmsIapSupported(false)
                    }
                    else -> Log.i(TAG, "user cancel login")
                }

            }
            Constants.REQ_CODE_MANAGE_SUBS ->{
                hmsIapListener?.onHmsIapSupported(true)
            }
        }

    }
}
