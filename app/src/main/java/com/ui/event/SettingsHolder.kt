package com.ui.event

import android.graphics.drawable.Drawable
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.*
import com.ui.custom.CustomModalWindow
import com.logic.IOManager
import com.ui.MainActivity
import com.json.questionnaire.Settings
import com.json.user.ObResult
import java.util.*


class SettingsHolder(val view: View): ViewHolder(view){

    lateinit var activity: MainActivity
    private val titleSettings: TextView
    private val privacySettings: FloatingActionButton
    private val groupSettings: TextView
    private val markSettings: TextView
    private val imageView: ImageView

    init {
        view.apply {
            imageView = findViewById(R.id.Settings_ImageView)
            titleSettings = findViewById(R.id.Settings_Title)
            privacySettings = findViewById(R.id.Settings_Privacy)
            groupSettings = findViewById(R.id.Settings_Group)
            markSettings = findViewById(R.id.Settings_Mark)
        }
    }

    fun downloadSettings(settings: Settings, statePrivacy: Boolean = true){
        settings.apply {
            if (icon != null) {
                val icon = openSource(activity, icon!!)
                if (icon is Drawable)
                    imageView.background = icon
            }
            privacySettings.setImageDrawable(activity.getDrawable(
                if (isPrivate)  R.drawable.ic_lock_outline_black_48dp
                else R.drawable.ic_lock_open_black_48dp
            ))
            titleSettings.text = title
            groupSettings.text = group
            markSettings.text = mark.toString()
        }
        view.setOnClickListener {
            var obItem: ObResult? = null
            for (item in IOManager.user.analytics)
                if (item.id == settings.id)
                    obItem = item
            val boolean = obItem != null && obItem.tries >= settings.tries
            var int = 0
            Log.e("boolean", boolean.toString())
            if (obItem != null && obItem.dateTry != null && settings.dateTry != null){
                Log.e("triesObItem", obItem.tries.toString())
                Log.e("tries.Settings", settings.tries.toString())
                int = obItem.dateTry!!.compareDate(settings.dateTry!! + Calendar.getInstance())
                Log.e("int", int.toString())
            }
            if (int == -1 || boolean || !statePrivacy) {
                Toast.makeText(activity.baseContext, "Пока нельзя", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!settings.isPrivate)
                openQuestionnaire(activity, settings, obItem)
            else {
                val modal = CustomModalWindow()
                modal.setTitle = "Недоступен!"
                modal.setDescription = "Используйте пароль для доступа к анкете"
                modal.action = {
                    addTextEdit("Enter here!")
                    addButtonAction("Cancel", CustomModalWindow.BUTTON_CANCEL){
                        dismiss()
                    }
                    addButtonAction("Ok", CustomModalWindow.BUTTON_CANCEL) {
                        if (entered == settings.password)
                            openQuestionnaire(this@SettingsHolder.activity, settings, obItem)
                        else Toast.makeText(context, "Неправильный пароль", Toast.LENGTH_SHORT).show()
                    }
                }
                modal.show(activity.supportFragmentManager, modal.javaClass.name)
            }
        }
    }
}