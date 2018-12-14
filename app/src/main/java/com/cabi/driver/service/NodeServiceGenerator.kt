package com.cabi.driver.service

import android.content.Context
import android.text.TextUtils
import com.cabi.driver.BuildConfig
import com.cabi.driver.data.CommonData
import com.cabi.driver.utils.SessionSave
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit

class NodeServiceGenerator {
    private val httpClient: OkHttpClient.Builder
    private val builder: Retrofit.Builder

    constructor(c: Context, dont_encode: Boolean, base_url: String,timeOut:Long) {
        val logging = HttpLoggingInterceptor()
        val b = RequestInterceptor(c)
        // set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient = OkHttpClient.Builder().connectTimeout(timeOut, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)
        val d = DecryptInterceptor(c)
        httpClient.interceptors().add(b)
        httpClient.addInterceptor(d)
        if (BuildConfig.DEBUG)
            httpClient.interceptors().add(logging)
        builder = Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
    }

    inner class RequestInterceptor internal constructor(internal var c: Context) : Interceptor {


        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val originalRequest = chain.request()
            var builder: Request.Builder = originalRequest.newBuilder()
            if (originalRequest.method().equals("POST", ignoreCase = true)) {
                builder = originalRequest.newBuilder()
                        .method(originalRequest.method(), originalRequest.body())
            }
            builder.addHeader("domain", SessionSave.getSession(CommonData.NODE_DOMAIN,c))
            builder.addHeader("Authorization","FNpfuspyEAzhjfoh2ONpWK0rsnClVL6OCaasqDQtWdI=")
            builder.addHeader("Content-type", "application/json")
            builder.addHeader("version", "${BuildConfig.VERSION_CODE}")
            builder.addHeader("token", SessionSave.getSession(CommonData.NODE_TOKEN, c))
            val originalHttpUrl = originalRequest.url()
            val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("stable_version", "" + BuildConfig.VERSION_CODE)
                    .addQueryParameter("tmrelease", "08_10_2018")
                    .addQueryParameter("id", SessionSave.getSession("Id", c))
                    .addQueryParameter("lang", SessionSave.getSession("Lang", c))
                    .build()
            builder.url(url)

            return chain.proceed(builder.build())
        }

    }


    inner class DecryptInterceptor internal constructor(internal var c: Context) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {

            val response = chain.proceed(chain.request())
            if (response.isSuccessful()) {
                val newResponse = response.newBuilder()
                var contentType = response.header("Content-Type")
                if (TextUtils.isEmpty(contentType)) contentType = "application/json"
                val data = response.body()
                if (data != null) {
                    val cryptedStream = data.byteStream()
                    var decrypted: String? = null
                    val result = ByteArrayOutputStream()
                    val buffer = ByteArray(1024)
                    var length: Int = cryptedStream.read(buffer)
                    while (length != -1) {
                        result.write(buffer, 0, length)
                        length = cryptedStream.read(buffer)
                    }

                    try {
//                        if (!result.toString("UTF-8").isEmpty())
//                            decrypted = AA().dd(result.toString("UTF-8"))
                        decrypted = result.toString("UTF-8")

                        newResponse.body(ResponseBody.create(MediaType.parse(contentType), decrypted))
                        val ress = newResponse.build()
                        if (CheckStatus(JSONObject(decrypted), c).isNormal())
                            return ress
                        else
                            return response
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                }

            }
            return response
        }


    }

    fun <S> createService(serviceClass: Class<S>): S {
        val retrofit = builder.client(httpClient.build()).build()
        return retrofit.create(serviceClass)
    }
}