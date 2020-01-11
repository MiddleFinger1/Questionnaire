package com.questionnaire.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.DisplayMetrics
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.LinearLayout.*
import android.widget.TextView
import android.widget.Toast
import com.application.R


class DialogAlert: DialogFragment() {

    lateinit var contextQuestionnaire: PresentativeQuestionnaire
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

        // не получается универсализировать для определенных нужд
        titleView.text = "Завершить тестирование!"
        descriptionView.text = "Вы хотите завершить анкету и узнать свой результат?"
        //setTitle = "Завершить тестирование!"
        //setDescription = "Вы хотите завершить анкету и узнать свой результат?"
        addButtonAction("Cancel") {
            dismiss()
        }
        addButtonAction("Ok"){
            val fragment = AnalyticsFragment()
            fragment.contextQuestionnaire = contextQuestionnaire
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.MainQuestionnaireLayout, fragment)?.commit()
            dismiss()
        }
        return views
    }

    fun addButtonAction(text: String, action: () -> Unit){
        val button = Button(ContextThemeWrapper(context, R.style.ItemThemeTextView)).apply {
            if (text.isNotEmpty())
                this.text = text
            setOnClickListener {
                action()
            }
            isAllCaps = false
            setBackgroundResource(R.drawable.item_style)
            elevation = 5f
            //typeface = Typeface.createFromAsset(context.assets, "Ubuntu-Light.ttf")
            textSize = 14f
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f).apply {
                val dp5 = resources.getDimension(R.dimen.dp5).toInt()
                val dp10 = resources.getDimension(R.dimen.dp10).toInt()
                setMargins(dp5, dp10, dp5, dp10)
            }
        }
        buttonsGroup.addView(button)
    }
}