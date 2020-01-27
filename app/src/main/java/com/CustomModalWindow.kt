package com

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.LinearLayout.*
import android.widget.RelativeLayout.LayoutParams
import com.helper.setOnTextChange


class CustomModalWindow: DialogFragment() {

    var setTitle = ""
    var setDescription = ""
    var entered = ""
    var isSmall = true
    private lateinit var titleView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var buttonsGroup: LinearLayout
    private lateinit var editEntered: EditText
    private lateinit var componentsLayout: LinearLayout
    lateinit var action: CustomModalWindow.() -> Unit

    override fun onResume() {
        val params = dialog.window!!.attributes
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        val screenWidth = metrics.widthPixels
        val screenHeight = metrics.heightPixels
        val height =
            if (isSmall) screenHeight / 2
            else screenHeight * 4/5
        params.width = screenWidth * 5/6
        params.height = height
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
            componentsLayout = findViewById(R.id.CModal_LayoutGroup)
        }
        if (setTitle != "") titleView.text = setTitle
        if (setDescription != "")
            descriptionView.text = setDescription
        editEntered.text.append(entered)
        try {
            action(this)
        }
        catch (ex: Exception){
            Log.e("exModalWindow", ex.toString())
        }

        return views
    }

    fun addCheckBox(text: String, action: CheckBox.() -> Unit){
        CheckBox(ContextThemeWrapper(buttonsGroup.context, R.style.ItemThemeTextView)).apply {
            if (text.isNotEmpty())
                this.text = text
            setOnClickListener {
                action(this)
            }
            val dp10 = resources.getDimension(R.dimen.dp10).toInt()
            isAllCaps = false
            setBackgroundResource(R.drawable.item_style)
            elevation = 5f
            //typeface = Typeface.createFromAsset(context.assets, "Ubuntu-Light.ttf")
            textSize = 14f
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                dp10*4,
                1f
            )
            componentsLayout.visibility = View.VISIBLE
            componentsLayout.setBackgroundColor(Color.GRAY)
            componentsLayout.addView(this)
        }
    }

    fun addTextEdit(hint: String = ""){
        editEntered.hint = hint
        editEntered.visibility = View.VISIBLE
        editEntered.setOnTextChange {
            entered = text.toString()
        }
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
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, dp10*4, 1f
            ).apply {
                val dp5 = resources.getDimension(R.dimen.dp5).toInt()
                setMargins(dp5, dp10, dp5, dp10)
            }
        }
        buttonsGroup.addView(button)
    }
}