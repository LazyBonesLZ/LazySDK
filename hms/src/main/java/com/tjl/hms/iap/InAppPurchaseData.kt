package com.tjl.hms.iap

class InAppPurchaseData {
    var applicationId = 0L
    var autoRenewing = false
    var orderId = ""
    var packageName = ""
    var productId = ""
    var productName = ""
    var purchaseTime = 0L
    /**订单交易状态。
    ● 0：已完成支付。
    ● 1：未支付。
    ● 2：已退款。*/
    var purchaseState = -1
    var developerPayload = ""
    var purchaseToken =""
    /**购买类型。
    ● 0：沙箱环境。
    ● 1：促销，暂不支持。
    正式购买不会返回该参数*/
    var purchaseType = -1
    var currency = ""
    var price = 0L
    var country = ""

    /** below properties relate to subscription*/
    var lastOrderId = ""
    var productGroup = ""
//    var purchaseTime = 0L
    var oriPurchaseTime = 0L
    var subscriptionId = ""
    var quantity = 0
    var daysLasted = 0L
    var numOfPeriods = 0L
    var numOfDiscount = 0L
    var expirationDate = 0L
    /**对于已经过期的订阅，表示过期原因，取值包括：
    ● 1：用户取消
    ● 2：商品不可用
    ● 3：用户签约信息异常
    ● 4：Billing错误
    ● 5：用户未同意涨价
    ● 6：未知错误
    同时有多个异常时，优先级为：1 > 2 > 3…*/
    var expirationIntent = -1
    /**
     * 一个过期的订阅，系统是否仍然在尝试自动完成续期处理。
    ● 0：终止尝试
    ● 1：仍在尝试完成续期
     */
    var retryFlag = -1
    /**
     * 是否处于促销价续期周期内。
    ● 1：是
    ● 0：否
     */
    var introductoryFlag = -1
    /**
     * 是否处于免费试用周期内。
    ● 1：是
    ● 0：否
     */
    var trialFlag = -1
    var cancelTime = 0L
    /**取消原因。
    ● 2：顾客升级、跨级等。
    ● 1：顾客因为在App内遇到了问题而取消了订阅。
    ● 0：其他原因取消，比如顾客错误地订阅了商品。
     */
    var cancelReason = -1
    var appInfo = ""
    /**用户是否已经关闭订阅上的通知，
    ● 1：是
    ● 0：否
    关闭状态下，订阅相关的通知均不会发送给用户。*/
    var notifyClosed = -1
    /**续期状态。
    ● 1：当前周期到期时自动续期。
    ● 0：用户停止了续期。
    仅针对自动续期订阅，对有效和过期的订阅均有效，并不代表顾客的订阅状态。通常，取值为0时，应用可以给顾客提供其他的订阅选项，例如推荐一个同组更低级别的商品。该值为0通常代表着顾客主动取消了该订阅。*/
    var renewStatus = -1
    /**商品提价时的用户意见。
    ● 1：用户已经同意提价。
    ● 0：用户未采取动作，超期后订阅失效。*/
    var priceConsentStatus = -1
    var renewPrice = 0L
    var subIsvalid = false
    var cancelledSubKeepDays = 0





}