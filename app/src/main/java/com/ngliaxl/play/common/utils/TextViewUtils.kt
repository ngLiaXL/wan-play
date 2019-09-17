package com.ngliaxl.play.common.utils

import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created by ngliaxl on 2018/6/15.
 */
object TextViewUtils {

    fun generateText(ctx: Context, width: Int): TextView {
        val params = LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT)
        params.gravity = Gravity.CENTER
        val textView = TextView(ctx)
        textView.layoutParams = params
        return textView
    }


}
