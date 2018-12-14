package com.cabi.driver.service

import android.content.Context
import android.provider.Settings
import com.cabi.driver.utils.SessionSave
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback


open class CheckUrl() {

    fun update(context: Context, newUrl: String, testUrl : String, urlFor: String) {
        val data = JSONObject()
        try {
            data.put("device_id", Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID))
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val client = NodeServiceGenerator(context, false,testUrl, 30).createService(CoreClient::class.java)
        val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data.toString())

        val coreResponse = client.urlCheck(testUrl,body)
        coreResponse.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                val data: String?

                try {
                    try {

                        data = response.body()!!.string()
                        val json = JSONObject(data)
                        if (data != null && json.getString("status").equals("1")) {

                            SessionSave.saveSession(urlFor, newUrl, context)
                        }

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