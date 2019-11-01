package com.tjl.lazysdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.hms.analytics.HiAnalyticsTools
import kotlinx.android.synthetic.main.activity_hms_analytics.*
import java.text.SimpleDateFormat
import java.util.*

class HmsAnalyticsActivity : AppCompatActivity() {
    private lateinit var hmsAnalyticsInstance: HiAnalyticsInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hms_analytics)
        initHMSAnalytics()
        logEvent.setOnClickListener {
            val time = SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(Date())
            val data = Bundle()
            data.putString("time",time)
            logEvent("CustomEvents",data)

            val tmpMsg = "CustomEvents: Usr Time=$time"
            status.text = tmpMsg
        }

        setUserProperty.setOnClickListener {
            val time = SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(Date())
            val tmpMsg = "UserProper: Usr Time=$time"
            status.text = tmpMsg
            setUserProperty("UserProper", "Usr Time:$time")
        }
    }

   private fun initHMSAnalytics() {
        HiAnalyticsTools.enableLog()
       hmsAnalyticsInstance = HiAnalytics.getInstance(this)
       hmsAnalyticsInstance.setAnalyticsCollectionEnabled(true)
       hmsAnalyticsInstance.setAutoCollectionEnabled(true)
    }

    private fun logEvent(eventName:String,data:Bundle){
        hmsAnalyticsInstance.logEvent(eventName,data)
    }

    private fun setUserProperty(propertyName:String,proprety:String){
        hmsAnalyticsInstance.setUserProperty(propertyName,proprety)
    }

    //adb shell setprop debug.huawei.hms.analytics.app <package_name>
    //adb shell setprop debug.huawei.hms.analytics.app .none.
}
