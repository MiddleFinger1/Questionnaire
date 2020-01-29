package com.helper

import android.content.Context
import android.graphics.Color
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import android.widget.EditText
import com.R


fun createEditTextView(context: Context, layoutParams: ViewGroup.MarginLayoutParams) =
    EditText(ContextThemeWrapper(context, R.style.ItemThemeTextView)).apply {
        setTextColor(Color.BLACK)
        highlightColor = Color.BLACK
        setBackgroundResource(R.drawable.item_style)
        this.layoutParams = layoutParams.apply {
            val dp10 = resources.getDimension(R.dimen.dp10).toInt()
            val dp5 = resources.getDimension(R.dimen.dp5).toInt()
            bottomMargin = dp5
            marginStart = dp10
            marginEnd = dp10
            topMargin = dp5
        }
    }