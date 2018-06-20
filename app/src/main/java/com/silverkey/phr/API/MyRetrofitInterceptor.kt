package com.silverkey.scops.API

import android.util.Log
import com.silverkey.phr.Helpers.Shared
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.RequestBody
import okio.Buffer
import java.io.IOException

/**
 * Created by Mohamed Taher on 12/12/2017.
 */

class MyRetrofitInterceptor : Interceptor {

    private val TAG = "API_CALL"

    override fun intercept(chain: Interceptor.Chain?): Response {

        var request = chain!!.request()
        val accept = "application/json"
        val content = "application/json"

        val httpUrl = request.url().newBuilder().build()
        request = request.newBuilder()
                .addHeader("Accept", accept)
                .addHeader("Content-Type", content)
                //.addHeader(Params.USER_SESSION, userSessionIdHeader)
                .url(httpUrl)
                .build()

        val x = bodyToString(request.body())
        Log.d(TAG, httpUrl.toString())
        Log.d(TAG, x)
        return chain.proceed(request)
    }

    fun bodyToString(request: RequestBody?): String {
        try {
            val copy = request
            val buffer = Buffer()
            if (copy != null)
                copy.writeTo(buffer)
            else
                return ""
            return buffer.readUtf8()
        } catch (e: IOException) {
            return "did not work"
        }
    }
}