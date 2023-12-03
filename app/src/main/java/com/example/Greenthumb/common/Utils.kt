package com.example.greenthumb.common

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.TextUtils
import android.util.Patterns


fun navigateActivity(context: Context, cls: Class<*>?) {
    val intent = Intent(context, cls)
    context.startActivity(intent)
}


fun isValidEmail(email: String): Boolean {
    if (!TextUtils.isEmpty(email)) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    return false
}

fun isInternetConnected(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        return networkInfo.isConnected
    }
}