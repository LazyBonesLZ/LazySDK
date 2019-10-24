package com.tjl.lazysdk

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.recyclerview.widget.LinearLayoutManager
import com.Utils
import com.huawei.hms.ads.identifier.AdvertisingIdClient
import com.huawei.hms.ads.installreferrer.api.InstallReferrerClient
import com.huawei.hms.ads.installreferrer.api.InstallReferrerStateListener
import com.huawei.hms.push.HmsMessaging
import com.tjl.lazysdk.adapter.SimpleRecycleViewAdapter
import kotlinx.android.synthetic.main.activity_hms.*
import java.io.IOException
import com.huawei.hms.aaid.HmsInstanceId




class HMSActivity : AppCompatActivity(),SimpleRecycleViewAdapter.OnItemClickListener {

    private var bSubscribed = false
    companion object {
        const val TAG = "HMSActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hms)
        val data = arrayListOf(
            "HMS Ads-Kit: OAID",
            "HMS Location",
            "HMS PUSH",
            "HMS IAP",
            "HMS Analytics"
        )
        val adapter = SimpleRecycleViewAdapter(this, data)
        adapter.setOnItemClickListener(this)
        titleListView.layoutManager = LinearLayoutManager(this)
        titleListView.adapter = adapter

        hmsConnect()

        getToken()

    }

    override fun onItemClick(title: String, position: Int) {
            when (title) {
                "HMS Ads-Kit: OAID" -> {
                    showOAID()
                }

                "HMS Location" -> {
                    requesLoctionPermission()
                }
                "HMS PUSH" -> {
                    if (bSubscribed){
                        unSubscribe()
                    }else{
                        subscribe()
                    }


                }
                "HMS IAP" -> {

                }
                "HMS Analytics" -> {

                }
            }

    }

    private fun showOAID() {
        Thread(Runnable {
            try {
                val info = AdvertisingIdClient.getAdvertisingIdInfo(this)
                if (null != info) {
                    val msg = "info id=" + info.id +
                            ", isLimitAdTrackingEnabled=" + info.isLimitAdTrackingEnabled

                    Log.i(TAG, msg)
                    runOnUiThread {
                        Utils.showToast(this, msg)
                    }
                }
            } catch (e: IOException) {
                val msg = "getAdvertisingIdInfo Exception: $e"
                Log.i(TAG, msg)
                runOnUiThread {
                    Utils.showToast(this, msg)
                }
            }

        }).start()
    }

    private fun requesLoctionPermission() {
        // check location permisiion
        if (SDK_INT <= Build.VERSION_CODES.P) {
            Log.i(TAG, "sdk < 28 Q")
            if (ActivityCompat.checkSelfPermission(
                    this,
                    ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf<String>(
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION
                )
                requestPermissions(this, strings, 1)
            } else {
                goHMSLocationActivity()
            }
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    this,
                    ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    this,
                    "android.permission.ACCESS_BACKGROUND_LOCATION"
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION,
                    "android.permission.ACCESS_BACKGROUND_LOCATION"
                )
                requestPermissions(this, strings, 2)
            } else {
                goHMSLocationActivity()
            }
        }
    }

    private fun goHMSLocationActivity() {
        startActivity(Intent(this, HMSLocationActivity::class.java))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.size > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                goHMSLocationActivity()
                Log.i(TAG, "onRequestPermissionsResult: apply LOCATION PERMISSION successful");
            } else {
                Log.i(TAG, "onRequestPermissionsResult: apply LOCATION PERMISSSION  failed");
            }
        }

        if (requestCode == 2) {
            if (grantResults.size > 2 && grantResults[2] == PackageManager.PERMISSION_GRANTED
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                goHMSLocationActivity()
                Log.i(
                    TAG,
                    "onRequestPermissionsResult: apply ACCESS_BACKGROUND_LOCATION successful"
                );
            } else {
                Log.i(TAG, "onRequestPermissionsResult: apply ACCESS_BACKGROUND_LOCATION  failed");
            }
        }
    }
    private fun hmsConnect() {
        Thread(Runnable {
            val hmsReferrerClient = InstallReferrerClient.newBuilder(this).build()
            hmsReferrerClient.startConnection(object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    when (responseCode) {
                        InstallReferrerClient.InstallReferrerResponse.OK ->
                            Log.i(CustomButtonActivity.TAG, "connect ads kit ok")
                        InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED ->
                            Log.i(CustomButtonActivity.TAG, "FEATURE_NOT_SUPPORTED")
                        InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE ->
                            Log.i(CustomButtonActivity.TAG, "SERVICE_UNAVAILABLE")
                        else->
                            Log.i(CustomButtonActivity.TAG, "responseCode: $responseCode")
                    }
                }

                override fun onInstallReferrerServiceDisconnected() {

                }

            })
        }).start()
    }

    private fun getToken(){
        object : Thread() {
            override fun run() {
                try {
                    val getToken =
                        HmsInstanceId.getInstance(this@HMSActivity).getToken("101238653", "HCM")
                    Log.e(TAG, "getToken:$getToken")
                } catch (e: Exception) {
                    Log.e(TAG, "getToken failed.", e)
                }

            }
        }.start()
    }

    private fun subscribe(){
        try {
            HmsMessaging.getInstance(this).subscribe("HMC").addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    runOnUiThread{
                        Utils.showToast(this@HMSActivity,"subscribe Complete")
                    }
                    Log.i(TAG, "subscribe Complete")
                    bSubscribed = true
                } else {
                    bSubscribed = false
                    runOnUiThread{
                        Utils.showToast(this@HMSActivity,"subscribe failed: ret=" + task.exception.message)
                    }
                    Log.e(TAG, "subscribe failed: ret=" + task.exception.message)
                }
            }
        } catch (e: Exception) {
            runOnUiThread{
                Utils.showToast(this@HMSActivity,"subscribe failed: exception=" + e.message)
            }
            Log.e(TAG, "subscribe failed: exception=" + e.message)
        }

    }

    private fun unSubscribe(){
        try {
            HmsMessaging.getInstance(this).unsubscribe("HMC").addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(TAG, "unsubscribe Complete")
                    bSubscribed = false
                } else {
                    Log.e(TAG, "unsubscribe failed: ret=" + task.exception.message)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "unsubscribe failed: exception=" + e.message)
        }

    }
}
