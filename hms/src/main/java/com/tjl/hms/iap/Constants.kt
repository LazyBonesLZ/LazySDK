package com.tjl.hms.iap

object Constants {
    /** requestCode for pull up the pay page  */
    val REQ_CODE_BUYWITHPRICE = 4001

    /** requestCode for pull up the pmsPay page  */
    val REQ_CODE_BUY = 4002

    /** requestCode for pull up the login page or agreement page for getBuyIntentWithPrice interface */
    val REQ_CODE_CONTINUE = 4005

    /** requestCode for pull up the Subscription management page */
    val REQ_CODE_MANAGE_SUBS = 4009

    /** requestCode for pull up the login page for isBillingSupported interface  */
    val REQ_CODE_LOGIN = 2001

    /** the type of products  */
    val PRODUCT_TYPE_CONSUMABLE = 0
    val PRODUCT_TYPE_NON_CONSUMABLE = 1
    val PRODUCT_TYPE_RENEWABLE_SUBSCRIPTION = 2
    val PRODUCT_TYPE_RENEWABLE_NON_SUBSCRIPTION = 3

    /** the state of purchase*/
    val STATE_NON_BUY = -1  //未购买
    val STATE_PURCHASED = 0 //已完成支付
    val STATE_UNPURCHASE = 1 //未支付
    val STATE_REFUNDED = 2 //已退款

    val STATE_CONSUMED = 3
    val STATE_UNCONSUMED = 0



    val APPID = "100051813"

    val ACTION_BUY = 5000
    val ACTION_CONSUME= 5001
}
