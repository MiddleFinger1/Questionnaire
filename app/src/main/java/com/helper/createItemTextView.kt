package com.helper

import android.content.Context
import android.graphics.Color
import android.view.ContextThemeWrapper
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import com.R


fun createItemTextView(context: Context, layoutParams: MarginLayoutParams) =
    TextView(ContextThemeWrapper(context, R.style.ItemThemeTextView)).apply {
        setTextColor(Color.BLACK)
        highlightColor = Color.BLACK
        this.layoutParams = layoutParams.apply {
            val dp10 = resources.getDimension(R.dimen.dp10).toInt()
            val dp5 = resources.getDimension(R.dimen.dp5).toInt()
            bottomMargin = dp5
            marginStart = dp10
            marginEnd = dp10
            topMargin = dp5
        }
    }