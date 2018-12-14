package com.cabi.driver.service

import android.content.Context
import android.provider.Settings
import com.cabi.driver.data.CommonData
import com.cabi.driver.utils.SessionSave
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

open class NodeAuth() {

    fun getAuth(context: Context) {
        val data = JSONObject()
        try {
            data.put("device_id", Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID))
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val client = NodeServiceGenerator(context, false, SessionSave.getSession(CommonData.NODE_URL, context), 30).createService(CoreClient::class.java)
        val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data.toString())

        val coreResponse = client.nodeAuth(body)
        coreResponse.enqueue( object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                val data: String?

                try {
                    try {

                        data = response.body()!!.string()
                        val json = JSONObject(data)
                        if (data != null)
                            SessionSave.saveSession(CommonData.NODE_TOKEN, json.getString("token"), context)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    //                            updateLocation = "";
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}