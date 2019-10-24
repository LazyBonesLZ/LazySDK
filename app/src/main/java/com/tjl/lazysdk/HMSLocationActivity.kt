package com.tjl.lazysdk

import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import com.huawei.hms.location.FusedLocationProviderClient
import com.huawei.hms.location.LocationCallback
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.SettingsClient
import com.huawei.hms.location.LocationServices
import com.huawei.hms.location.LocationAvailability
import com.huawei.hms.location.LocationResult
import com.huawei.hms.common.ResolvableApiException
import com.huawei.hms.location.LocationSettingsStatusCodes
import com.huawei.hms.common.ApiException
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import android.os.Looper.getMainLooper
import android.util.Log
import com.huawei.hms.location.LocationSettingsResponse
import com.huawei.hms.location.LocationSettingsRequest
import kotlinx.android.synthetic.main.activity_hmslocation.*
import java.text.SimpleDateFormat
import java.util.*


class HMSLocationActivity : AppCompatActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var settingsClient: SettingsClient
    private lateinit var mLocationCallback: LocationCallback
    private lateinit var mLocationRequest: LocationRequest

    companion object {
        const val TAG = "HMSLocationActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hmslocation)
        initLocationService()
        updateLocation.setOnClickListener {
            updateStatus("")
            requestLocationUpdatesWithCallback()
        }
        stopUpdate.setOnClickListener {
            updateStatus("")
            removeLocationUpdatesWithCallback()
        }
    }

    private fun updateStatus(msg:String){
        Log.i(TAG, msg)
        runOnUiThread {
            val tmpMsg = SimpleDateFormat("HH:mm:ss").format(Date()) + " "+ msg
            locationStatus.text = tmpMsg
        }
    }

    private fun initLocationService() {
        // create fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // create settingsClient
        settingsClient = LocationServices.getSettingsClient(this)
        mLocationRequest = LocationRequest()
        // Set the interval for location updates, in milliseconds.
        mLocationRequest.interval = 10000
        // set the priority of the request
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null) {
                    val locations = locationResult!!.locations
                    if (!locations.isEmpty()) {
                        for (location in locations) {

                            val msg = "onLocationResult location:\nLongitude = ${location.longitude}\nLatitude = ${location.latitude}\nAccuracy = ${location.accuracy}"
                            updateStatus(msg)
                        }
                    }
                }
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                if (locationAvailability != null) {
                    val flag = locationAvailability!!.isLocationAvailable
                    Log.i(
                        TAG,
                        "onLocationAvailability isLocationAvailable:$flag"
                    )
                }
            }
        }
    }

    /**
     * function：Requests location updates with a callback on the specified Looper thread.
     * first：use SettingsClient object to call checkLocationSettings(LocationSettingsRequest locationSettingsRequest) method to check device settings.
     * second： use  FusedLocationProviderClient object to call requestLocationUpdates (LocationRequest request, LocationCallback callback, Looper looper) method.
     */
    private fun requestLocationUpdatesWithCallback() {
        try {
            val builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(mLocationRequest)
            val locationSettingsRequest = builder.build()
            // check devices settings before request location updates.
            settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener {
                    //                    Log.i(TAG, "check location settings success")
                    // request location updates
                    fusedLocationProviderClient
                        .requestLocationUpdates(
                            mLocationRequest,
                            mLocationCallback,
                            Looper.getMainLooper()
                        )
                        .addOnSuccessListener {
                            Log.i(
                                TAG,
                                "requestLocationUpdatesWithCallback onSuccess"
                            )
                        }
                        .addOnFailureListener { e ->
                            Log.e(
                                TAG,
                                "requestLocationUpdatesWithCallback onFailure:" + e.message
                            )
                        }
                }
                .addOnFailureListener { e ->
                    Log.e(
                        TAG,
                        "checkLocationSetting onFailure:" + e.message
                    )
                    val statusCode = (e as ApiException).statusCode
                    when (statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(
                                this@HMSLocationActivity,
                                0
                            )
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.e(TAG, "PendingIntent unable to execute request.")
                        }

                    }
                }
        } catch (e: Exception) {
            Log.e(
                TAG,
                "requestLocationUpdatesWithCallback exception:" + e.message
            )
        }

    }

    /**
     * remove the request with callback
     */
    private fun removeLocationUpdatesWithCallback() {
        try {
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
                .addOnSuccessListener {
                    Log.i(
                        TAG,
                        "removeLocationUpdatesWithCallback onSuccess"
                    )
                }
                .addOnFailureListener { e ->
                                       Log.e(
                        TAG,
                        "removeLocationUpdatesWithCallback onFailure:" + e.message
                    )
                }
        } catch (e: Exception) {
           Log.e(
               TAG,
               "removeLocationUpdatesWithCallback exception:" + e.message
           )
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        removeLocationUpdatesWithCallback()
    }


}
