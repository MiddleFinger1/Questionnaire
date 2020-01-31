package com.ui.custom

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
import com.R
import com.setOnTextChange


class CustomModalWindow: DialogFragment() {

    var setTitle = ""
    var setDescription = ""
    var setDpi = 0
    var entered = ""
    var isSmall = true
    private lateinit var titleView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var editEntered: EditText
    private lateinit var componentsLayout: LinearLayout
    private lateinit var buttonOk: Button
    private lateinit var buttonCancel: Button
    lateinit var action: CustomModalWindow.() -> Unit

    companion object {
        const val BUTTON_OK = "buttonOk"
        const val BUTTON_CANCEL = "buttonCancel"
        const val BUTTON_OTHER = "buttonOther"
    }

    override fun onResume() {
        val params = dialog.window!!.attributes
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        val screenWidth = metrics.widthPixels
        val screenHeight = metrics.heightPixels
        val height =
            if (setDpi != 0) setDpi
            else if (isSmall && setDpi == 0)screenHeight * 4/5
            else screenHeight / 2
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
            editEntered = findViewById(R.id.CModal_EditEntered)
            componentsLayout = findViewById(R.id.CModal_LayoutGroup)
            buttonOk = findViewById(R.id.CModal_ButtonActive)
            buttonCancel = findViewById(R.id.CModal_ButtonNegative)
        }
        if (setTitle != "") titleView.text = setTitle
        if (setDescription != "") {
            descriptionView.visibility = View.VISIBLE
            descriptionView.text = setDescription
        }
        editEntered.text.append(entered)
        try {
            action(this)
        }
        catch (ex: Exception){
            Log.e("exModalWindow", ex.toString())
        }
        return views
    }

    fun addCheckBox(text: String, state: Boolean = false, action: CheckBox.() -> Unit){
        CheckBox(ContextThemeWrapper(componentsLayout.context, R.style.ItemThemeTextView)).apply {
            if (text.isNotEmpty())
                this.text = text
            setOnClickListener {
                action(this)
            }
            val dp10 = resources.getDimension(R.dimen.dp10).toInt()
            isAllCaps = false
            isChecked = state
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

    fun addRadioGroup(vararg buttons: String, action: (RadioGroup, Int) -> Unit){
        val radioGroup = RadioGroup(componentsLayout.context)
            radioGroup.setOnCheckedChangeListener{ group, i ->
                action(group, i)
            }
        for (item in buttons){
            radioGroup.addView(RadioButton(ContextThemeWrapper(context, R.style.ItemThemeTextView)).apply {
                text = item
                setTextColor(Color.BLACK)
                setBackgroundResource(R.drawable.item_style)
            })
        }
        componentsLayout.addView(radioGroup)
    }

    fun addButtonAction(text: String, buttonType: String, action: () -> Unit){
        val button: Button? = when(buttonType){
            BUTTON_OK -> buttonOk
            BUTTON_CANCEL -> buttonCancel
            BUTTON_OTHER ->
                Button(ContextThemeWrapper(context, R.style.ItemThemeTextView))
            else -> null
        }
        if (button != null){
            button.visibility = View.VISIBLE
            button.setBackgroundResource(R.drawable.item_style)
            button.isAllCaps = false
            button.text = text
            button.setOnClickListener {
                action()
            }
            if (buttonType == BUTTON_OTHER){
                button.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f).apply {
                    val dp10 = resources.getDimension(R.dimen.dp10)
                    bottomMargin = dp10.toInt()
                }
                componentsLayout.addView(button)
            }
        }
    }
}