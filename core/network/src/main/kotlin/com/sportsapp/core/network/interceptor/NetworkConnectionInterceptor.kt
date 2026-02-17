package com.sportsapp.core.network.interceptor

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import com.sportsapp.core.common.util.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(private val context: Context) : Interceptor {

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected()) {
            throw NoConnectivityException()
        }
        return chain.proceed(chain.request())
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}