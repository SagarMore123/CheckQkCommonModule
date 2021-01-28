package com.astrika.checqk.discount.network.network_utils

import android.content.Context
import com.astrika.checqk.discount.network.network_utils.NetworkUrls.Companion.IS_FIRST_TIME_LOGIN_WITH_LOGIN_ID
import com.astrika.checqk.discount.network.network_utils.NetworkUrls.Companion.LOGIN_MASTERS
import com.astrika.checqk.discount.network.network_utils.NetworkUrls.Companion.LOGIN_WITH_LOGIN_ID
import com.astrika.checqk.discount.network.network_utils.NetworkUrls.Companion.REFRESH_TOKEN
import com.astrika.checqk.discount.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response


class AuthenticationInterceptorRefreshToken(
    private val context: Context?
) : Interceptor {

    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response? {

        var mainResponse: Response? = null
        //MAKE SYNCHRONIZED
//        synchronized(this) {
        val originalRequest = chain.request()

        var accessToken = Constants.getAccessToken(context = context) ?: ""
        if (originalRequest.url().toString().contains(LOGIN_MASTERS)
            || originalRequest.url().toString().contains(IS_FIRST_TIME_LOGIN_WITH_LOGIN_ID)
            || originalRequest.url().toString().contains(LOGIN_WITH_LOGIN_ID)
            || originalRequest.url().toString().contains(REFRESH_TOKEN)
        ) {
            accessToken = ""
        }
        val authenticationRequest = originalRequest.newBuilder()
            .addHeader("Authorization", accessToken).build()
//                .addHeader("appVersion", BuildConfig.VERSION_NAME).build()
        return chain.proceed(authenticationRequest)


    }


}