package com.yunushan.letgocasestudy.domain.interceptor

import com.yunushan.letgocasestudy.BuildConfig
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class HttpInterceptor : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        return response.request.newBuilder().header("Authorization", "Bearer " + BuildConfig.APIKEY)
            .build()
    }
}