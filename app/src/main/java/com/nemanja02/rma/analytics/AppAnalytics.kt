package com.nemanja02.rma.analytics

class AppAnalytics {
    private val log = mutableListOf<String>()

    fun log(message: String) {
        log.add(message)
    }
}