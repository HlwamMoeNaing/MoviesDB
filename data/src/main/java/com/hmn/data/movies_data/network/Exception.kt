package com.hmn.data.movies_data.network

import java.io.IOException

sealed class NetworkException: IOException() {
    class NoInternetException: NetworkException() {
        override val message: String
            get() = "You Are Not Connected to the Internet. Please check your WiFi or Data connection."
    }
}