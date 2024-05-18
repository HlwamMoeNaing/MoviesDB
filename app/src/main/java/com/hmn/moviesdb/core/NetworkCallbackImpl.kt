package com.hmn.moviesdb.core

import android.net.ConnectivityManager
import android.net.Network

class NetworkCallbackImpl(private val viewModel: BaseViewModel) :
    ConnectivityManager.NetworkCallback() {

    override fun onAvailable(network: Network) {
        viewModel.onNetworkChanged(true)
    }

    override fun onLost(network: Network) {
        viewModel.onNetworkChanged(false)
    }
}