/**
 *
 */
package com.ngliaxl.play

interface BasePresenter {

    fun resume()

    fun pause()

    fun destroy()

    companion object {
        const val NETWORK_ERROR = "network error"
    }

}
