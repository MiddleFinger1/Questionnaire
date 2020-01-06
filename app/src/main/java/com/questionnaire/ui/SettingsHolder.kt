package com.questionnaire.ui

import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView.ViewHolder
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import android.widget.Toast
import com.application.R
import com.questionnaire.Settings


class SettingsHolder(val view: View): ViewHolder(view){

    lateinit var activity: AppCompatActivity
    private val titleSettings: TextView
    private val privacySettings: ImageView
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

    fun downloadSettings(settings: Settings){
        settings.apply {
            if (icon != null) {
                val icon = openSource(activity, icon!!)
                if (icon is Drawable)
                    imageView.background = icon
            }
            privacySettings.setBackgroundResource(
                if (privacy == Settings.PUBLIC) R.drawable.ic_lock_open_black_48dp
                else R.drawable.ic_lock_outline_black_48dp
            )
            titleSettings.text = title
            groupSettings.text = group
            markSettings.text = mark.toString()
        }
        view.setOnClickListener {
            if (settings.privacy == Settings.PUBLIC)
                openQuestionnaire(activity, settings)
            else 
                Toast.makeText(activity.baseContext, "Closed", Toast.LENGTH_SHORT).show()
        }
    }

}