package com

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView


class CustomModalWindow: DialogFragment() {

    var setTitle = ""
    var setDescription = ""
    var entered = ""
    private lateinit var titleView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var buttonsGroup: LinearLayout
    private lateinit var editEntered: EditText
    lateinit var action: CustomModalWindow.() -> Unit

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

        val views = inflater.inflate(R.layout.custom_modal_window, container)
        views.apply {
            titleView = findViewById(R.id.CModal_Title)
            descriptionView = findViewById(R.id.CModal_Description)
            buttonsGroup = findViewById(R.id.CModal_ButtonsGroup)
            editEntered = findViewById(R.id.CModal_EditEntered)
        }
        if (setTitle != "") titleView.text = setTitle
        if (setDescription != "")
            descriptionView.text = setDescription

        try {
            action(this)
        }
        catch (ex: Exception){
            Log.e("exModalWindow", ex.toString())
        }

        return views
    }

    fun addTextEdit(hint: String = ""){
        editEntered.hint = hint
        editEntered.visibility = View.VISIBLE
        editEntered.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                entered = editEntered.text.toString()
            }
        })
    }

    fun addButtonAction(text: String, action: () -> Unit){
        val button = Button(ContextThemeWrapper(buttonsGroup.context, R.style.ItemThemeTextView)).apply {
            if (text.isNotEmpty())
                this.text = text
            setOnClickListener {
                action()
            }
            val dp10 = resources.getDimension(R.dimen.dp10).toInt()
            isAllCaps = false
            setBackgroundResource(R.drawable.item_style)
            elevation = 5f
            //typeface = Typeface.createFromAsset(context.assets, "Ubuntu-Light.ttf")
            textSize = 14f
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp10*4,
                1f
            ).apply {
                val dp5 = resources.getDimension(R.dimen.dp5).toInt()
                gravity = Gravity.BOTTOM
                setMargins(dp5, dp10, dp5, dp10)
            }
        }
        buttonsGroup.addView(button)
    }
}