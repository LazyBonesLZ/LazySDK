package com

import android.content.Context
import android.util.TypedValue
import android.widget.Toast
import android.R.attr.versionCode
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import androidx.core.content.pm.PackageInfoCompat
import androidx.core.net.ConnectivityManagerCompat


object Utils {
    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun dpToPx(context: Context,dp:Float):Int{
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, context.resources
                .displayMetrics).toInt()
    }

    fun pxToDp(context: Context,px:Int):Float{
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX, px.toFloat(), context.resources
                .displayMetrics)
    }

    fun getEMUILevel():Int{
        var emuiApiLevel = 0
        try {
            val cls = Class.forName("android.os.SystemProperties")
            val method = cls.getDeclaredMethod("get", *arrayOf<Class<*>>(String::class.java))
            emuiApiLevel = Integer.parseInt(
                method.invoke(
                    cls,
                    arrayOf<Any>("ro.build.hw_emui_api_level")
                ) as String
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return  emuiApiLevel

    }

    fun getHMSVersion(context: Context):Long{
        var pi: PackageInfo? = null
        val pm = context.packageManager
        var hwid = 0L
        try {
            pi = pm.getPackageInfo("com.huawei.hwid", 0)
            if (pi != null) {
                hwid = PackageInfoCompat.getLongVersionCode(pi)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return hwid
    }

    private val TAG = "Utils"
    /** get the network is available or is not  */
    fun netWorkState(context: Context?): Boolean {
        if (context == null) {
            return false
        }
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = ConnectivityManagerCompat.getNetworkInfoFromBroadcast(cm, Intent())
        return networkInfo != null && networkInfo.isConnected && networkInfo.isAvailable
    }
}