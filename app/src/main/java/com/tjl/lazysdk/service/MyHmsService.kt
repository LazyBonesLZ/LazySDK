package com.tjl.lazysdk.service

import android.util.Log
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage

class MyHmsService : HmsMessageService() {
    companion object{
        const val TAG = "MyHmsService"
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        Log.i(TAG, "Token:$token")
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)
    }
}
