package com.application.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.DisplayMetrics
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.LinearLayout.*
import android.widget.TextView
import com.application.R


class DialogAlert: DialogFragment() {

    var setTitle = ""
    var setDescription = ""
    private lateinit var titleView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var buttonsGroup: LinearLayout

    override fun onResume() {
        val params = dialog.window!!.attributes
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        val screenWidth = metrics.widthPixels
        val screenHeight = metrics.heightPixels
        params.width = screenWidth * 3/4
        params.height = screenHeight / 2
        dialog.window!!.attributes = params as WindowManager.LayoutParams
        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)

        val views = inflater.inflate(R.layout.layout_dialog_alert, container)
        views.apply {
            titleView = findViewById(R.id.DialogAlert_Title)
            descriptionView = findViewById(R.id.DialogAlert_Description)
            buttonsGroup = findViewById(R.id.DialogAlert_ButtonsGroup)
        }
        if (setTitle != "") titleView.text = setTitle
        if (setDescription != "")
            descriptionView.text = setDescription

        return views
    }

    companion object {
        const val BUTTON_OK = 0
        const val BUTTON_CANCEL = 1
        const val BUTTON_SIMPLE = 2
    }

    fun addButtonAction(text: String, aim: Int = BUTTON_SIMPLE, action: () -> Unit){
        val button = Button(context).apply {
            if (text.isNotEmpty())
                this.text = text
            setOnClickListener {
                action()
            }
            //typeface = Typeface.createFromAsset(context.assets, "Ubuntu-Light.ttf")
            textSize = 14f
            setBackgroundColor(
                when(aim){
                    BUTTON_OK -> Color.parseColor("#F9A825")
                    BUTTON_CANCEL -> Color.parseColor("#6295C3")
                    BUTTON_SIMPLE -> Color.parseColor("#ACACAC")
                    else -> Color.WHITE
                }
            )
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f)
        }
        buttonsGroup.addView(button)
    }
}