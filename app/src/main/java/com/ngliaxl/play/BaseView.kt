/**
 *
 */
package com.ngliaxl.play

import android.content.Context

interface BaseView {

    fun context(): Context

    fun showLoading(message: String)

    fun hideLoading()

    fun onError(message: String)
}
