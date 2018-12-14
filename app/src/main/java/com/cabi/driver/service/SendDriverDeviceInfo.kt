package com.cabi.driver.service

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cabi.driver.data.apiData.DetailInfo
import com.cabi.driver.utils.DeviceUtils
import com.cabi.driver.utils.DriverUtils
import com.cabi.driver.utils.SessionSave
import okhttp3.ResponseBody

import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response

class SendDriverDeviceInfo {
    fun sendInfo(context: Context, unique: String) {

        val base_url = SessionSave.getSession("base_url", context)
        val uri = Uri.parse(base_url)
        val path = uri.path
        val client = ServiceGenerator(context, false, base_url.replace(path.toRegex(), "")).createService(CoreClient::class.java)

        val detail_infoCall = client.detail_infoCall(DetailInfo(DeviceUtils.getAllInfo(context), DriverUtils.driverInfo(context), unique), SessionSave.getSession("Lang", context))

        detail_infoCall.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful()) {
                        Log.v("FirebaseService", "" + response.message())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                t.printStackTrace()


            }

        })

    }
}